package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.ReparacaoProgramada;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ReparacoesEmCurso implements Navigatable {
    private SGRInterface sgr;
    private Navigator navigator;
    private TableView<ReparacaoProgramada> tabela;
    private Button reparar = new Button("Reparar");
    private ReparacaoProgramada selected;

    public ReparacoesEmCurso(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.tabela = new TableView<>();

        TableColumn<ReparacaoProgramada, String> nifCliente = new TableColumn<>("NIF Cliente");
        nifCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<ReparacaoProgramada, String> descricao = new TableColumn<>("Descrição");
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        this.tabela.getColumns().setAll(nifCliente, descricao);

        this.reparar.setDisable(true);
        this.tabela.getSelectionModel().selectedItemProperty().addListener((observableValue, old, reparacaoProgramada) -> {
            selected = reparacaoProgramada;

            this.reparar.setDisable(selected == null);
        });
        this.tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.reparar.setOnAction(ev -> {
            if (selected != null) {
                navigator.navigateTo(new Reparar(sgr, navigator, selected));
            }
        });
    }

    @Override
    public Node getScene() {
        VBox vbox = new VBox();

        VBox.setVgrow(this.tabela, Priority.ALWAYS);
        this.tabela.setMaxHeight(Double.MAX_VALUE);

        vbox.setSpacing(5);
        vbox.getChildren().addAll(tabela, reparar);

        return vbox;
    }
}
