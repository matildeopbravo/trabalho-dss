package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.gui.components.TabelaReparacoes;
import javafx.scene.Node;
import javafx.scene.control.TableView;

public class TodasReparacoes implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;
    private TabelaReparacoes tabelaReparacoes;

    public TodasReparacoes(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.tabelaReparacoes = new TabelaReparacoes(sgr);
        this.tabelaReparacoes.getItems().setAll(sgr.getReparacoesAtuais());
        this.tabelaReparacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @Override
    public Node getScene() {
        return tabelaReparacoes;
    }
}
