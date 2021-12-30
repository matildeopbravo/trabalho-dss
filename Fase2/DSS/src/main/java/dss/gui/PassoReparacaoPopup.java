package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.equipamento.Componente;
import dss.business.reparacao.PassoReparacao;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PassoReparacaoPopup extends PopUp<PassoReparacao> {
    private final SGRInterface sgr;

    private final TextField descricao = new TextField();
    private final TextField duracao = new TextField();
    private final TextField custoMaoDeObra = new TextField();
    private final Button submit = new Button("Criar passo");
    private final Button cancel = new Button("Cancelar");
    private final Button apagarSelecionado = new Button("Apagar");
    private final Button addComponente = new Button("Adicionar");
    private final TableView<Componente> componenteTableView = new TableView<>();
    private final ComboBox<Componente> componenteComboBox = new ComboBox<>();

    public PassoReparacaoPopup(SGRInterface sgr) {
        this.sgr = sgr;

        TableColumn<Componente, String> descricaoComponente = new TableColumn<>("Descrição");
        descricaoComponente.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Componente, String> precoComponente = new TableColumn<>("Preço");
        precoComponente.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getPreco())));

        componenteTableView.getColumns().addAll(descricaoComponente, precoComponente);

        componenteComboBox.getItems().setAll(sgr.getComponentes());
    }

    public PassoReparacaoPopup(SGRInterface sgr, PassoReparacao passoReparacao) {
        this(sgr);
        descricao.setText(passoReparacao.getDescricao());
        duracao.setText(String.valueOf(passoReparacao.getDuracaoPrevista().getSeconds() / 60));
        custoMaoDeObra.setText(String.format("%.2f", passoReparacao.getCustoMaoDeObraPrevisto()));
        componenteTableView.getItems().setAll(passoReparacao.getComponentesPrevistos());
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Novo passo");
    }

    @Override
    protected Scene getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));

        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(10);

        gridPane.add(new Label("Descrição"), 0, 0);
        gridPane.add(new Label("Duração prevista (minutos)"), 0, 1);
        gridPane.add(new Label("Custo previsto"), 0, 2);
        gridPane.add(descricao, 1, 0);
        gridPane.add(duracao, 1, 1);
        gridPane.add(custoMaoDeObra, 1, 2);
        gridPane.setMaxWidth(Double.MAX_VALUE);

        HBox componenteBox = new HBox();
        componenteBox.setSpacing(5);
        componenteBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(componenteComboBox, Priority.ALWAYS);
        addComponente.setOnAction(ev -> {
            if (componenteComboBox.getValue() != null)
                componenteTableView.getItems().add(componenteComboBox.getValue());
        });
        componenteComboBox.setMaxWidth(Double.MAX_VALUE);
        componenteComboBox.setPlaceholder(new Label("Selecionar componente"));
        apagarSelecionado.setDisable(true);

        componenteBox.getChildren().addAll(componenteComboBox, addComponente, apagarSelecionado);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.getChildren().addAll(cancel, submit);

        componenteTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        componenteTableView.setMaxWidth(Double.MAX_VALUE);
        componenteTableView.setMinHeight(100.0);
        componenteTableView.setPrefHeight(100.0);
        componenteTableView.setPlaceholder(new Label("Sem componentes"));

        vbox.getChildren().addAll(gridPane, new Label("Componentes"), componenteTableView, componenteBox, buttonBox);

        return new Scene(vbox);
    }
}
