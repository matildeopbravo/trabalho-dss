package dss;


import dss.clientes.Cliente;
import dss.equipamentos.*;
import dss.estatisticas.*;
import dss.exceptions.*;
import dss.reparacoes.*;
import dss.utilizador.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class SGR  implements  SGRInterface{
    private SGRFacade sgrFacade;
    //####ATRIBUTOS####
    private Utilizador utilizadorAutenticado;
    // servicos expressos
    private final Map<Integer, ServicoExpressoTabelado> servicoExpresso;
    private final Email email ;

    //####CONSTRUTOR####

    public SGR() throws FileNotFoundException {
        this.sgrFacade = new SGRFacade();
        this.utilizadorAutenticado = null;
        // populate this
        this.servicoExpresso = new HashMap<>();
        this.email = new Email();
    }

    public void loadFromFile(String objectFile) throws IOException, ClassNotFoundException {
        FileInputStream fi = new FileInputStream(new File(objectFile));
        ObjectInputStream oi = new ObjectInputStream(fi) ;
        this.sgrFacade =  (SGRFacade) oi.readObject();
        // ver como fazer quanto aos servico expresso
    }

    public void writeToFile(String objectFile) throws IOException {
        FileOutputStream fo = new FileOutputStream(new File(objectFile));
        ObjectOutputStream os = new ObjectOutputStream(fo);
        os.writeObject(this.sgrFacade);
    }

    //####MÉTODOS####
    public void autenticaUtilizador(String id, String senha) throws CredenciasInvalidasException{
        utilizadorAutenticado = sgrFacade.validaCredenciais(id, senha);
        //System.out.println("Utilizador autenticado com sucesso:" + utilizadores.getUtilizador(id));
    }

    public void criaCliente(String NIF, String nome, String email, String numeroTelemovel,
                                 String funcionarioCriador) throws UtilizadorJaExisteException {
        sgrFacade.criaCliente(NIF, nome, email, numeroTelemovel, funcionarioCriador);
    }

    public void criaReparacaoProgramada(String NIFCliente, String descricao) {
        sgrFacade.criaFichaReparacaoProgramada(NIFCliente, utilizadorAutenticado.getId(), descricao);
    }

    public void criaReparacaoExpresso(int idServico, String idCliente, String idTecnico , String descricao) throws ReparacaoJaExisteException {
        ReparacaoExpresso r = new ReparacaoExpresso(servicoExpresso.get(idServico), idCliente,
                utilizadorAutenticado.getId(), idTecnico, descricao);
        sgrFacade.adicionaReparacaoExpressoAtual(r);
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
       Cliente c = sgrFacade.getCliente(reparacao.getIdCliente());
       // TODO
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
        Cliente c = sgrFacade.getCliente(reparacao.getIdCliente());
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

    @Override
    public void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        sgrFacade.adicionaEquipamento(equipamento);
    }

    public Map<String,EstatisticasReparacoesTecnico> estatisticasReparacoesTecnicos() {
        return sgrFacade.estatisticasReparacoesTecnicos();
    }

    public Map<String, List<Intervencao>> intervencoesTecnicos() {
        return sgrFacade.intervencoesTecnicos();
    }

    @Override
    public Map<String, EstatisticasFuncionario> estatisticasFuncionarios() {
        return sgrFacade.estatisticasFuncionarios();
    }

    @Override
    public void registaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException {
        sgrFacade.adicionaUtilizador(utilizador);
    }

    @Override
    public void removeUtilizador(String idUtilizador) throws UtilizadorNaoExisteException {
        sgrFacade.removeUtilizador(idUtilizador);
    }

    @Override
    public Collection<Utilizador> getUtilizadores() {
        return sgrFacade.getUtilizadores();
    }

    @Override
    public Collection<Tecnico> getTecnicos() {
        return sgrFacade.getTecnicos();
    }

    @Override
    public Collection<Funcionario> getFuncionarios() {
        return sgrFacade.getFuncionarios();
    }

    @Override
    //TODO nao tem clone
    public Collection<Cliente> getClientes() {
        return sgrFacade.getClientes();
    }

    @Override
    public Utilizador getUtilizador(String id) throws UtilizadorNaoExisteException {
        return sgrFacade.getUtilizador(id);
    }

    @Override
    public Cliente getCliente(String id) throws UtilizadorNaoExisteException{
        return sgrFacade.getCliente(id);
    }

    @Override
    public Collection<Equipamento> getEquipamentos() {
        return sgrFacade.getEquipamentos();
    }

    @Override
    public Collection<Equipamento> getEquipamentosAbandonados() {
        return sgrFacade.getEquipamentosAbandonados();
    }

    @Override
    //CLONE???
    public Equipamento getEquipamento(String codigo) throws EquipamentoNaoExisteException{
        return sgrFacade.getEquipamento(codigo);
    }

    @Override
    public Collection<Reparacao> getReparacoesConcluidas() {
        return sgrFacade.getReparacoesConcluidas();
    }

    @Override
    public Collection<Reparacao> getReparacoesAtuais() {
        return sgrFacade.getReparacoesAtuais();
    }

    @Override
    public Collection<ReparacaoExpresso> getReparacoesExpresso() {
        return sgrFacade.getReparacoesExpresso();
    }

    @Override
    public Collection<ReparacaoProgramada> getReparacoesProgramadas() {
        return sgrFacade.getReparacoesProgramadas();
    }

    @Override
    public Collection<Componente> getComponentes() {
        return sgrFacade.getComponentes();
    }

    @Override
    public Componente getComponente(Integer id) throws EquipamentoNaoExisteException {
        return sgrFacade.getComponente(id);
    }

    public void marcaReparacaoCompleta(Reparacao reparacao) {
        reparacao.setFase(Fase.Reparado);
    }

    public ReparacaoProgramada obtemReparacaoProgramadaDisponivel() {
        return sgrFacade.getReparacaoProgramadaDisponivel();
    }

    /**
     * Executa passo ou subpasso seguinte da reparação
      */
    public void efetuaReparacaoProgramada(ReparacaoProgramada reparacao, int custoMaoDeObraReal, Duration duracaoReal
                                            , Collection<Componente> componentesReais)
            throws NaoPodeSerReparadoAgoraException, UtilizadorNaoExisteException {
        if (utilizadorAutenticado instanceof Tecnico) {
            boolean completa = sgrFacade.efetuaReparacaoProgramada(reparacao, custoMaoDeObraReal
                    , duracaoReal, componentesReais, (Tecnico) utilizadorAutenticado);
            if (completa) {
                marcaReparacaoCompleta(reparacao);
                enviaMailReparacaoConcluida(reparacao, sgrFacade.getCliente(reparacao.getIdCliente()));
            }
        }
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

    public Tecnico encontraTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException {
        return this.sgrFacade.getTecnicoDisponivel();
    }

    public void iniciaReparacaoExpresso(ReparacaoExpresso r) throws TecnicoNaoAtribuidoException {
        if(!r.getIdTecnicoReparou().equals(utilizadorAutenticado.getId()))
            throw new TecnicoNaoAtribuidoException();
        ((Tecnico) utilizadorAutenticado).ocupaTecnico();
    }

    public void concluiReparacaoExpresso(ReparacaoExpresso r) throws TecnicoNaoAtribuidoException, ReparacaoNaoExisteException {
        if(!r.getIdTecnicoReparou().equals(utilizadorAutenticado.getId()))
            throw new TecnicoNaoAtribuidoException();
        ((Tecnico) utilizadorAutenticado).libertaTecnico();
        sgrFacade.concluiReparacaoExpresso(r.getId());
    }

    // devolve todos os componentes que contêm todas as palavras da stringPesquisa na descrição
    public Collection<Componente> pesquisaComponentes(String stringPesquisa) {
        return sgrFacade.pesquisaComponentes(stringPesquisa);
    }

    public Componente getComponeteByDescricao(String descricao) {
        return sgrFacade.getComponenteByDescricao(descricao);
    }

    public Collection<ReparacaoProgramada> getReparacoesAguardarOrcamento() {
        return sgrFacade.getReparacoesAAguardarOrcamento();
    }

    public Utilizador getUtilizadorAutenticado() {
        return utilizadorAutenticado;
    }
}