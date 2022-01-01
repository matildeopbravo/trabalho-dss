package dss.gui;

import dss.business.cliente.Cliente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Duration;

public class DuracaoPopUp extends PopUp<Duration> {
    Navigator nav;

    DuracaoPopUp(Navigator nav) {
        super();
        this.nav = nav;
    }
    @Override
    protected Scene getScene() {
        VBox mainVBox = new VBox();
        mainVBox.setPadding(new Insets(10));
        mainVBox.setSpacing(5);
        Label l = new Label("Duração Real (Minutos)");
        Button submit = new Button("Guardar");
        TextField duracao = new TextField();
        mainVBox.getChildren().setAll(l, duracao, submit);
        submit.setOnAction(ev -> {
            getStage().close();
            getOnDone().run(Duration.ofMinutes(Long.parseLong(duracao.getText())),"");
           nav.navigateBack("Reparação Concluída");
        });
        return new Scene(mainVBox);
    }
}
