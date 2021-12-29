package dss.gui;

import dss.SGR;
import dss.gui.components.TabelaReparacoes;
import javafx.scene.Node;

public class TodasReparacoes implements Navigatable {
    private SGR sgr;
    private Navigator navigator;
    private TabelaReparacoes tabelaReparacoes;

    public TodasReparacoes(SGR sgr, Navigator navigator) {
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
