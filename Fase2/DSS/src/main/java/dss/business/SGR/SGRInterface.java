package dss.business.SGR;

import dss.business.auxiliar.Pair;
import dss.business.cliente.Cliente;
import dss.business.equipamento.Componente;
import dss.business.equipamento.Equipamento;
import dss.business.estatisticas.EstatisticasFuncionario;
import dss.business.estatisticas.EstatisticasReparacoesTecnico;
import dss.business.reparacao.*;
import dss.business.utilizador.Funcionario;
import dss.business.utilizador.Tecnico;
import dss.business.utilizador.TipoUtilizador;
import dss.business.utilizador.Utilizador;
import dss.exceptions.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SGRInterface {

    void atualizaEquipamentoAbandonado();

    void loadFromFile(String objectFile) throws IOException, ClassNotFoundException;

    void writeToFile(String objectFile) throws IOException;

    ReparacaoProgramada criaReparacaoProgramada(String nifCliente, String descricao) throws NaoExisteException;

    ReparacaoExpresso criaReparacaoExpresso(int idServico, String idCliente, String idTecnico, String descricao) throws FichaDesteClienteJaExisteException;

    void marcaOrcamentoComoAceite(ReparacaoProgramada r);

    void marcaOrcamentoComoRecusado(ReparacaoProgramada r);

    void marcarOrcamentoComoArquivado(ReparacaoProgramada r);

    boolean marcaComoImpossivelReparar(ReparacaoProgramada reparacao) throws NaoExisteException;

    void marcaComoNotificado(Reparacao e);

    boolean realizaOrcamento(ReparacaoProgramada reparacao) throws NaoExisteException;

    void togglePausaReparacao(ReparacaoProgramada reparacao);

    void marcaReparacaoCompleta(Reparacao reparacao);

    Pair<Boolean, Boolean> verificaExcedeOrcamento(float custoNovo, ReparacaoProgramada reparacaoProgramada);

    boolean enviaMailReparacaoConcluida(Reparacao r, Cliente cliente);

    void marcaComoEntregueConcluida(Reparacao r);

    void marcaComoEntregueRecusada(Reparacao r);

    void enviaMailOrcamentoUltrapassado(ReparacaoProgramada r, Cliente c);

    void iniciaReparacaoExpresso(ReparacaoExpresso r) throws TecnicoNaoAtribuidoException;

    void adicionaEquipamento(Equipamento equipamento) throws FichaDesteClienteJaExisteException;

    void concluiReparacao(Reparacao reparacao) throws NaoExisteException;

    Utilizador getUtilizadorAutenticado();

    void autenticaUtilizador(String nome, String senha) throws CredenciasInvalidasException;

    //Devolve a lista das estatísticas de atendimentos de cada funcionário
    //de balcão
    List<EstatisticasFuncionario> estatisticasFuncionarios();

    List<EstatisticasReparacoesTecnico> estatisticasReparacoesTecnicos();

    //Devolve a lista de total de intervenções realizadas por cada técnico
    Map<String, List<Intervencao>> intervencoesTecnicos();

    Tecnico getTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException;

    void criaCliente(String NIF, String nome, String email, String numeroTelemovel,
                     String funcionarioCriador) throws JaExisteException;


    void registaUtilizador(String nome, String id, String password, TipoUtilizador t) throws JaExisteException;

    void registaUtilizador(Utilizador utilizador) throws JaExisteException;

    void apagaUtilizador(String idUtilizador) throws NaoExisteException;

    void apagaCliente(String idCliente) throws NaoExisteException;

    // Getters
    Collection<Utilizador> getUtilizadores();

    Collection<Tecnico> getTecnicos();

    Collection<Funcionario> getFuncionarios();

    Collection<Cliente> getClientes();

    Utilizador getUtilizador(String id) throws NaoExisteException; // devolve null se não existir

    Cliente getCliente(String id) throws NaoExisteException; // devolve null se não existir

    Collection<Equipamento> getEquipamentos();

    Collection<Equipamento> getEquipamentosAbandonados();

    Equipamento getEquipamento(int codigo) throws EquipamentoNaoExisteException; // devolve null se não existir

    Collection<Reparacao> getReparacoesConcluidas();

    Collection<Reparacao> getReparacoesAtuais();

    Collection<ReparacaoExpresso> getReparacoesExpresso();

    Collection<ReparacaoProgramada> getReparacoesProgramadas();

    void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacao) throws ReparacaoJaExisteException;

    Collection<ReparacaoProgramada> getReparacoesAguardarOrcamento();

    Collection<Componente> getComponentes();

    Componente getComponente(Integer id) throws EquipamentoNaoExisteException; // devolve null se não existir

    Collection<Componente> pesquisaComponentes(String stringPesquisa);

    List<ReparacaoProgramada> reparacoesAguardarAprovacao();

    List<ReparacaoProgramada> getReparacoesProgramadasEmCurso();

    List<Tecnico> getTecnicosDisponveis();

    Collection<ServicoExpressoTabelado> getServicosTabelados();

    void marcaComoEntregueConcluida(String text) throws ReparacaoNaoExisteException;

    Equipamento getEquipamentoByIdCliente(String idCliente);

    void marcaComoEntregueConcluida(String idCliente, Duration zero) throws NaoExisteException;
}
