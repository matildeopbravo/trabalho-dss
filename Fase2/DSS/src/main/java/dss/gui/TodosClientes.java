package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.cliente.Cliente;
import dss.exceptions.NaoExisteException;
import dss.gui.components.TabelaClientes;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.Optional;

public class TodosClientes implements Navigatable {
    private Button deleteButton;
    private Button addButton;
    private Button detailsButton;
    private Button reparacaoProgramadaButton;
    private Button reparacoesExpressoButton;
    private TabelaClientes tabelaClientes;
    private Cliente selected;

    private final Collection<Cliente> listaClientes;
    private final SGRInterface sgr;
    private final Navigator navigator;

    public TodosClientes(SGRInterface sgr, Navigator navigator) {
        listaClientes = sgr.getClientes();
        this.sgr = sgr;
        this.navigator = navigator;

        deleteButton = new Button("Apagar");
        detailsButton = new Button("Detalhes");
        addButton = new Button("Novo cliente");
        reparacaoProgramadaButton = new Button("Nova reparação programada");
        reparacoesExpressoButton = new Button("Nova reparação expresso");
        tabelaClientes = new TabelaClientes();

        addButton.setOnAction(e -> navigator.navigateTo(new NovoCliente(sgr, navigator)));

        reparacoesExpressoButton.setDisable(true);
        reparacaoProgramadaButton.setDisable(true);
        deleteButton.setDisable(true);
        deleteButton.setStyle("-fx-background-color: rgba(255,1,1,0.86)");

        deleteButton.setOnAction(ev -> {
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Apagar cliente");
                alert.setHeaderText("Apagar cliente " + selected.getNome() + "?");
                Optional<ButtonType> result = alert.showAndWait();

                result.ifPresent(b -> {
                    if (b == ButtonType.OK) {
                        // TODO: Implementar a funcionalidade de apagar clientes
                        System.err.println("Por implementar");
                        try {
                            sgr.apagaCliente(selected.getNIF());
                        } catch (NaoExisteException e) {
                            e.printStackTrace();
                        }
                        tabelaClientes.getItems().setAll(sgr.getClientes());
                    }
                });
            }
        });

        detailsButton.setDisable(true);
        detailsButton.setOnAction(e -> navigator.navigateTo(new DetalhesCliente(sgr, navigator, selected)));
        tabelaClientes.setOnDoubleClick(new TabelaClientes.ClienteCallback() {
            // Não entendo porque é que aqui não me deixa usar uma lambda...
            @Override
            public void run(Cliente cliente) {
                navigator.navigateTo(new DetalhesCliente(sgr, navigator, cliente));
            }
        });
        reparacaoProgramadaButton.setOnAction(e -> navigator.navigateTo(new NovaReparacaoProgramada(sgr, navigator, selected.getNIF())));
        reparacoesExpressoButton.setOnAction(e -> navigator.navigateTo(new NovaReparacaoExpresso(sgr, navigator, selected.getNIF())));

        tabelaClientes.getSelectionModel().selectedItemProperty().addListener((observableValue, old, cliente) -> {
            selected = cliente;

            deleteButton.setDisable(selected == null);
            detailsButton.setDisable(selected == null);
            reparacaoProgramadaButton.setDisable(selected == null);
            reparacoesExpressoButton.setDisable(selected == null);
        });
    }

    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);
        
        VBox.setVgrow(tabelaClientes, Priority.ALWAYS);
        tabelaClientes.getItems().setAll(listaClientes);

        HBox buttons = new HBox();
        buttons.setSpacing(5.0);

        buttons.getChildren().addAll(addButton, detailsButton, reparacaoProgramadaButton, reparacoesExpressoButton, deleteButton);

        vbox.getChildren().addAll(tabelaClientes, buttons);

        return vbox;
    }
}
