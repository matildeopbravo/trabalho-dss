import java.util.List;

public class FichaReparacaoProgramada extends FichaReparacao {
    Equipamento equipamentoAReparar;
    PlanoReparacao planoReparacao;
    Orcamento orcamento;
    List<Componente> componentes;
    // a ser reparado ou está pausado
    boolean estado;
    Fase fase;

}
