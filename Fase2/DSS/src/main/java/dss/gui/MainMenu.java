package dss.gui;

import dss.SGR;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainMenu {
    private SGR sgr;
    private Navigator navigator;

    public MainMenu(SGR sgr, Navigator navigator) {
        this.sgr = sgr;
        this.navigator = navigator;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        Label title = new Label("Menu inicial");
        title.setFont(new Font(24.0));
        vbox.getChildren().add(title);

        Button newClientButton = new Button("Novo cliente");
        newClientButton.setOnAction(e -> navigator.navigateTo(new NovoCliente(sgr, navigator).getScene()));

        Button testPopUp = new Button("Testar popup");
        testPopUp.setOnAction(s -> navigator.openPopup(new ClientsPopUp()));

        vbox.getChildren().addAll(newClientButton, testPopUp);

        return vbox;
    }
}
