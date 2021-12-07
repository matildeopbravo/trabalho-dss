package dss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    // Estamos a usar JavaFX, portanto este é o método principal da aplicação
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dss/gui/Main.fxml"));
        Scene mainScene = new Scene(loader.load());

        stage.setScene(mainScene);
        stage.setTitle("Sistema de Gestão de Reparações");

        stage.show();
    }
}
