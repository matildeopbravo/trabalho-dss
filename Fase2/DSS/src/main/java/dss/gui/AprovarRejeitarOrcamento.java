package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.Orcamento;
import dss.business.reparacao.Reparacao;
import dss.business.reparacao.ReparacaoProgramada;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AprovarRejeitarOrcamento implements Navigatable {
    private final SGRInterface sgr;
    private final Navigator navigatable;
    private final TableView<ReparacaoProgramada> orcamentoTableView;
    private final Button aprovar = new Button("Aprovar");
    private final Button rejeitar = new Button("Rejeitar");
    private ReparacaoProgramada selected;

    public AprovarRejeitarOrcamento(SGRInterface sgr, Navigator navigatable) {
        this.sgr = sgr;
        this.navigatable = navigatable;
        this.orcamentoTableView = new TableView<>();

        TableColumn<ReparacaoProgramada, String> idCliente = new TableColumn<>("NIF cliente");
        idCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<ReparacaoProgramada, String> descricao = new TableColumn<>("Descrição");
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        orcamentoTableView.getColumns().addAll(idCliente, descricao);

        this.aprovar.setStyle("-fx-background-color: #2dd430");
        this.rejeitar.setStyle("-fx-background-color: #d42d2d");

        this.aprovar.setDisable(true);
        this.rejeitar.setDisable(true);

        orcamentoTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, old, reparacao) -> {
            selected = reparacao;

            aprovar.setDisable(selected == null);
            rejeitar.setDisable(selected == null);
        });

        orcamentoTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.aprovar.setOnAction(e -> {
            if (selected != null)
                sgr.marcaOrcamentoComoAceite(selected);
            this.orcamentoTableView.getItems().setAll(sgr.reparacoesAguardarAprovacao());
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Orçamento aprovado");
            a.setHeaderText("Orçamento aprovado");
            a.showAndWait();
        });

        this.rejeitar.setOnAction(e -> {
            if (selected != null)
                sgr.marcaOrcamentoComoRecusado(selected);
            this.orcamentoTableView.getItems().setAll(sgr.reparacoesAguardarAprovacao());
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Orçamento aprovado");
            a.setHeaderText("Orçamento aprovado");
            a.showAndWait();
        });
    }

    @Override
    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(5);

        orcamentoTableView.getItems().setAll(sgr.reparacoesAguardarAprovacao());
        VBox.setVgrow(orcamentoTableView, Priority.ALWAYS);
        orcamentoTableView.setMaxHeight(Double.MAX_VALUE);

        HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.getChildren().addAll(aprovar, rejeitar);

        vbox.getChildren().addAll(orcamentoTableView, buttons);
        return vbox;
    }
}
