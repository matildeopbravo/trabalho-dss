package dss.gui;

import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.Future;

public interface Navigator {
    // Esta interface gere a navegação entre dois ecrãs
    void navigateTo(Node node);
    void navigateBack();
    void navigateBack(String message);

    Stage getStage();

    default <T> void openPopup(PopUp<T> popUp) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());

        popUp.setStage(dialog);

        dialog.setScene(popUp.getScene());
        dialog.show();
    }
}
