package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.Reparacao;
import dss.gui.components.TabelaReparacoes;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TodasReparacoes implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;
    private TabelaReparacoes tabelaReparacoes;
    private Button detalhes;
    private Reparacao selected;

    public TodasReparacoes(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.tabelaReparacoes = new TabelaReparacoes(sgr);
        this.tabelaReparacoes.getItems().setAll(sgr.getReparacoesAtuais());
        this.tabelaReparacoes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.detalhes = new Button();

        detalhes.setDisable(true);
        detalhes.setOnAction(e -> navigator.navigateTo(new DetalhesReparacao(sgr, navigator, selected)));

    }

    @Override
    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);

        VBox.setVgrow(tabelaReparacoes, Priority.ALWAYS);
        tabelaReparacoes.getItems().setAll(sgr.getReparacoesAtuais());

        HBox buttons = new HBox();
        buttons.setSpacing(5.0);

        buttons.getChildren().addAll(detalhes);

        vbox.getChildren().addAll(tabelaReparacoes, buttons);

        return vbox;
    }
}
