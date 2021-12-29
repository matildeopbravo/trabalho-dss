package dss.gui;

import dss.SGR;
import dss.clientes.Cliente;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TodosClientes {
    private Collection<Cliente> listaClientes ;
    public TodosClientes(SGR sgr, Navigator navigator) {
        listaClientes = sgr.getClientes();
    }

    public Node getScene() {
        TableView<Cliente> tabela = new TableView<>();
        TableColumn<Cliente,String> nif = new TableColumn<>("NIF");
        nif.setCellValueFactory(new PropertyValueFactory<>("NIF"));

        TableColumn<Cliente,String> nome = new TableColumn<>("Nome");
        nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        TableColumn<Cliente,String> mail = new TableColumn<>("Email");
        mail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        TableColumn<Cliente,String> telemovel = new TableColumn<>("Telemovel");
        telemovel.setCellValueFactory(new PropertyValueFactory<>("numeroTelemovel"));

        tabela.getColumns().addAll(nif, nome,mail, telemovel);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.getItems().setAll(listaClientes);
        return tabela;
    }
}
