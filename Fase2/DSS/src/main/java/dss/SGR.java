package dss;


import SGRModel.SGRModel;
import dss.clientes.Cliente;
import dss.equipamentos.*;
import dss.estatisticas.*;
import dss.exceptions.*;
import dss.reparacoes.*;
import dss.utilizador.*;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SGR implements SGRInterface {
    private final SGRModel model;
    //####ATRIBUTOS####
    private Utilizador utilizadorAutenticado;
    // servicos expressos
    private final Map<Integer, ServicoExpressoTabelado> servicoExpresso;
    private final Email email ;

    //####CONSTRUTOR####

    public SGR() throws FileNotFoundException {
        this.model = new SGRModel();
        this.utilizadorAutenticado = null;
        this.servicoExpresso = new HashMap<>();
        this.email = new Email();
    }

    //####MÉTODOS####
    public void autenticaUtilizador(String id, String senha) throws CredenciasInvalidasException{
        utilizadorAutenticado = model.validaCredenciais(id, senha);
        //System.out.println("Utilizador autenticado com sucesso:" + utilizadores.getUtilizador(id));
    }

    public void criaCliente(String NIF, String nome, String email, String numeroTelemovel,
                                 String funcionarioCriador) throws UtilizadorJaExisteException {
        model.criaCliente(NIF, nome, email, numeroTelemovel, funcionarioCriador);
    }

    public void criaFichaReparacaoProgramada(String NIFCliente) {
        model.criaFichaReparacaoProgramada(NIFCliente, utilizadorAutenticado.getId());
    }


   public void marcaOrcamentoComoAceite(ReparacaoProgramada r)  {
        r.setFase(Fase.EmReparacao);
   }


    public void marcaOrcamentoComoRecusado(ReparacaoProgramada r) {
        r.setFase(Fase.Recusada);
    }

    public void marcaComoImpossivelReparar (ReparacaoProgramada reparacao) throws UtilizadorNaoExisteException{
       reparacao.setFase(Fase.NaoPodeSerReparado);
       Cliente c = model.getCliente(reparacao.getIdCliente());
       // TODO
       email.enviaMail(c.getEmail(), "Equipamento Não Pode ser Reparado",
               "Após uma análise do estado do equipamento, concluímos que a sua" +
                       "reparação não será possível. Por favor levante o seu equipamento na loja.\n");
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

    // tem que ter plano de reparacao para poder criar orcamento
    public void realizaOrcamento(ReparacaoProgramada reparacao) throws UtilizadorNaoExisteException {
        Cliente c = model.getCliente(reparacao.getIdCliente());
        reparacao.realizaOrcamento(utilizadorAutenticado.getId());
        reparacao.setDataEnvioOrcamento(LocalDateTime.now());
        reparacao.setFase(Fase.AEsperaResposta);
        email.enviaMail(c.getEmail(), "Orçamento",
        reparacao.getOrcamentoMail(c.getNome()));
    }

    public void togglePausaReparacao(ReparacaoProgramada reparacao) {
        reparacao.togglePausarReparacao();
    }

    @Override
    public void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        model.adicionaEquipamento(equipamento);
    }

    public Map<String,EstatisticasReparacoesTecnico> estatisticasReparacoesTecnicos() {
        return model.estatisticasReparacoesTecnicos();
    }

    public Map<String, List<Intervencao>> intervencoesTecnicos() {
        return model.intervencoesTecnicos();
    }

    @Override
    public Map<String, EstatisticasFuncionario> estatisticasFuncionarios() {
        return model.estatisticasFuncionarios();
    }

    @Override
    public void registaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException {
        model.adicionaUtilizador(utilizador);
    }

    @Override
    public void removeUtilizador(String idUtilizador) throws UtilizadorNaoExisteException {
        model.removeUtilizador(idUtilizador);
    }

    @Override
    public Collection<Utilizador> getUtilizadores() {
        return model.getUtilizadores();
    }

    @Override
    public Collection<Tecnico> getTecnicos() {
        return model.getTecnicos();
    }

    @Override
    public Collection<Funcionario> getFuncionarios() {
        return model.getFuncionarios();
    }

    @Override
    //TODO nao tem clone
    public Collection<Cliente> getClientes() {
        return model.getClientes();
    }

    @Override
    public Utilizador getUtilizador(String id) throws UtilizadorNaoExisteException {
        return model.getUtilizador(id);
    }

    @Override
    public Cliente getCliente(String id) throws UtilizadorNaoExisteException{
        return model.getCliente(id);
    }

    @Override
    public Collection<Equipamento> getEquipamentos() {
        return model.getEquipamentos();
    }

    @Override
    public Collection<Equipamento> getEquipamentosAbandonados() {
        return model.getEquipamentosAbandonados();
    }

    @Override
    //CLONE???
    public Equipamento getEquipamento(String codigo) throws EquipamentoNaoExisteException{
        return model.getEquipamento(codigo);
    }

    @Override
    public Collection<Reparacao> getReparacoesConcluidas() {
        return model.getReparacoesConcluidas();
    }

    @Override
    public Collection<Reparacao> getReparacoesAtuais() {
        return model.getReparacoesAtuais();
    }

    @Override
    public Collection<ReparacaoExpresso> getReparacoesExpresso() {
        return model.getReparacoesExpresso();
    }

    @Override
    public Collection<ReparacaoProgramada> getReparacoesProgramadas() {
        return model.getReparacoesProgramadas();
    }

    @Override
    public Collection<Componente> getComponentes() {
        return model.getComponentes();
    }

    @Override
    public Componente getComponente(Integer id) throws EquipamentoNaoExisteException {
        return model.getComponente(id);
    }

    public void marcaReparacaoCompleta(Reparacao reparacao) {
        reparacao.setFase(Fase.Reparado);
    }

    public ReparacaoProgramada obtemReparacaoProgramadaDisponivel() {
        return model.getReparacaoProgramadaDisponivel();
    }

    /**
     * Executa passo ou subpasso seguinte da reparação
      */
    public void efetuaReparacaoProgramada(ReparacaoProgramada reparacao, int custoReal, Duration duracaoReal)
            throws NaoPodeSerReparadoAgoraException, UtilizadorNaoExisteException {
        if (utilizadorAutenticado instanceof Tecnico) {
            boolean completa = model.efetuaReparacaoProgramada(reparacao, custoReal, duracaoReal, (Tecnico) utilizadorAutenticado);
            if (completa) {
                marcaReparacaoCompleta(reparacao);
                enviaMailReparacaoConcluida(model.getCliente(reparacao.getIdCliente()));
            }
        }
    }

    public boolean verificaExcedeOrcamento(float novoCusto, ReparacaoProgramada reparacaoProgramada) {
        return reparacaoProgramada.ultrapassouOrcamento(novoCusto);
    }

    private void enviaMailReparacaoConcluida(Cliente cliente) {
        email.enviaMail(cliente.getEmail(),"Reparacao Concluida", "Caro " + cliente.getNome() +
                " a sua encomenda está completa. Por favor levante o seu equipamento na loja.\n");
    }

    private void marcaComoEntregueConluida(Reparacao r){
        r.marcaComoEntregueConcluida(utilizadorAutenticado.getId());
    }

    private void marcaComoEntregueRecusada(Reparacao r){
        r.marcaComoEntregueRecusada(utilizadorAutenticado.getId());
    }

    private void enviaMailOrcamentoUltrapassado(Cliente c) {
        email.enviaMail(c.getEmail(), "Orçamento Ultrapassado", "Caro " + c.getNome() +
                ",\n O Orçamento previsto será ultrapassado. Pretende continuar com o serviço de reparação?" +
                "\n Atenciosamente, Centro de Reparações");
    }

    public Tecnico encontraTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException {
        return this.model.getTecnicoDisponivel();
    }

    public void concluiReparacaoExpresso(Integer id) throws ReparacaoNaoExisteException {
        model.concluiReparacaoExpresso(id);
    }

    public void iniciaReparacaoExpresso(String idCliente, int idReparacaoEfetuar) throws ReparacaoJaExisteException{
        Tecnico tecnico = (Tecnico) utilizadorAutenticado;
        tecnico.ocupaTecnico();
        ReparacaoExpresso reparacaoExpresso = new ReparacaoExpresso(servicoExpresso.get(idReparacaoEfetuar),
                idCliente,utilizadorAutenticado.getId(),utilizadorAutenticado.getId());
        model.adicionaReparacaoExpressoAtual(reparacaoExpresso);
    }

    // devolve todos os componentes que contêm todas as palavras da stringPesquisa na descrição
    public Collection<Componente> pesquisaComponentes(String stringPesquisa) {
        return model.pesquisaComponentes(stringPesquisa);
    }

    public Componente getComponeteByDescricao(String descricao) {
        return model.getComponenteByDescricao(descricao);
    }

    public Collection<ReparacaoProgramada> getReparacoesAguardarOrcamento() {
        return model.getReparacoesAAguardarOrcamento();
    }
}