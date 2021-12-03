import ClassesUtilizador.Utilizador;

import java.util.List;

public interface SGRInterface {
    //Devolve verdadeiro se o utilizador é autenticado, falso otherwise
    boolean autenticaUtilizador (String id, String senha);

    //Devolve boolean de sucesso
    boolean adicionaFichaDeCliente (FichaCliente fichaCliente);

    //Tem de gerar para o equipamento um código
    void adicionaEquipamento(Equipamento equipamento);

    //Devolve boolean de sucesso
    boolean adicionaServicoExpresso(FichaReparacaoExpresso servicoExpresso);

    //Devolve boolean de sucesso
    //O serviço expresso terá de ter um indicador de fase, sendo a fase inicial a de
    //reliazação do orçamento/plano de reparação
    boolean adicionaServicoProgramado(FichaReparacaoProgramada servicoProgramado);

    //Devolve a lista das estatísticas de reparações de cada técnico
    List estatisticasTecnicos();

    //Devolve a lista das estatísticas de atendimentos de cada funcionário
    //de balcão
    List estatisticasFuncionarios();

    //Devolve a lista de total de intervenções realizadas por cada técnico
    List totalIntervencoesPorTecnico();

    void alteraFaseServico(Fase fase, String idFicha);

    //Devolve boolean de sucesso
    boolean registaUtilizador (Utilizador utilizador);

    //Devolve boolean de sucesso
    boolean removeUtilizador (String idUtilizador);

    //Devolve boolean de sucesso
    boolean adicionaComponente (Componente componente);

    //Devolve boolean de sucesso
    boolean removeComponente (String codComponente);
}
