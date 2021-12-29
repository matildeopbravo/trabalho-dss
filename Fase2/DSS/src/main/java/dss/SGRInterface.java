package dss;

import dss.clientes.Cliente;
import dss.equipamentos.Componente;
import dss.equipamentos.Equipamento;
import dss.estatisticas.EstatisticasFuncionario;
import dss.reparacoes.Reparacao;
import dss.reparacoes.ReparacaoExpresso;
import dss.reparacoes.ReparacaoProgramada;
import dss.utilizador.Funcionario;
import dss.utilizador.Tecnico;
import dss.utilizador.Utilizador;
import dss.exceptions.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SGRInterface {
    // Devolve o utilizador se a autenticação for bem sucedida, se não devolve null
    //static void autenticaUtilizador(String id, String senha) throws UtilizadorNaoExisteException;

    //Tem de gerar um código para o equipamento, que será devolvido
    void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException;


    // TODO
    //Devolve a lista das estatísticas de atendimentos de cada funcionário
    //de balcão
    Map<String, EstatisticasFuncionario> estatisticasFuncionarios();

    //Devolve a lista de total de intervenções realizadas por cada técnico
    Map<String, List<Intervencao>> intervencoesTecnicos();

    void registaUtilizador (Utilizador utilizador) throws JaExisteException;
    void removeUtilizador (String idUtilizador) throws NaoExisteException;

    // Se o componente já existir, soma a quantidade ao stock
    //void adicionaComponente (Componente componente);
    //void removeComponente (String codComponente) throws ComponenteNaoExisteException;

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

    Collection<Componente> getComponentes();
    Componente getComponente(Integer id) throws EquipamentoNaoExisteException; // devolve null se não existir

}
