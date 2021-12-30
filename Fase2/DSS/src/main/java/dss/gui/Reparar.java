package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.auxiliar.Pair;
import dss.business.cliente.Cliente;
import dss.business.equipamento.Componente;
import dss.business.reparacao.PassoReparacao;
import dss.business.reparacao.ReparacaoProgramada;
import dss.exceptions.NaoExisteException;
import dss.gui.components.TabelaPlanoReparacao;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Duration;

public class Reparar implements Navigatable {
    private final SGRInterface sgr;
    private final Navigator navigator;
    private final ReparacaoProgramada reparacao;
    private final TabelaPlanoReparacao tabelaPlanoReparacao;
    private final TextField duracao = new TextField();
    private final TextField custoMaoDeObra = new TextField();
    private final TableView<Componente> componentesPrevistos = new TableView<>();
    private final TableView<Componente> componentesReais = new TableView<>();
    private final ComboBox<Componente> componenteComboBox = new ComboBox<>();
    private final Button addComponente = new Button("Adicionar");
    private final Button nextButton = new Button("Próximo");

    private Label descricaoPasso = new Label();

    private PassoReparacao atual;

    public Reparar(SGRInterface sgr, Navigator navigator, ReparacaoProgramada reparacaoProgramada) {
        this.sgr = sgr;
        this.navigator = navigator;
        this.reparacao = reparacaoProgramada;
        this.tabelaPlanoReparacao = new TabelaPlanoReparacao(2);
        this.reparacao.togglePausarReparacao();
        this.atual = reparacaoProgramada.getPlanoReparacao().getNextPasso();

        custoMaoDeObra.setOnKeyTyped(e -> this.onValidate());
        duracao.setOnKeyTyped(e -> this.onValidate());

        TableColumn<Componente, String> descricaoComponente = new TableColumn<>("Descrição");
        descricaoComponente.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Componente, String> precoComponente = new TableColumn<>("Preço");
        precoComponente.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getPreco())));

        componentesPrevistos.getColumns().addAll(descricaoComponente, precoComponente);
        componentesReais.getColumns().addAll(descricaoComponente, precoComponente);

        componentesPrevistos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        componentesReais.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        this.nextButton.setOnAction(ev -> {
            if (validate()) {
                float custo = Float.parseFloat(custoMaoDeObra.getText());

                Pair<Boolean, Boolean> res = sgr.verificaExcedeOrcamento(this.reparacao.getCustoTotalReal() + custo, reparacao);
                if (res.getFirst()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Orçamento ultrapassado");
                    alert.setHeaderText("O orçamento foi ultrapassado. O cliente terá de aprovar");
                    if (!res.getSecond()) {
                        try {
                            Cliente c = sgr.getCliente(reparacao.getIdCliente());
                            alert.setContentText("Notifique o cliente para o Nº " + c.getNumeroTelemovel());
                        } catch (NaoExisteException e) {
                            e.printStackTrace();
                        }
                    }
                    alert.showAndWait();
                    navigator.navigateBack("Orçamento excedido");
                } else {
                    this.atual.executa(custo, Duration.ofMinutes(Long.parseLong(duracao.getText())), componentesReais.getItems());

                    this.atual = reparacao.getPlanoReparacao().getNextPasso();
                    if (atual != null)
                        updatePasso();
                    else {
                        navigator.navigateBack("Reparação concluída");
                        sgr.concluiReparacao(reparacao);
                    }
                }
            }
        });

        updatePasso();
    }

    private void onValidate() {
        nextButton.setDisable(!validate());
    }

    private boolean validate() {
        try {
            Integer.parseInt(this.duracao.getText());
            Float.parseFloat(this.custoMaoDeObra.getText());
            return true;
        } catch (NumberFormatException f) {
            return false;
        }
    }

    private void updatePasso() {
        this.descricaoPasso.setText(atual.getDescricao());
        this.duracao.setText(String.valueOf(atual.getDuracaoPrevista().getSeconds() / 60));
        this.custoMaoDeObra.setText(String.valueOf(atual.getCustoMaoDeObraPrevisto()));
        this.componentesPrevistos.getItems().setAll(atual.getComponentesPrevistos());
        this.tabelaPlanoReparacao.setPlano(this.reparacao.getPlanoReparacao());

        componentesReais.getItems().clear();

        for (Componente c : atual.getComponentesPrevistos()) {
            System.out.println(c.toString());
        }
    }

    @Override
    public Node getScene() {
        HBox hBox = new HBox();
        hBox.setSpacing(5.0);
        this.tabelaPlanoReparacao.setMaxHeight(Double.MAX_VALUE);
        this.tabelaPlanoReparacao.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tabelaPlanoReparacao, Priority.ALWAYS);

        Label descricaoNome = new Label("Descricao");
        descricaoNome.setFont(new Font(20));

        descricaoPasso.setWrapText(true);

        VBox passoBox = new VBox();
        passoBox.setSpacing(5.0);

        VBox.setVgrow(componentesReais, Priority.ALWAYS);
        VBox.setVgrow(componentesPrevistos, Priority.ALWAYS);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(10);

        GridPane.setHgrow(duracao, Priority.ALWAYS);
        GridPane.setHgrow(custoMaoDeObra, Priority.ALWAYS);

        gridPane.add(new Label("Duração (minutos)"), 0, 0);
        gridPane.add(new Label("Custo (€)"), 0, 1);
        gridPane.add(duracao, 1, 0);
        gridPane.add(custoMaoDeObra, 1, 1);
        gridPane.setMaxWidth(Double.MAX_VALUE);

        HBox componenteBox = new HBox();
        componenteBox.setSpacing(5);
        componenteBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(componenteComboBox, Priority.ALWAYS);
        addComponente.setOnAction(ev -> {
            if (componenteComboBox.getValue() != null)
                componentesReais.getItems().add(componenteComboBox.getValue());
            this.onValidate();
        });
        componenteComboBox.setMaxWidth(Double.MAX_VALUE);
        componenteComboBox.setPlaceholder(new Label("Selecionar componente"));
        componenteComboBox.getItems().setAll(sgr.getComponentes());
        componenteBox.getChildren().addAll(componenteComboBox, addComponente);

        passoBox.getChildren().addAll(descricaoNome, descricaoPasso, gridPane, new Label("Componentes previstos"),
                componentesPrevistos, new Label("Componentes reais"), componentesReais, componenteBox, nextButton);

        hBox.getChildren().addAll(passoBox, tabelaPlanoReparacao);

        return hBox;
    }

    @Override
    public void onExit() {
        this.reparacao.togglePausarReparacao();
    }
}
