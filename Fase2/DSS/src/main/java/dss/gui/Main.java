package dss.gui;

import dss.SGR;
import dss.exceptions.CredenciasInvalidasException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;


public class Main implements Initializable {
    @FXML
    private TextField nif;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    @FXML
    private Pane errors;

    private SGR sgr;

    public void login(ActionEvent e)  {
        try {
            sgr.autenticaUtilizador(nif.getText(),password.getText());
        } catch (CredenciasInvalidasException ex) {
            showError(ex.getMessage());
            // TODO: Apresentar erro
        }
    }

    public void setSGR(SGR sgr) {
        this.sgr = sgr;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errors.setVisible(false);
    }

    private void resetError() {
        errors.getChildren().clear();
        errors.setVisible(false);
    }

    private void showError(String error) {
        errors.getChildren().add(new Label(error));
        errors.setVisible(true);
    }
}
