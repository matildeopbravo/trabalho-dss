package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.estatisticas.EstatisticasFuncionario;
import dss.gui.components.TabelaEstatisticasFuncionario;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EstatisticasFuncionarios implements Navigatable {
    private TabelaEstatisticasFuncionario tabelaEstatisticasFuncionario;
    private EstatisticasFuncionario selected;
    private final List<EstatisticasFuncionario> estatisticasFuncionarios;
    private final SGRInterface sgr;

    public EstatisticasFuncionarios(SGRInterface sgr) {
        this.estatisticasFuncionarios = sgr.estatisticasFuncionarios();
        tabelaEstatisticasFuncionario = new TabelaEstatisticasFuncionario();
        this.sgr = sgr;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);

        VBox.setVgrow(tabelaEstatisticasFuncionario, Priority.ALWAYS);
        tabelaEstatisticasFuncionario.getItems().setAll(estatisticasFuncionarios);

        HBox buttons = new HBox();
        buttons.setSpacing(5.0);
        vbox.getChildren().addAll(tabelaEstatisticasFuncionario, buttons);

        return vbox;
    }
}
