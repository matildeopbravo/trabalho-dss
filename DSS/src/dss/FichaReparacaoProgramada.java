package dss;

import java.util.List;

public class FichaReparacaoProgramada extends FichaReparacao {
    Equipamento equipamentoAReparar;
    PlanoReparacao planoReparacao;
    Orcamento orcamento;
    List<Componente> componentes;
    boolean pausado ;
    Fase fase;

    public FichaReparacaoProgramada(String idCliente, String funcionarioCriador) {
        super(idCliente, funcionarioCriador);
        this.equipamentoAReparar = new Equipamento(idCliente);
        pausado = true;
        this.orcamento = null;
        this.componentes = null;
        fase = Fase.AEsperaOrcamento;
    }
}
