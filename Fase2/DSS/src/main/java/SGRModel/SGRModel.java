package SGRModel;

import dss.Intervencao;
import dss.clientes.Cliente;
import dss.clientes.ClienteDAO;
import dss.clientes.ClienteFacade;
import dss.equipamentos.*;
import dss.estatisticas.EstatisticasFuncionario;
import dss.estatisticas.EstatisticasReparacoesTecnico;
import dss.exceptions.*;
import dss.reparacoes.*;
import dss.utilizador.*;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SGRModel {
    private UtilizadorFacade utilizadorFacade;
    private ReparacaoFacade reparacaoFacade;
    private EquipamentoFacade equipamentoFacade;
    private ClienteFacade clienteFacade;

    public SGRModel() {
//        utilizadorFacade = UtilizadorDAO.lerUtilizadores("utilizadores.obj");
//        reparacaoFacade = ReparacaoDAO.lerReparacoes("reparacoes.obj");
//        equipamentoFacade = EquipamentoDAO.lerEquipamento("equipamento.obj");
//        clienteFacade = ClienteDAO.lerClientes("clientes.obj");

        utilizadorFacade = new UtilizadorFacade();
        reparacaoFacade = new ReparacaoFacade();
        equipamentoFacade = new EquipamentoFacade();
        clienteFacade = new ClienteFacade();

        try {
            utilizadorFacade.adicionaUtilizador(new Gestor("Exemplo", "123456789", "password"));
        } catch (UtilizadorJaExisteException e) {
            e.printStackTrace();
        }

        equipamentoFacade.atualizaEquipamentoAbandonado(reparacaoFacade);
        reparacaoFacade.arquivaReparacoesAntigas();
    }

    //############
    //#UTILIZADOR#
    //############
    public Utilizador validaCredenciais(String nome, String senha) throws CredenciasInvalidasException {
        return utilizadorFacade.validaCredenciais(nome, senha);
    }

    public void adicionaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException {
        utilizadorFacade.adicionaUtilizador(utilizador);
    }

    public void removeUtilizador(String utilizadorID) throws UtilizadorNaoExisteException {
        utilizadorFacade.removeUtilizador(utilizadorID);
    }

    public Utilizador getUtilizador(String utilizadorID) throws UtilizadorNaoExisteException {
        return utilizadorFacade.getUtilizador(utilizadorID);
    }

    public Collection<Utilizador> getUtilizadores() {
        return utilizadorFacade.getUtilizadores();
    }

    public Collection<Tecnico> getTecnicos() {
        return utilizadorFacade.getTecnicos();
    }

    public Collection<Funcionario> getFuncionarios() {
        return utilizadorFacade.getFuncionarios();
    }

    private EstatisticasReparacoesTecnico estatisticasReparacoesByTecnico(Tecnico t) {
        Stream<Reparacao> reparacoesConcluidas = reparacaoFacade.getReparacoesConcluidas()
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
        return utilizadorFacade.getTecnicos().stream()
                .collect(Collectors.toMap(Tecnico::getId, this::estatisticasReparacoesByTecnico));
    }

    private List<Intervencao> getIntervencoesByTecnico(Tecnico t) {
        return Stream.concat(reparacaoFacade.getReparacoesConcluidas().stream()
                        , reparacaoFacade.getReparacoesProgramadasAtuais().stream())
                .filter(r -> r.getTecnicosQueRepararam().contains(t.getId()))
                .map(Reparacao::getIntervencoesRealizadas)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Map<String, List<Intervencao>> intervencoesTecnicos() {
        return utilizadorFacade.getTecnicos().stream()
                .collect(Collectors.toMap(Tecnico::getId, this::getIntervencoesByTecnico));
    }

    private int getNumRececoes(Funcionario f) {
        ArrayList<Reparacao> l = new ArrayList<>(reparacaoFacade.getReparacoesProgramadasAtuais());
        l.addAll(reparacaoFacade.getReparacoesExpressoAtuais());
        l.addAll(reparacaoFacade.getReparacoesConcluidas());
        return (int)
                l.stream()
                        .filter(reparacao -> reparacao.getFuncionarioCriador().equals(f.getId()))
                        .count();
    }

    private int getNumEntregas(Funcionario f) {
        return (int) reparacaoFacade.getReparacoesConcluidas()
                .stream()
                .filter(reparacao -> reparacao.getFuncionarioEntregou().equals(f.getId()))
                .count();
    }

    public Map<String, EstatisticasFuncionario> estatisticasFuncionarios() {
        return utilizadorFacade.getFuncionarios().stream()
                .collect(Collectors.toMap(Funcionario::getId,
                        f -> new EstatisticasFuncionario(getNumRececoes(f), getNumEntregas(f))));
    }

    public Tecnico getTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException{
        return utilizadorFacade.getTecnicos().stream()
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

        clienteFacade.adicionaCliente(cliente);
    }

    public Cliente getCliente(String idCliente) throws UtilizadorNaoExisteException {
        return clienteFacade.getCliente(idCliente);
    }

    public Collection<Cliente> getClientes() {
        return clienteFacade.getClientes();
    }

    //###########
    //#REPARACAO#
    //###########
    public void criaFichaReparacaoProgramada(String nifCliente, String utilizadorID) {
        ReparacaoProgramada reparacao = new ReparacaoProgramada(nifCliente, utilizadorID);
        reparacaoFacade.adicionaReparacaoProgramadaAtual(reparacao);
    }

    public Collection<Reparacao> getReparacoesConcluidas() {
        return reparacaoFacade.getReparacoesConcluidas();
    }

    public Collection<Reparacao> getReparacoesAtuais() {
        Collection<Reparacao> reparacoes = new ArrayList<>(reparacaoFacade.getReparacoesExpressoAtuais());
        reparacoes.addAll(reparacaoFacade.getReparacoesProgramadasAtuais());
        return reparacoes;
    }

    public Collection<ReparacaoProgramada> getReparacoesProgramadas() {
        return reparacaoFacade.getReparacoesProgramadasAtuais();
    }

    public Collection<ReparacaoExpresso> getReparacoesExpresso() {
        return reparacaoFacade.getReparacoesExpressoAtuais();
    }

    public ReparacaoProgramada getReparacaoProgramadaDisponivel() {
        // ir buscar reparacao que esteja em fase propicia a ser reparada
        // e que esteja pausada
        for (ReparacaoProgramada f : reparacaoFacade.getReparacoesProgramadasAtuais()) {
            if (f.podeSerReparadaAgora())
                return f;
        }
        return null;
    }

    public boolean efetuaReparacaoProgramada(ReparacaoProgramada reparacao, int custoReal, Duration duracaoReal, Tecnico tecnico) throws NaoPodeSerReparadoAgoraException{
        return reparacao.efetuaReparacao(tecnico.getId(), custoReal, duracaoReal);
    }

    public void concluiReparacaoExpresso(Integer id) throws ReparacaoNaoExisteException{
        reparacaoFacade.concluiExpresso(id);
    }

    public void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacao) throws ReparacaoJaExisteException {
        reparacaoFacade.adicionaReparacaoExpressoAtual(reparacao);
    }

    public Collection<ReparacaoProgramada> getReparacoesAAguardarOrcamento() {
        return reparacaoFacade.getReparacoesProgramadasAtuais().stream()
                .filter(ReparacaoProgramada::estaPausado) // para garantir que nenhum tecnico esta a reparar
                .filter( r -> r.getFase().equals(Fase.AEsperaOrcamento))
                .collect(Collectors.toList());
    }

    //#############
    //#EQUIPAMENTO#
    //#############
    public void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        equipamentoFacade.adicionaEquipamento(equipamento);
    }

    public Collection<Equipamento> getEquipamentos() {
        return equipamentoFacade.getEquipamentos();
    }

    public Collection<Equipamento> getEquipamentosAbandonados() {
        return equipamentoFacade.getEquipamentosAbandonados();
    }

    public Equipamento getEquipamento(String id) throws EquipamentoNaoExisteException {
        return equipamentoFacade.getEquipamento(id);
    }

    public Collection<Componente> getComponentes() {
        return equipamentoFacade.getComponentes();
    }

    public Componente getComponente(Integer id) throws EquipamentoNaoExisteException{
        return equipamentoFacade.getComponente(id);
    }

    public Collection<Componente> pesquisaComponentes (String stringPesquisa) {
        List<String> searchTokens = Arrays.asList(stringPesquisa.split(" "));
        return equipamentoFacade.getComponentes().stream()
                .filter(comp -> List.of(comp.getDescricao()).containsAll(searchTokens))
                .collect(Collectors.toList());
    }

    public Componente getComponenteByDescricao (String descricao) {
        return equipamentoFacade.getComponentes()
                .stream()
                .filter(e -> e.getDescricao().equals(descricao))
                .findFirst()
                .orElse(null);
    }
}