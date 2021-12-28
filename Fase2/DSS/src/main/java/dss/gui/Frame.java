package dss.gui;

import dss.SGR;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class Frame implements Initializable {
    @FXML
    private MenuBar topMenu;
    @FXML
    private Label loggedInAs;
    @FXML
    private AnchorPane content;
    @FXML
    private HBox message;
    @FXML
    private Label messageText;
    @FXML
    private Button exitButton;
    @FXML
    private Button backButton;

    private FXMLLoader frameLoader;
    private SGR sgr;
    private Stack<Node> navigationStack;

    public Frame(SGR sgr) {
        this.sgr = sgr;
        this.navigationStack = new Stack<>();
        frameLoader = new FXMLLoader(getClass().getResource("/dss/gui/Frame.fxml"));
        frameLoader.setController(this);
    }

    public Scene getScene() throws IOException {
        return new Scene(frameLoader.load());
    }

    private void setContent(Node content) {
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 10.0);
        AnchorPane.setLeftAnchor(content, 10.0);
        AnchorPane.setRightAnchor(content, 10.0);
        this.content.getChildren().clear();
        this.content.getChildren().add(content);
    }

    public void navigateTo(Node content) {
        // TODO: Inserir botão de retrocesso
        navigationStack.push(content);
        closeMessage();
        setContent(content);

        if (this.navigationStack.size() >= 2) {
            this.exitButton.setVisible(true);
            if (navigationStack.size() >= 3) {
                this.backButton.setVisible(true);
            }
        }
    }

    public void navigateBack() {
        navigationStack.pop();
        setContent(navigationStack.peek());

        if (navigationStack.size() == 1) {
            this.exitButton.setVisible(false);
        }
        if (navigationStack.size() <= 2) {
            this.backButton.setVisible(false);
        }
    }

    public void navigateBack(String message) {
        messageText.setText(message);
        this.message.setMaxHeight(Region.USE_COMPUTED_SIZE);
        this.message.setVisible(true);
        navigateBack();
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initialize");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dss/gui/LogIn.fxml"));
        LogIn controller = new LogIn(sgr, this);
        loader.setController(controller);

        loggedInAs.setText("Utilizador não autenticado");
        this.message.managedProperty().bindBidirectional(this.message.visibleProperty());
        this.backButton.managedProperty().bindBidirectional(this.backButton.visibleProperty());
        this.exitButton.managedProperty().bindBidirectional(this.exitButton.visibleProperty());

        this.exitButton.setVisible(false);
        this.backButton.setVisible(false);

        try {
            navigateTo(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void login() {
        loggedInAs.setText("Autenticado como: " + sgr.getUtilizadorAutenticado().getNome());
        MainMenu mainMenu = new MainMenu(sgr, this);

        this.exitButton.setVisible(true);
        navigateTo(mainMenu.getScene());
    }

    @FXML
    public void onBack() {
        navigateBack();
    }
    @FXML
    public void onExit() {
        while (navigationStack.size() != 1)
            navigateBack("Sessão terminada");
    }
    @FXML
    public void onOpen() {}
    @FXML
    public void onSave() {}
    @FXML
    public void closeMessage() {
        this.message.setVisible(false);
        this.message.setMaxHeight(0);
    }
}
