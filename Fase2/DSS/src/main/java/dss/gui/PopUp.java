package dss.gui;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class PopUp<T> implements Navigator {
    class OnDone {
        public void run(T result, String message) {}
    }

    private Node content;
    private Navigator parent;
    private Stage stage;
    private OnDone onDone;

    protected abstract Scene getScene();

    @Override
    public void navigateTo(Node node) {
        // TODO: stack
    }

    @Override
    public void navigateBack() {
        // TODO: Do nothing?
    }

    @Override
    public void navigateBack(String message) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setOnDone(OnDone onDone) {
        this.onDone = onDone;
    }

    public OnDone getOnDone() {
        return this.onDone;
    }
}
