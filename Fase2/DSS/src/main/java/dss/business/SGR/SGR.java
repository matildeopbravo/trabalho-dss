package dss.business.SGR;


import dss.business.Email.Email;
import dss.business.cliente.Cliente;
import dss.data.*;
import dss.data.IClientes;
import dss.business.equipamento.*;
import dss.business.estatisticas.*;
import dss.exceptions.*;
import dss.business.reparacao.*;
import dss.business.utilizador.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SGR implements SGRInterface {
    private IUtilizadores utilizadores;
    private IReparacoes reparacoes;
    private IEquipamentos equipamentos;
    private IClientes clientes;
    private Utilizador utilizadorAutenticado;
    private final Map<Integer, ServicoExpressoTabelado> servicoExpresso;
    private final Email email;

    //####CONSTRUTOR####

    public SGR() throws FileNotFoundException {

        this.utilizadorAutenticado = null;
        this.servicoExpresso = ServicoExpressoTabelado.populate();
        this.email = new Email();
        utilizadores = new UtilizadoresDAO();
        reparacoes = new ReparacoesDAO();
        equipamentos = new EquipamentosDAO();
        clientes = new ClientesDAO();

        try {
            utilizadores.add(new Gestor("Exemplo", "123456789", "password"));
            utilizadores.add(new Gestor("Exemplo 2", "1", ""));
        } catch (JaExisteException e) {
            e.printStackTrace();
        }
        atualizaEquipamentoAbandonado();
        reparacoes.arquivaReparacoesAntigas();
    }

    private void atualizaEquipamentoAbandonado() {
        List<Equipamento> abandonados = equipamentos.atualizaEquipamentoAbandonado();
        abandonados.forEach(e -> reparacoes.arquivaReparacoesDeEquipamento(e.getIdEquipamento()));
    }

    // TODO
    public void loadFromFile(String objectFile) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(objectFile));
        ObjectInputStream oi = new ObjectInputStream(fi);
        this.utilizadores = (UtilizadoresDAO) oi.readObject();
        this.reparacoes = (ReparacoesDAO) oi.readObject();
        this.equipamentos = (EquipamentosDAO) oi.readObject();
        this.clientes = (ClientesDAO) oi.readObject();
    }

    public void writeToFile(String objectFile) throws IOException {
        FileOutputStream fo = new FileOutputStream(new File(objectFile));
        ObjectOutputStream os = new ObjectOutputStream(fo);
        os.writeObject(this.utilizadores);
        os.writeObject(this.reparacoes);
        os.writeObject(this.equipamentos);
        os.writeObject(this.clientes);
    }

    //####MÉTODOS####
    public void criaReparacaoExpresso(int idServico, String idCliente, String idTecnico, String descricao) {
        ReparacaoExpresso r = new ReparacaoExpresso(servicoExpresso.get(idServico), idCliente,
                utilizadorAutenticado.getId(), idTecnico, descricao);
        reparacoes.adicionaReparacaoExpressoAtual(r);
    }

    public void marcaOrcamentoComoAceite(ReparacaoProgramada r) {
        r.setFase(Fase.EmReparacao);
        r.marcaComoNaoNotificado();
    }

    public void marcaOrcamentoComoRecusado(ReparacaoProgramada r) {
        r.setFase(Fase.Recusada);
    }

    public void marcarOrcamentoComoArquivado(ReparacaoProgramada r) {
        reparacoes.marcarOrcamentoComoArquivado(r);
    }

    public void marcaComoImpossivelReparar(ReparacaoProgramada reparacao) throws NaoExisteException {
        reparacao.setFase(Fase.NaoPodeSerReparado);
        Cliente c = clientes.get(reparacao.getIdCliente());
        email.enviaMail(c.getEmail(), "Equipamento Não Pode ser Reparado",
                "Após uma análise do estado do equipamento, concluímos que a sua" +
                        "reparação não será possível. Por favor levante o seu equipamento na loja.\n");
        reparacao.marcaComoNotificado();
    }

    // so vai aparecer esta obção tendo criado um passo
    public void adicionaSubpassoPlano(PassoReparacao passo, String descricao, Duration duracao, float custo) {
        passo.addSubpasso(new PassoReparacao(descricao, duracao, custo, new ArrayList<>()));
    }

    public void adicionaPassoPlano(ReparacaoProgramada reparacao, String descricao, Duration duracao, float custo) {
        PlanoReparacao plano = reparacao.getPlanoReparacao();
        if (plano == null) {
            plano = reparacao.criaPlanoReparacao();
        }
        plano.addPasso(descricao, duracao, custo, new ArrayList<>());
    }

    public void marcaComoNotificado(Reparacao e) {
        e.marcaComoNotificado();
    }

    // tem que ter plano de reparacao para poder criar orcamento
    public void realizaOrcamento(ReparacaoProgramada reparacao) throws NaoExisteException {
        Cliente c = clientes.get(reparacao.getIdCliente());
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

    public boolean verificaExcedeOrcamento(float novoCusto, ReparacaoProgramada reparacaoProgramada) {
        return reparacaoProgramada.ultrapassouOrcamento(novoCusto);
    }

    private void enviaMailReparacaoConcluida(Reparacao r, Cliente cliente) {
        email.enviaMail(cliente.getEmail(), "Reparacao Concluida", "Caro " + cliente.getNome() +
                " a sua encomenda está completa. Por favor levante o seu equipamento na loja.\n");
        r.marcaComoNotificado();
    }

    private void marcaComoEntregueConluida(Reparacao r) {
        r.marcaComoEntregueConcluida(utilizadorAutenticado.getId());
    }

    private void marcaComoEntregueRecusada(Reparacao r) {
        r.marcaComoEntregueRecusada(utilizadorAutenticado.getId());
    }

    private void enviaMailOrcamentoUltrapassado(ReparacaoProgramada r, Cliente c) {
        email.enviaMail(c.getEmail(), "Orçamento Ultrapassado", "Caro " + c.getNome() +
                ",\n O Orçamento previsto será ultrapassado. Pretende continuar com o serviço de reparação?" +
                "\n Atenciosamente, Centro de Reparações");
        r.marcaComoNotificado();
    }


    public void iniciaReparacaoExpresso(ReparacaoExpresso r) throws TecnicoNaoAtribuidoException {
        if (!r.getIdTecnicoReparou().equals(utilizadorAutenticado.getId()))
            throw new TecnicoNaoAtribuidoException();
        ((Tecnico) utilizadorAutenticado).ocupaTecnico();
    }

    public void concluiReparacaoExpresso(ReparacaoExpresso r, Duration duracaoReal) throws TecnicoNaoAtribuidoException, ReparacaoNaoExisteException {
        if (!r.getIdTecnicoReparou().equals(utilizadorAutenticado.getId()))
            throw new TecnicoNaoAtribuidoException();
        ((Tecnico) utilizadorAutenticado).libertaTecnico();
        reparacoes.concluiExpresso(r.getId(), duracaoReal);

    }

    // devolve todos os componentes que contêm todas as palavras da stringPesquisa na descrição

    public Utilizador getUtilizadorAutenticado() {
        return utilizadorAutenticado;
    }

    public void autenticaUtilizador(String nome, String senha) throws CredenciasInvalidasException {
        utilizadorAutenticado = ((UtilizadoresDAO) utilizadores).validaCredenciais(nome, senha);
    }

    public void registaUtilizador(Utilizador utilizador) throws JaExisteException {
        utilizadores.add(utilizador);
    }

    public void registaUtilizador(String nome, String id, String password, TipoUtilizador tipo) throws JaExisteException {
        Utilizador user;
        switch (tipo){
            case  Tecnico -> user = new Tecnico(nome,id,password);
            case Funcionario -> user = new Funcionario(nome,id,password);
            default -> user = new Gestor(nome,id,password);
        }
        registaUtilizador(user);
        System.out.println("Utilizador Registado");
    }
    public void removeUtilizador(String utilizadorID) throws NaoExisteException {
        utilizadores.remove(utilizadorID);
    }

    public Utilizador getUtilizador(String utilizadorID) throws NaoExisteException {
        return utilizadores.get(utilizadorID);
    }

    public Collection<Utilizador> getUtilizadores() {
        return utilizadores.getAll();
    }

    public Collection<Tecnico> getTecnicos() {
        return utilizadores.getByClass(Tecnico.class);
    }

    public Collection<Funcionario> getFuncionarios() {
        return utilizadores.getByClass(Funcionario.class);
    }

    private EstatisticasReparacoesTecnico estatisticasReparacoesByTecnico(Tecnico t) {
        Supplier<Stream<Reparacao>> supplierConcluidas = () -> reparacoes.getReparacoesConcluidas()
                .stream()
                .filter(r -> r.getTecnicosQueRepararam().contains(t.getId()));

        Supplier<Stream<Reparacao>> supplierProgramadas = () -> supplierConcluidas.get()
                .filter(r -> r instanceof ReparacaoProgramada);

        int numReparacoesProgramadas = (int)
                supplierProgramadas.get()
                        .count();
        int numReparacoesExpresso = (int) supplierConcluidas.get()
                .filter(r -> r instanceof ReparacaoExpresso)
                .count();
        //duracaoMediaDasReparacosProgramadas
        double duracaoMedia = supplierProgramadas.get()
                .map(Reparacao::getDuracaoReal)
                .map(Duration::toSeconds)
                .mapToLong(Long::longValue)
                .average()
                .orElse(Double.NaN);

        double desvioMedio = supplierProgramadas.get()
                .map(r -> Math.abs(r.getDuracaoPrevista().getSeconds() - r.getDuracaoReal().getSeconds()))
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(Double.NaN);

        return new EstatisticasReparacoesTecnico(t.getId(),numReparacoesExpresso,numReparacoesProgramadas, duracaoMedia, desvioMedio);
    }

    public List<EstatisticasReparacoesTecnico> estatisticasReparacoesTecnicos() {
        return utilizadores.getByClass(Tecnico.class)
                .stream()
                .map(this::estatisticasReparacoesByTecnico)
                .collect(Collectors.toList());
    }

    private List<Intervencao> getIntervencoesByTecnico(Tecnico t) {
        return Stream.concat(reparacoes.getReparacoesConcluidas().stream()
                        , reparacoes.getReparacoesProgramadasAtuais().stream())
                .filter(r -> r.getTecnicosQueRepararam().contains(t.getId()))
                .map(Reparacao::getIntervencoesRealizadas)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Map<String, List<Intervencao>> intervencoesTecnicos() {
        return utilizadores.getByClass(Tecnico.class).stream()
                .collect(Collectors.toMap(Tecnico::getId, this::getIntervencoesByTecnico));
    }

    private int getNumRececoes(Funcionario f) {
        ArrayList<Reparacao> l = new ArrayList<>(reparacoes.getReparacoesProgramadasAtuais());
        l.addAll(reparacoes.getReparacoesExpressoAtuais());
        l.addAll(reparacoes.getReparacoesConcluidas());
        return (int)
                l.stream()
                        .filter(reparacao -> reparacao.getFuncionarioCriador().equals(f.getId()))
                        .count();
    }

    private int getNumEntregas(Funcionario f) {
        return (int) reparacoes.getReparacoesConcluidas()
                .stream()
                .filter(reparacao -> reparacao.getFuncionarioEntregou().equals(f.getId()))
                .count();
    }

    public List<EstatisticasFuncionario> estatisticasFuncionarios() {
        return utilizadores.getByClass(Funcionario.class)
                .stream()
                .map(f -> new EstatisticasFuncionario(f.getId(),getNumRececoes(f), getNumEntregas(f)))
                .collect(Collectors.toList());
    }

    public Tecnico getTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException {
        return utilizadores.getByClass(Tecnico.class).stream()
                .filter(Predicate.not(Tecnico::estaOcupado))
                .findFirst()
                .orElseThrow(NaoHaTecnicosDisponiveisException::new);
    }

    //#########
    //#CLIENTE#
    //#########
    public void criaCliente(String NIF, String nome, String email, String numeroTelemovel,
                            String funcionarioCriador) throws JaExisteException {
        Cliente cliente = new Cliente(NIF, nome, email, numeroTelemovel, funcionarioCriador);

        clientes.add(cliente);
    }

    public Cliente getCliente(String idCliente) throws NaoExisteException {
        return clientes.get(idCliente);
    }

    public Collection<Cliente> getClientes() {
        return clientes.getAll();
    }

    //###########
    //#REPARACAO#
    //###########
    public void criaReparacaoProgramada(String nifCliente, String descricao) throws NaoExisteException {
        if (clientes.get(nifCliente) == null)
            throw new ClienteNaoExisteException();
        ReparacaoProgramada reparacao = new ReparacaoProgramada(nifCliente, utilizadorAutenticado.getId(), descricao);
        reparacoes.adicionaReparacaoProgramadaAtual(reparacao);
    }

    public Collection<Reparacao> getReparacoesConcluidas() {
        return reparacoes.getReparacoesConcluidas();
    }

    public Collection<Reparacao> getReparacoesAtuais() {
        Collection<Reparacao> reparacoes = new ArrayList<>(this.reparacoes.getReparacoesExpressoAtuais());
        reparacoes.addAll(this.reparacoes.getReparacoesProgramadasAtuais());
        return reparacoes;
    }

    public Collection<ReparacaoProgramada> getReparacoesProgramadas() {
        return reparacoes.getReparacoesProgramadasAtuais();
    }

    public Collection<ReparacaoExpresso> getReparacoesExpresso() {
        return reparacoes.getReparacoesExpressoAtuais();
    }

    public ReparacaoProgramada getReparacaoProgramadaDisponivel() {
        // ir buscar reparacao que esteja em fase propicia a ser reparada
        // e que esteja pausada
        for (ReparacaoProgramada f : reparacoes.getReparacoesProgramadasAtuais()) {
            if (f.podeSerReparadaAgora())
                return f;
        }
        return null;
    }


    public void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacao) throws ReparacaoJaExisteException {
        reparacoes.adicionaReparacaoExpressoAtual(reparacao);
    }

    public Collection<ReparacaoProgramada> getReparacoesAguardarOrcamento() {
        return reparacoes.getReparacoesProgramadasAtuais().stream()
                .filter(ReparacaoProgramada::estaPausado) // para garantir que nenhum tecnico esta a reparar
                .filter(r -> r.getFase().equals(Fase.AEsperaOrcamento))
                .collect(Collectors.toList());
    }

    //#############
    //#EQUIPAMENTO#
    //#############
    public void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        equipamentos.adicionaEquipamento(equipamento);
    }

    public Collection<Equipamento> getEquipamentos() {
        return equipamentos.getEquipamentos();
    }

    public Collection<Equipamento> getEquipamentosAbandonados() {
        return equipamentos.getEquipamentosAbandonados();
    }

    @Override
    public Equipamento getEquipamento(int id) throws EquipamentoNaoExisteException {
        return equipamentos.getEquipamento(id);
    }

    public Collection<Componente> getComponentes() {
        return equipamentos.getComponentes();
    }

    public Componente getComponente(Integer id) throws EquipamentoNaoExisteException {
        return equipamentos.getComponente(id);
    }

    public Collection<Componente> pesquisaComponentes(String stringPesquisa) {
        List<String> searchTokens = Arrays.asList(stringPesquisa.split(" "));
        return equipamentos.getComponentes().stream()
                .filter(comp -> List.of(comp.getDescricao()).containsAll(searchTokens))
                .collect(Collectors.toList());
    }

    public Componente getComponenteByDescricao(String descricao) {
        return equipamentos.getComponentes()
                .stream()
                .filter(e -> e.getDescricao().equals(descricao))
                .findFirst()
                .orElse(null);
    }

    public void apagaUtilizador(String idUtilizador) throws NaoExisteException {
        utilizadores.remove(idUtilizador);
    }

    public void apagaCliente(String idCliente) throws NaoExisteException {
        clientes.remove(idCliente);
    }
}
