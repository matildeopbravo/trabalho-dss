package dss.gui;

import dss.SGR;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainMenu {
    private SGR sgr;
    private Frame frame;

    public MainMenu(SGR sgr, Frame frame) {
        this.sgr = sgr;
        this.frame = frame;
    }

    public Node getScene() {
        VBox vbox = new VBox();
        Label title = new Label("Menu inicial");
        title.setFont(new Font(24.0));
        vbox.getChildren().add(title);

        Button newClientButton = new Button("Novo cliente");
        newClientButton.setOnAction(e -> frame.navigateTo(new NovoCliente(sgr, frame).getScene()));

        vbox.getChildren().add(newClientButton);

        return vbox;
    }
}
