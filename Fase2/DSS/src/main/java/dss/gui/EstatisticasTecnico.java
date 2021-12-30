package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.estatisticas.EstatisticasReparacoesTecnico;
import dss.business.utilizador.Utilizador;
import dss.exceptions.NaoExisteException;
import dss.gui.components.TabelaEstatisticasTecnico;
import dss.gui.components.TabelaUtilizadores;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.Optional;

public class EstatisticasTecnico implements Navigatable {
    private TabelaEstatisticasTecnico tabelaEstatisticasTecnico;
    private EstatisticasTecnico selected;

    private final Collection<EstatisticasReparacoesTecnico> estatisticasTecnicos;
    private final SGRInterface sgr;

    public EstatisticasTecnico(SGRInterface sgr) {
        estatisticasTecnicos = sgr.estatisticasReparacoesTecnicos();
        tabelaEstatisticasTecnico = new TabelaEstatisticasTecnico();
        this.sgr = sgr;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);
        
        VBox.setVgrow(tabelaEstatisticasTecnico, Priority.ALWAYS);
        tabelaEstatisticasTecnico.getItems().setAll(estatisticasTecnicos);

        HBox buttons = new HBox();
        buttons.setSpacing(5.0);
        vbox.getChildren().addAll(tabelaEstatisticasTecnico, buttons);

        return vbox;
    }
}
