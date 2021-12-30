package dss.gui;

import dss.business.SGRInterface;
import dss.gui.components.TabelaReparacoes;
import javafx.scene.Node;
import javafx.scene.Scene;

public class TodasReparacoes implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;
    private TabelaReparacoes tabelaReparacoes;

    public TodasReparacoes(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.tabelaReparacoes = new TabelaReparacoes(sgr);
        this.tabelaReparacoes.getItems().setAll(sgr.getReparacoesAtuais());
    }

    @Override
    public Node getScene() {
        return new TabelaReparacoes(sgr);
    }
}
