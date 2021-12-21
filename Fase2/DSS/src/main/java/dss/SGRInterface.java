package dss;

import dss.equipamentos.Componente;
import dss.equipamentos.Equipamento;
import dss.equipamentos.Fase;
import dss.fichas.FichaCliente;
import dss.fichas.FichaReparacao;
import dss.fichas.FichaReparacaoExpresso;
import dss.fichas.FichaReparacaoProgramada;
import dss.utilizador.Funcionario;
import dss.utilizador.Tecnico;
import dss.utilizador.Utilizador;
import dss.estatisticas.EstatisticasFuncionario;
import dss.estatisticas.EstatisticasTecnico;
import dss.exceptions.*;

import java.util.List;
import java.util.Map;

public interface SGRInterface {
    // Devolve o utilizador se a autenticação for bem sucedida, se não devolve null
    //static void autenticaUtilizador(String id, String senha) throws UtilizadorNaoExisteException;

    //Tem de gerar um código para o equipamento, que será devolvido
    String adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException;

    void adicionaServicoExpresso(FichaReparacaoExpresso servicoExpresso) throws ServicoJaExisteException;

    //O serviço expresso terá de ter um indicador de fase, sendo a fase inicial a de
    //reliazação do orçamento/plano de reparação
    void adicionaServicoProgramado(FichaReparacaoProgramada servicoProgramado) throws ServicoJaExisteException;

    //Devolve a lista das estatísticas de reparações de cada técnico
    Map<String, EstatisticasTecnico> estatisticasTecnicos();

    //Devolve a lista das estatísticas de atendimentos de cada funcionário
    //de balcão
    Map<String, EstatisticasFuncionario> estatisticasFuncionarios();

    //Devolve a lista de total de intervenções realizadas por cada técnico
    Map<String, Integer> totalIntervencoesPorTecnico();

    void alteraFaseServico(Fase fase, String idFicha) throws ServicoNaoExisteException;

    void registaUtilizador (Utilizador utilizador) throws UtilizadorJaExisteException;
    void removeUtilizador (String idUtilizador) throws UtilizadorNaoExisteException;

    // Se o componente já existir, soma a quantidade ao stock
    //void adicionaComponente (Componente componente);
    //void removeComponente (String codComponente) throws ComponenteNaoExisteException;

    // Getters
    List<Utilizador> getUtilizadores();
    List<Tecnico> getTecnicos();
    List<Funcionario> getFuncionarios();
    List<FichaCliente> getClientes();

    Utilizador getUtilizador(String id) throws UtilizadorNaoExisteException; // devolve null se não existir
    FichaCliente getCliente(String id); // devolve null se não existir

    List<Equipamento> getEquipamentos();
    List<Equipamento> getEquipamentosAbandonados();
    Equipamento getEquipamento(String codigo); // devolve null se não existir

    List<FichaReparacao> getReparacoes();
    List<FichaReparacao> getReparacoesCompletadas();
    List<FichaReparacao> getReparacoesEmCurso();
    List<FichaReparacaoProgramada> getReparacoesSemOrcamento();
    FichaReparacao getServico(String id); // devolve null se não existir

    List<FichaReparacaoExpresso> getReparacoesExpresso();
    List<FichaReparacaoProgramada> getReparacoesProgramadas();

    List<Componente> getComponentes();
    Componente getComponente(String id); // devolve null se não existir

    void marcaReparacaoCompleta(FichaReparacao f);
}
