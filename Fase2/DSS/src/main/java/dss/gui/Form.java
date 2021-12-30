package dss.gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

public abstract class Form {
    private Map<String, Node> inputs;
    protected String title;
    private String buttonText;
    private Button button;

    protected void init(String title, Map<String, Node> inputs, String button) {
        this.title = title;
        this.inputs = inputs;
        this.buttonText = button;

        this.button = new Button(buttonText);

        for (Node value : this.inputs.values()) {
            if (value instanceof TextField field) {
                field.textProperty().addListener(ev -> this.button.setDisable(!this.validateSubmit()));
                field.setOnAction(ev -> this.submit());
            }
        }
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
        this.button.setText(buttonText);
    }

    protected abstract boolean validateSubmit();
    protected abstract List<String> submit(); // Devolve lista de erros

    public Node getScene() {
        HBox hcontainer = new HBox();
        VBox vcontainer = new VBox();

        vcontainer.setSpacing(16.0);

        hcontainer.setAlignment(Pos.CENTER);
        vcontainer.setAlignment(Pos.CENTER);

        Label title = new Label(this.title);
        title.setFont(new Font(24.0));

        vcontainer.getChildren().add(title);
        hcontainer.getChildren().add(vcontainer);

        GridPane grid = new GridPane();
        grid.setVgap(5.0);
        grid.setHgap(10.0);

        int i = 0;
        for (Map.Entry<String, Node> input : inputs.entrySet()) {
            grid.add(new Label(input.getKey()), 0, i);
            grid.add(input.getValue(), 1, i);
            i++;
        }

        vcontainer.getChildren().add(grid);

        this.button.setMaxWidth(Double.MAX_VALUE);
        this.button.setOnAction(ev -> {
            List<String> errors = submit();
            if (errors.size() > 0) {
                Alert erros = new Alert(Alert.AlertType.ERROR);
                erros.setHeaderText("Foram encontrados erros ao submeter:");
                erros.setTitle("Erro");
                erros.setContentText(errors.stream().map(s -> " - " + s + "\n").reduce("", (a, b) -> a + b));
                erros.showAndWait();
            }
        });
        this.button.setDisable(!validateSubmit());

        vcontainer.getChildren().add(this.button);

        return hcontainer;
    }
}
