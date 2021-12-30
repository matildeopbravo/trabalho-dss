package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.Intervencao;
import dss.gui.components.TabelaIntervencoes;
import javafx.scene.Node;

import java.util.List;
import java.util.Map;

public class EstatisticasIntervencoesTecnicos implements Navigatable {
    private SGRInterface sgr;

    public EstatisticasIntervencoesTecnicos(SGRInterface sgr) {
        this.sgr = sgr;
    }

    @Override
    public Node getScene() {
        TabelaIntervencoes t = new TabelaIntervencoes();
        t.setIntervencoes(sgr.intervencoesTecnicos());

        return t;
    }
}
