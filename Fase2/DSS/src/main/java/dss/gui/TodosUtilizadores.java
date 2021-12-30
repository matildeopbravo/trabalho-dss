package dss.gui;

import dss.business.SGR.SGRInterface;
import dss.business.cliente.Cliente;
import dss.business.utilizador.Utilizador;
import dss.exceptions.NaoExisteException;
import dss.gui.components.TabelaClientes;
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

public class TodosUtilizadores implements Navigatable {
    private Button deleteButton;
    private Button addButton;
    private Button detailsButton;
    private TabelaUtilizadores tabelaUtilizadores;
    private Utilizador selected;

    private final Collection<Utilizador> listaUtilizadores;
    private final SGRInterface sgr;
    private final Navigator navigator;

    public TodosUtilizadores(SGRInterface sgr, Navigator navigator) {
        listaUtilizadores = sgr.getUtilizadores();
        this.sgr = sgr;
        this.navigator = navigator;

        deleteButton = new Button("Apagar");
        detailsButton = new Button("Detalhes");
        addButton = new Button("Novo Utilizador");
        tabelaUtilizadores = new TabelaUtilizadores();

        addButton.setOnAction(e -> navigator.navigateTo(new NovoUtilizador(sgr, navigator)));

        addButton.setDisable(false);
        deleteButton.setDisable(true);
        deleteButton.setStyle("-fx-background-color: rgba(255,1,1,0.91)");

        deleteButton.setOnAction(ev -> {
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Apagar Utilizador");
                alert.setHeaderText("Apagar utilizador " + selected.getNome() + "?");
                Optional<ButtonType> result = alert.showAndWait();

                result.ifPresent(b -> {
                    if (b == ButtonType.OK) {
                        // TODO: Implementar a funcionalidade de apagar clientes
                        System.err.println("Por implementar");
                        try {
                            sgr.apagaUtilizador(selected.getId());
                        } catch (NaoExisteException ignored) {
                        }
                        tabelaUtilizadores.getItems().setAll(sgr.getUtilizadores());
                    }
                });
            }
        });

        detailsButton.setDisable(true);
        detailsButton.setOnAction(e -> navigator.navigateTo(new DetalhesUtilizador(sgr, navigator, selected)));
        tabelaUtilizadores.setOnDoubleClick(new TabelaUtilizadores.UtilizadorCallback() {
            // Não entendo porque é que aqui não me deixa usar uma lambda...
            @Override
            public void run(Utilizador user) {
                navigator.navigateTo(new DetalhesUtilizador(sgr, navigator, user));
            }
        });

        tabelaUtilizadores.getSelectionModel().selectedItemProperty().addListener((observableValue, old, cliente) -> {
            System.out.println("Selected " + cliente);
            selected = cliente;

            deleteButton.setDisable(selected == null);
            detailsButton.setDisable(selected == null);
        });
    }

    public Node getScene() {
        VBox vbox = new VBox();
        vbox.setSpacing(10.0);
        
        VBox.setVgrow(tabelaUtilizadores, Priority.ALWAYS);
        tabelaUtilizadores.getItems().setAll(listaUtilizadores);

        HBox buttons = new HBox();
        buttons.setSpacing(5.0);

        buttons.getChildren().addAll(addButton, detailsButton, deleteButton);

        vbox.getChildren().addAll(tabelaUtilizadores, buttons);

        return vbox;
    }
}
