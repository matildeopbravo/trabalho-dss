package dss.business;

import dss.business.clientes.Cliente;
import dss.business.equipamentos.Componente;
import dss.business.equipamentos.Equipamento;
import dss.business.estatisticas.EstatisticasFuncionario;
import dss.business.estatisticas.EstatisticasReparacoesTecnico;
import dss.business.reparacoes.Reparacao;
import dss.business.reparacoes.ReparacaoExpresso;
import dss.business.reparacoes.ReparacaoProgramada;
import dss.business.utilizador.Funcionario;
import dss.business.utilizador.Tecnico;
import dss.business.utilizador.Utilizador;
import dss.exceptions.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

 public interface SGRInterface {

    void loadFromFile(String objectFile) throws IOException, ClassNotFoundException;

    void writeToFile(String objectFile) throws IOException;

     void criaReparacaoProgramada(String nifCliente, String descricao);
    void criaReparacaoExpresso(int idServico, String idCliente, String idTecnico , String descricao);

     void marcaOrcamentoComoRecusado(ReparacaoProgramada r) ;
     void marcarOrcamentoComoArquivado(ReparacaoProgramada r);
     void marcaComoImpossivelReparar (ReparacaoProgramada reparacao) throws NaoExisteException;

     void adicionaSubpassoPlano(PassoReparacao passo, String descricao, Duration duracao, float custo);
     void adicionaPassoPlano(ReparacaoProgramada reparacao, String descricao, Duration duracao, float custo);
     void marcaComoNotificado(Reparacao e);

     void realizaOrcamento(ReparacaoProgramada reparacao) throws NaoExisteException;

     void togglePausaReparacao(ReparacaoProgramada reparacao);

     void marcaReparacaoCompleta(Reparacao reparacao);

     void efetuaReparacaoProgramada(ReparacaoProgramada reparacao, int custoMaoDeObraReal, Duration duracaoReal
            , Collection<Componente> componentesReais)
            throws NaoPodeSerReparadoAgoraException, NaoExisteException;


     void iniciaReparacaoExpresso(ReparacaoExpresso r) throws TecnicoNaoAtribuidoException;

    void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException;
     void concluiReparacaoExpresso(ReparacaoExpresso r, Duration duracaoReal)
            throws TecnicoNaoAtribuidoException, ReparacaoNaoExisteException ;

     Utilizador getUtilizadorAutenticado();

     void autenticaUtilizador(String nome, String senha) throws CredenciasInvalidasException;

    //Devolve a lista das estatísticas de atendimentos de cada funcionário
    //de balcão
    Map<String, EstatisticasFuncionario> estatisticasFuncionarios();
     Map<String, EstatisticasReparacoesTecnico> estatisticasReparacoesTecnicos();

    //Devolve a lista de total de intervenções realizadas por cada técnico
    Map<String, List<Intervencao>> intervencoesTecnicos();
     Tecnico getTecnicoDisponivel() throws NaoHaTecnicosDisponiveisException;

     void criaCliente(String NIF, String nome, String email, String numeroTelemovel,
                            String funcionarioCriador) throws JaExisteException;


    void registaUtilizador (Utilizador utilizador) throws JaExisteException;
    void removeUtilizador (String idUtilizador) throws NaoExisteException;

    public void apagaUtilizador(String idUtilizador) throws NaoExisteException;
    public void apagaCliente(String idCliente) throws NaoExisteException;

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

     public void adicionaReparacaoExpressoAtual(ReparacaoExpresso reparacao) throws ReparacaoJaExisteException;
     public Collection<ReparacaoProgramada> getReparacoesAguardarOrcamento();

    Collection<Componente> getComponentes();
    Componente getComponente(Integer id) throws EquipamentoNaoExisteException; // devolve null se não existir
     public Componente getComponenteByDescricao (String descricao);
     public Collection<Componente> pesquisaComponentes (String stringPesquisa);
}
