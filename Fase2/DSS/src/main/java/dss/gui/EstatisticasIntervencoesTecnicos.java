package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.gui.components.TabelaIntervencoes;
import javafx.scene.Node;

public class EstatisticasIntervencoesTecnicos implements Navigatable {
    private final SGRInterface sgr;

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
