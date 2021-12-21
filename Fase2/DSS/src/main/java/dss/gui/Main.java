package dss.gui;

import dss.SGR;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


public class Main {
    @FXML
    private TextField nif;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    private SGR sgr;

    public void login(ActionEvent e)  {
        sgr.autenticaUtilizador(nif.getText(),password.getText());
    }

    public void setSGR(SGR sgr) {
        this.sgr = sgr;
    }
}
