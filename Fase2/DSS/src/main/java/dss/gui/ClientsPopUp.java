package dss.gui;

import dss.business.clientes.Cliente;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ClientsPopUp extends PopUp<Cliente> {
    @Override
    protected Scene getScene() {
        return new Scene(new Label("Hello"));
    }
}
