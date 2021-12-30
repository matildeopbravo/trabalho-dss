package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ReparacaoProgramada;
import javafx.scene.Node;

public class Reparar implements Navigatable {
    private final SGRInterface sgr;
    private final Navigator navigator;
    private final ReparacaoProgramada reparacao;

    public Reparar(SGRInterface sgr, Navigator navigator, ReparacaoProgramada reparacaoProgramada) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.reparacao = reparacaoProgramada;
    }

    @Override
    public Node getScene() {
        return null;
    }
}
