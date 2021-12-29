package dss;


import dss.clientes.Cliente;
import dss.clientes.ClientesDAO;
import dss.equipamentos.*;
import dss.estatisticas.*;
import dss.exceptions.*;
import dss.reparacoes.*;
import dss.utilizador.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SGR implements SGRInterface{
    private UtilizadoresDAO utilizadoresDAO;
    private ReparacoesDAO reparacoesDAO;
    private EquipamentosDAO equipamentosDAO;
    private ClientesDAO clientesDAO;
    //####ATRIBUTOS####
    private Utilizador utilizadorAutenticado ;
    // servicos expressos
    private final Map<Integer, ServicoExpressoTabelado> servicoExpresso;
    private final Email email ;

    //####CONSTRUTOR####

    public SGR() throws FileNotFoundException {

        this.utilizadorAutenticado = null;
        // populate this
        this.servicoExpresso = new HashMap<>();
        this.email = new Email();
        utilizadoresDAO = new UtilizadoresDAO();
        reparacoesDAO = new ReparacoesDAO();
        equipamentosDAO = new EquipamentosDAO();
        clientesDAO = new ClientesDAO();

        try {
            utilizadoresDAO.adicionaUtilizador(new Gestor("Exemplo", "123456789", "password"));
            utilizadoresDAO.adicionaUtilizador(new Gestor("Exemplo 2", "1", ""));
        } catch (UtilizadorJaExisteException e) {
            e.printStackTrace();
        }

        equipamentosDAO.atualizaEquipamentoAbandonado(reparacoesDAO);
        reparacoesDAO.arquivaReparacoesAntigas();
    }

    // TODO
    public void loadFromFile(String objectFile) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(objectFile));
        ObjectInputStream oi = new ObjectInputStream(fi) ;
        this.utilizadoresDAO =  (UtilizadoresDAO) oi.readObject();
        this.reparacoesDAO =  (ReparacoesDAO) oi.readObject();
        this.equipamentosDAO =  (EquipamentosDAO) oi.readObject();
        this.clientesDAO =  (ClientesDAO) oi.readObject();
    }

    public void writeToFile(String objectFile) throws IOException {
        FileOutputStream fo = new FileOutputStream(new File(objectFile));
        ObjectOutputStream os = new ObjectOutputStream(fo);
        os.writeObject(this.utilizadoresDAO);
        os.writeObject(this.reparacoesDAO);
        os.writeObject(this.equipamentosDAO);
        os.writeObject(this.clientesDAO);
    }

    //####MÉTODOS####


    public void criaReparacaoExpresso(int idServico, String idCliente, String idTecnico , String descricao) throws ReparacaoJaExisteException {
        ReparacaoExpresso r = new ReparacaoExpresso(servicoExpresso.get(idServico), idCliente,
                utilizadorAutenticado.getId(), idTecnico, descricao);
        reparacoesDAO.adicionaReparacaoExpressoAtual(r);
    }

   public void marcaOrcamentoComoAceite(ReparacaoProgramada r)  {
        r.setFase(Fase.EmReparacao);
        r.marcaComoNaoNotificado();
   }

    public void marcaOrcamentoComoRecusado(ReparacaoProgramada r) {
        r.setFase(Fase.Recusada);
    }

    public void marcaComoImpossivelReparar (ReparacaoProgramada reparacao) throws UtilizadorNaoExisteException{
       reparacao.setFase(Fase.NaoPodeSerReparado);
       Cliente c = clientesDAO.getCliente(reparacao.getIdCliente());
       email.enviaMail(c.getEmail(), "Equipamento Não Pode ser Reparado",
               "Após uma análise do estado do equipamento, concluímos que a sua" +
                       "reparação não será possível. Por favor levante o seu equipamento na loja.\n");
       reparacao.marcaComoNotificado();
    }

    // so vai aparecer esta obção tendo criado um passo
    public void adicionaSubpassoPlano(PassoReparacao passo, String descricao, Duration duracao, float custo) {
       passo.addSubpasso(new PassoReparacao(descricao,duracao,custo));
    }

    public void adicionaPassoPlano(ReparacaoProgramada reparacao, String descricao, Duration duracao, float custo) {
        PlanoReparacao plano = reparacao.getPlanoReparacao();
        if (plano == null) {
            plano = reparacao.criaPlanoReparacao();
       }
       plano.addPasso(descricao,duracao,custo);
    }

    public void marcaComoNotificado(Reparacao e) {
        e.marcaComoNotificado();
    }

    // tem que ter plano de reparacao para poder criar orcamento
    public void realizaOrcamento(ReparacaoProgramada reparacao) throws UtilizadorNaoExisteException {
        Cliente c = clientesDAO.getCliente(reparacao.getIdCliente());
        reparacao.realizaOrcamento(utilizadorAutenticado.getId());
        reparacao.setDataEnvioOrcamento(LocalDateTime.now());
        reparacao.setFase(Fase.AEsperaResposta);
        email.enviaMail(c.getEmail(), "Orçamento",
        reparacao.getOrcamentoMail(c.getNome()));
        reparacao.marcaComoNotificado();
    }

    public void togglePausaReparacao(ReparacaoProgramada reparacao) {
        reparacao.togglePausarReparacao();
    }




    public void marcaReparacaoCompleta(Reparacao reparacao) {
        reparacao.setFase(Fase.Reparado);
    }


    /**
     * Executa passo ou subpasso seguinte da reparação
      */
    public void efetuaReparacaoProgramada(ReparacaoProgramada reparacao, int custoMaoDeObraReal, Duration duracaoReal
                                            , Collection<Componente> componentesReais)
            throws NaoPodeSerReparadoAgoraException, UtilizadorNaoExisteException {
        //if (utilizadorAutenticado instanceof Tecnico) {
                boolean completa = reparacao.efetuaReparacao(utilizadorAutenticado.getId(),
                        custoMaoDeObraReal, duracaoReal, componentesReais);
            if (completa) {
                marcaReparacaoCompleta(reparacao);
                enviaMailReparacaoConcluida(reparacao, clientesDAO.getCliente(reparacao.getIdCliente()));
            }
        //}
    }

    public boolean verificaExcedeOrcamento(float novoCusto, ReparacaoProgramada reparacaoProgramada) {
        return reparacaoProgramada.ultrapassouOrcamento(novoCusto);
    }

    private void enviaMailReparacaoConcluida(Reparacao r , Cliente cliente) {
        email.enviaMail(cliente.getEmail(),"Reparacao Concluida", "Caro " + cliente.getNome() +
                " a sua encomenda está completa. Por favor levante o seu equipamento na loja.\n");
        r.marcaComoNotificado();
    }

    private void marcaComoEntregueConluida(Reparacao r){
        r.marcaComoEntregueConcluida(utilizadorAutenticado.getId());
    }

    private void marcaComoEntregueRecusada(Reparacao r){
        r.marcaComoEntregueRecusada(utilizadorAutenticado.getId());
    }

    private void enviaMailOrcamentoUltrapassado(ReparacaoProgramada r, Cliente c) {
        email.enviaMail(c.getEmail(), "Orçamento Ultrapassado", "Caro " + c.getNome() +
                ",\n O Orçamento previsto será ultrapassado. Pretende continuar com o serviço de reparação?" +
                "\n Atenciosamente, Centro de Reparações");
        r.marcaComoNotificado();
    }


    public void iniciaReparacaoExpresso(ReparacaoExpresso r) throws TecnicoNaoAtribuidoException {
        if(!r.getIdTecnicoReparou().equals(utilizadorAutenticado.getId()))
            throw new TecnicoNaoAtribuidoException();
        ((Tecnico) utilizadorAutenticado).ocupaTecnico();
    }

    public void concluiReparacaoExpresso(ReparacaoExpresso r, Duration duracaoReal) throws TecnicoNaoAtribuidoException, ReparacaoNaoExisteException {
        if(!r.getIdTecnicoReparou().equals(utilizadorAutenticado.getId()))
            throw new TecnicoNaoAtribuidoException();
        ((Tecnico) utilizadorAutenticado).libertaTecnico();
        reparacoesDAO.concluiExpresso(r.getId(),duracaoReal);

    }

    // devolve todos os componentes que contêm todas as palavras da stringPesquisa na descrição

    public Utilizador getUtilizadorAutenticado() {
        return utilizadorAutenticado;
    }

    public void autenticaUtilizador(String nome, String senha) throws CredenciasInvalidasException {
        utilizadorAutenticado = utilizadoresDAO.validaCredenciais(nome, senha);
    }

    public void registaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException {
        utilizadoresDAO.adicionaUtilizador(utilizador);
    }

    public void removeUtilizador(String utilizadorID) throws UtilizadorNaoExisteException {
        utilizadoresDAO.removeUtilizador(utilizadorID);
    }

    public Utilizador getUtilizador(String utilizadorID) throws UtilizadorNaoExisteException {
        return utilizadoresDAO.getUtilizador(utilizadorID);
    }

    public Collection<Utilizador> getUtilizadores() {
        return utilizadoresDAO.getUtilizadores();
    }

    public Collection<Tecnico> getTecnicos() {
        return utilizadoresDAO.getTecnicos();
    }

    public Collection<Funcionario> getFuncionarios() {
        return utilizadoresDAO.getFuncionarios();
    }

    private EstatisticasReparacoesTecnico estatisticasReparacoesByTecnico(Tecnico t) {
        Stream<Reparacao> reparacoesConcluidas = reparacoesDAO.getReparacoesConcluidas()
                .stream()
                .filter(r -> r.getTecnicosQueRepararam().contains(t.getId()));

        Stream<Reparacao> reparacoesProgramadas = reparacoesConcluidas
                .filter(r -> r instanceof ReparacaoProgramada);

        int numReparacoesProgramadas = (int)
                reparacoesProgramadas
                        .count();
        int numReparacoesExpresso = (int) reparacoesConcluidas
                .filter(r -> r instanceof ReparacaoExpresso)
                .count();
        //duracaoMediaDasReparacosProgramadas
        double duracaoMedia = reparacoesProgramadas
                .map(Reparacao::getDuracaoReal)
                .map(Duration::toSeconds)
                .mapToLong(Long::longValue)
                .average()
                .orElse(Double.NaN);

        double desvioMedio = reparacoesProgramadas
                .map(r -> Math.abs(r.getDuracaoPrevista().getSeconds() - r.getDuracaoReal().getSeconds()))
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(Double.NaN);

        return new EstatisticasReparacoesTecnico(numReparacoesExpresso,numReparacoesProgramadas, duracaoMedia, desvioMedio);
    }

    public Map<String, EstatisticasReparacoesTecnico> estatisticasReparacoesTecnicos() {
        return utilizadoresDAO.getTecnicos().stream()
                .collect(Collectors.toMap(Tecnico::getId, this::estatisticasReparacoesByTecnico));
    }

    private List<Intervencao> getIntervencoesByTecnico(Tecnico t) {
        return Stream.concat(reparacoesDAO.getReparacoesConcluidas().stream()
                        , reparacoesDAO.getReparacoesProgramadasAtuais().stream())
                .filter(r -> r.getTecnicosQueRepararam().contains(t.getId()))
                .map(Reparacao::getIntervencoesRealizadas)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Map<String, List<Intervencao>> intervencoesTecnicos() {
        return utilizadoresDAO.getTecnicos().stream()
                .collect(Collectors.toMap(Tecnico::getId, this::getIntervencoesByTecnico));
    }

    private int getNumRececoes(Funcionario f) {
        ArrayList<Reparacao> l = new ArrayList<>(reparacoesDAO.getReparacoesProgramadasAtuais());
        l.addAll(reparacoesDAO.getReparacoesExpressoAtuais());
        l.addAll(reparacoesDAO.getReparacoesConcluidas());
        return (int)
                l.stream()
                        .filter(reparacao -> reparacao.getFuncionarioCriador().equals(f.getId()))
                        .count();
    }

    private int getNumEntregas(Funcionario f) {
        return (int) reparacoesDAO.getReparacoesConcluidas()
                .stream()
                .filter(reparacao -> reparacao.getFuncionarioEntregou().equals(f.getId()))
                .count();
    }

    public Map<String, EstatisticasFuncionario> estatisticasFuncionarios() {
        return utilizadoresDAO.getFuncionarios().stream()
                .collect(Collectors.toMap(Funcionario::getId,
                        f -> new EstatisticasFuncionario(getNumRececoes(f), getNumEntregas(f))));
    }

    public Tecnico getTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException{
        return utilizadoresDAO.getTecnicos().stream()
                .filter(Predicate.not(Tecnico::estaOcupado))
                .findFirst()
                .orElseThrow(NaoHaTecnicosDisponiveisException::new);
    }

    //#########
    //#CLIENTE#
    //#########
    public void criaCliente(String NIF, String nome, String email, String numeroTelemovel,
                            String funcionarioCriador) throws UtilizadorJaExisteException {
        Cliente cliente = new Cliente(NIF,nome,email,numeroTelemovel,funcionarioCriador);

        clientesDAO.adicionaCliente(cliente);
    }

    public Cliente getCliente(String idCliente) throws UtilizadorNaoExisteException {
        return clientesDAO.getCliente(idCliente);
    }

    public Collection<Cliente> getClientes() {
        return clientesDAO.getClientes();
    }

    //###########
    //#REPARACAO#
    //###########
    public void criaReparacaoProgramada(String nifCliente, String descricao) {
        ReparacaoProgramada reparacao = new ReparacaoProgramada(nifCliente, utilizadorAutenticado.getId(), descricao);
        reparacoesDAO.adicionaReparacaoProgramadaAtual(reparacao);
    }

    public Collection<Reparacao> getReparacoesConcluidas() {
        return reparacoesDAO.getReparacoesConcluidas();
    }

    public Collection<Reparacao> getReparacoesAtuais() {
        Collection<Reparacao> reparacoes = new ArrayList<>(reparacoesDAO.getReparacoesExpressoAtuais());
        reparacoes.addAll(reparacoesDAO.getReparacoesProgramadasAtuais());
        return reparacoes;
    }

    public Collection<ReparacaoProgramada> getReparacoesProgramadas() {
        return reparacoesDAO.getReparacoesProgramadasAtuais();
    }

    public Collection<ReparacaoExpresso> getReparacoesExpresso() {
        return reparacoesDAO.getReparacoesExpressoAtuais();
    }

    public ReparacaoProgramada getReparacaoProgramadaDisponivel() {
        // ir buscar reparacao que esteja em fase propicia a ser reparada
        // e que esteja pausada
        for (ReparacaoProgramada f : reparacoesDAO.getReparacoesProgramadasAtuais()) {
            if (f.podeSerReparadaAgora())
                return f;
        }
        return null;
    }


    public void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacao) throws ReparacaoJaExisteException {
        reparacoesDAO.adicionaReparacaoExpressoAtual(reparacao);
    }

    public Collection<ReparacaoProgramada> getReparacoesAguardarOrcamento() {
        return reparacoesDAO.getReparacoesProgramadasAtuais().stream()
                .filter(ReparacaoProgramada::estaPausado) // para garantir que nenhum tecnico esta a reparar
                .filter( r -> r.getFase().equals(Fase.AEsperaOrcamento))
                .collect(Collectors.toList());
    }

    //#############
    //#EQUIPAMENTO#
    //#############
    public void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        equipamentosDAO.adicionaEquipamento(equipamento);
    }

    public Collection<Equipamento> getEquipamentos() {
        return equipamentosDAO.getEquipamentos();
    }

    public Collection<Equipamento> getEquipamentosAbandonados() {
        return equipamentosDAO.getEquipamentosAbandonados();
    }

    public Equipamento getEquipamento(String id) throws EquipamentoNaoExisteException {
        return equipamentosDAO.getEquipamento(id);
    }

    public Collection<Componente> getComponentes() {
        return equipamentosDAO.getComponentes();
    }

    public Componente getComponente(Integer id) throws EquipamentoNaoExisteException{
        return equipamentosDAO.getComponente(id);
    }

    public Collection<Componente> pesquisaComponentes (String stringPesquisa) {
        List<String> searchTokens = Arrays.asList(stringPesquisa.split(" "));
        return equipamentosDAO.getComponentes().stream()
                .filter(comp -> List.of(comp.getDescricao()).containsAll(searchTokens))
                .collect(Collectors.toList());
    }

    public Componente getComponenteByDescricao (String descricao) {
        return equipamentosDAO.getComponentes()
                .stream()
                .filter(e -> e.getDescricao().equals(descricao))
                .findFirst()
                .orElse(null);
    }

    public void apagaUtilizador(String idUtilizador) throws UtilizadorNaoExisteException {
        utilizadoresDAO.removeUtilizador(idUtilizador);
    }
    public void apagaCliente(String idCliente) throws ClienteNaoExisteException {
        clientesDAO.removeCliente(idCliente);
    }
}