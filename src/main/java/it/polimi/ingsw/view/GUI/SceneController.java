package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.GUI.sceneController.OpponentPlayerboardSceneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Objects;

/**
 * SceneController handled the switch to other scene and the modifies of current scene nodes.
 */
public class SceneController {

    private static Scene scene;

    /**
     * @param selector is one of chosen scene node
     * @return if is the right scene.
     *
     * this method try to lookup @param selector and return if is the right scene.
     */
    public static boolean isCurrentScene(String selector){
        try {
            Node node = scene.lookup(selector);
            node.isDisable();
            return true;
        } catch (NullPointerException e){
            return false;
        }
    }

    /**
     * @param stage is current stage.
     * @param fxml identifies fxml files for new scene.
     * @throws IOException if fxml loading file fails.
     */
    public static void setScene(Stage stage,  String fxml) throws IOException {
        StackPane pane = new StackPane(loadFXML(fxml));
        scene = new Scene(pane, Double.MAX_VALUE, Double.MAX_VALUE);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param fxml identifies fxml files for new scene.
     *
     * switch to a new scene.
     */
    public static void changeRootPane(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param opsc is one OpponentPlayerboardSceneController.
     * @param fxml identifies fxml files for new scene.
     *
     * switch to a new scene.
     */
    public static void changeRootPane(OpponentPlayerboardSceneController opsc, String fxml){
        try {
            scene.setRoot(loadFXML(opsc, fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fxml refers to one fxml file.
     * @return the parent of loaded fxml file.
     * @throws IOException if fxml loading file fails.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader.load();
    }

    /**
     * @param opsc is one OpponentPlayerboardSceneController.
     * @param fxml refers to one fxml file.
     * @return the parent of loaded fxml file.
     * @throws IOException if fxml loading file fails.
     *
     * load fxml file and set @param opsc as Controller.
     */
    private static Parent loadFXML(OpponentPlayerboardSceneController opsc, String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setController(opsc);
        return fxmlLoader.load();
    }

    /**
     * @param selector is one scene node.
     * @param visible refers to if @param selector is visible or not.
     *
     * set @param selector to visible or invisible due to @param visible.
     */
    public static void setVisible(String selector, boolean visible){
        Node label = scene.lookup(selector);
        label.setVisible(visible);
    }

    /**
     * @param selector is one scene node.
     * @param disable refers to if @param selector is visible or not.
     *
     * set @param selector to disable or enable due to @param visible.
     */
    public static void setDisable(String selector, boolean disable){
        Node label = scene.lookup(selector);
        label.setDisable(disable);
    }

    /**
     * @param selector is one scene node.
     * @param text is the text which is going to be set to @param selector.
     */
    public static void setText(String selector, String text){
        Label label = (Label) scene.lookup(selector);
        label.setText(text);
    }

    /**
     * @param selector is one scene node.
     *
     * clear @param selector text.
     */
    public static void clearText(String selector){
        TextField textField = (TextField) scene.lookup(selector);
        textField.clear();
    }

    /**
     * @param selector is one scene node.
     * @param file refers to file image.
     *
     * if @param file equals "" set @param selector to null, otherwise set to the selected image.
     */
    public static void setImage(String selector, String file) {
        ImageView imageView = (ImageView) scene.lookup(selector);
        if(file.equals(""))
            imageView.setImage(null);
        else {
            InputStream fis = SceneController.class.getResourceAsStream(file);
            imageView.setImage(new Image(Objects.requireNonNull(fis)));
        }
    }

    /**
     * @param selector is one scene node.
     * @param x is the x coordinate which is going to be set @param selector.
     */
    public static void setLayoutX(String selector, int x){
        Node node = scene.lookup(selector);
        node.setLayoutX(x);
    }

    /**
     * @param selector is one scene node.
     * @param y is the y coordinate which is going to be set @param selector.
     */
    public static void setLayoutY(String selector, int y){
        Node node = scene.lookup(selector);
        node.setLayoutY(y);
    }

    /**
     * @param selector is one scene node.
     * @param opacity refers to @param selector opacity.
     *
     * set @param selector opacity.
     */
    public static void setOpacity(String selector, int opacity){
        ImageView imageView = (ImageView) scene.lookup(selector);
        imageView.setOpacity(opacity);
    }

    /**
     * @param message is one message received from Server.
     *
     * add @param message to YourTurnSceneController messages ScrollPane.
     */
    public static void addMessage(String message){
        ScrollPane messages = (ScrollPane) scene.lookup("#messages");
        VBox vbox = (VBox) messages.getContent();
        vbox.getChildren().add(0, new Label(message));
    }

    /**
     * @param error is one error message received from Server.
     *
     * display @param error to YourTurnSceneController errorPane.
     */
    public static void errorMessage(String error){
        Pane pane = (Pane) scene.lookup("#errorPane");
        Label label = (Label) pane.getChildren().get(0);
        pane.setVisible(true);
        label.setText(error);
    }
}
