package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.reparacao.Reparacao;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AguardarOrcamento implements Navigatable {
    private Button criarOrçamento = new Button("Criar orçamento");
    private Reparacao selected;
    private Navigator navigator;
    private SGRInterface sgr;

    public AguardarOrcamento(SGRInterface sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
        criarOrçamento.setDisable(true);
    }

    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(5.0);

        TableView<Reparacao> tabela = new TableView<>();
        TableColumn<Reparacao, String> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Reparacao, String> nome = new TableColumn<>("NIF Cliente");
        nome.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        TableColumn<Reparacao, String> descricao = new TableColumn<>("Descrição");
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tabela.getColumns().addAll(id, nome, descricao);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.getItems().setAll(sgr.getReparacoesAguardarOrcamento());

        tabela.getSelectionModel().selectedItemProperty().addListener((observableValue, old, reparacao) -> {
            selected = reparacao;
            criarOrçamento.setDisable(selected == null);
        });

        VBox.setVgrow(tabela, Priority.ALWAYS);

        HBox botoes = new HBox();
        botoes.setSpacing(5);
        botoes.getChildren().add(criarOrçamento);

        criarOrçamento.setOnAction(ev -> {
            navigator.navigateTo(new NovoOrcamento(sgr, navigator, selected));
        });

        vbox.getChildren().addAll(tabela, botoes);
        return vbox;
    }
}
