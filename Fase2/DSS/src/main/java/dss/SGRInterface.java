package dss;

import dss.equipamentos.Componente;
import dss.equipamentos.Equipamento;
import dss.equipamentos.Fase;
import dss.estatisticas.EstatisticasFuncionario;
import dss.reparacoes.Reparacao;
import dss.reparacoes.ReparacaoExpresso;
import dss.reparacoes.ReparacaoProgramada;
import dss.utilizador.Funcionario;
import dss.utilizador.Tecnico;
import dss.utilizador.Utilizador;
import dss.exceptions.*;

import java.util.List;
import java.util.Map;

public interface SGRInterface {
    // Devolve o utilizador se a autenticação for bem sucedida, se não devolve null
    //static void autenticaUtilizador(String id, String senha) throws UtilizadorNaoExisteException;

    //Tem de gerar um código para o equipamento, que será devolvido
    String adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException;


    // TODO
    //Devolve a lista das estatísticas de atendimentos de cada funcionário
    //de balcão
    Map<String, EstatisticasFuncionario> estatisticasFuncionarios();

    //Devolve a lista de total de intervenções realizadas por cada técnico
    Map<String, List<Intervencao>> intervencoesTecnicos();

    void registaUtilizador (Utilizador utilizador) throws UtilizadorJaExisteException;
    void removeUtilizador (String idUtilizador) throws UtilizadorNaoExisteException;

    // Se o componente já existir, soma a quantidade ao stock
    //void adicionaComponente (Componente componente);
    //void removeComponente (String codComponente) throws ComponenteNaoExisteException;

    // Getters
    List<Utilizador> getUtilizadores();
    List<Tecnico> getTecnicos();
    List<Funcionario> getFuncionarios();
    List<Cliente> getClientes();

    Utilizador getUtilizador(String id) throws UtilizadorNaoExisteException; // devolve null se não existir
    Cliente getCliente(String id); // devolve null se não existir

    List<Equipamento> getEquipamentos();
    List<Equipamento> getEquipamentosAbandonados();
    Equipamento getEquipamento(String codigo); // devolve null se não existir

    List<Reparacao> getReparacoes();
    List<Reparacao> getReparacoesCompletadas();
    List<Reparacao> getReparacoesEmCurso();
    List<ReparacaoProgramada> getReparacoesSemOrcamento();
    Reparacao getServico(String id); // devolve null se não existir

    List<ReparacaoExpresso> getReparacoesExpresso();
    List<ReparacaoProgramada> getReparacoesProgramadas();

    List<Componente> getComponentes();
    Componente getComponente(String id); // devolve null se não existir

}
