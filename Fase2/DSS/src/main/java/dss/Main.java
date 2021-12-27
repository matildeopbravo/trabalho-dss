package dss;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    // Estamos a usar JavaFX, portanto este é o método principal da aplicação
    @Override
    public void start(Stage stage) throws Exception {
        SGR sgr = new SGR();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dss/gui/Main.fxml"));
        dss.gui.Main controller = new dss.gui.Main();
        controller.setSGR(sgr);
        loader.setController(controller);
        Scene mainScene = new Scene(loader.load());
        System.out.println("SGR LOADED");
        stage.setScene(mainScene);
        stage.setTitle("Sistema de Gestão de Reparações");
        stage.show();
    }
}
