package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.PassoReparacao;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ReparacaoProgramada;
import dss.gui.components.TabelaPlanoReparacao;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.awt.*;

public class Reparar implements Navigatable {
    private final SGRInterface sgr;
    private final Navigator navigator;
    private final ReparacaoProgramada reparacao;
    private final TabelaPlanoReparacao tabelaPlanoReparacao;

    private Label descricaoPasso;

    private PassoReparacao atual;

    public Reparar(SGRInterface sgr, Navigator navigator, ReparacaoProgramada reparacaoProgramada) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.reparacao = reparacaoProgramada;
        this.tabelaPlanoReparacao = new TabelaPlanoReparacao(2);
        this.tabelaPlanoReparacao.setPlano(reparacaoProgramada.getPlanoReparacao());
        this.reparacao.togglePausarReparacao();
        this.atual = reparacaoProgramada.getPlanoReparacao().getNextPasso();
    }

    @Override
    public Node getScene() {
        HBox hBox = new HBox();
        hBox.setSpacing(5.0);
        this.tabelaPlanoReparacao.setMaxHeight(Double.MAX_VALUE);
        this.tabelaPlanoReparacao.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabelaPlanoReparacao, Priority.ALWAYS);



        hBox.getChildren().addAll(tabelaPlanoReparacao);

        return hBox;
    }

    @Override
    public void onExit() {
        this.reparacao.togglePausarReparacao();
    }
}
