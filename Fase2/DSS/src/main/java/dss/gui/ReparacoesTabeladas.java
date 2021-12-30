package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.ServicoExpressoTabelado;
import dss.gui.components.TabelaServicosTabelados;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;

public class ReparacoesTabeladas implements Navigatable {
    private TabelaServicosTabelados tabela;
    private ReparacoesTabeladas selected;

    private final Collection<ServicoExpressoTabelado> tabelados;
    private final SGRInterface sgr;

    public ReparacoesTabeladas(SGRInterface sgr) {
        tabelados = sgr.getServicosTabelados();
        tabela = new TabelaServicosTabelados();
        this.sgr = sgr;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);
        
        VBox.setVgrow(tabela, Priority.ALWAYS);
        tabela.getItems().setAll(tabelados);

        HBox buttons = new HBox();
        buttons.setSpacing(5.0);
        vbox.getChildren().addAll(tabela, buttons);

        return vbox;
    }
}
