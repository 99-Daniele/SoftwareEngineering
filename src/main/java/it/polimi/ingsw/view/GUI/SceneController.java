package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.parser.CardMapGUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class SceneController {

    private static Scene scene;

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Stage stage,  String fxml) throws IOException {
        scene = new Scene(loadFXML(fxml));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void changeRootPane(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeRootPane(SceneController sceneController, String fxml) {
        try {
            scene.setRoot(loadFXML(sceneController, fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader.load();
    }

    private static Parent loadFXML(SceneController sceneController, String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setController(sceneController);
        return fxmlLoader.load();
    }

    public static void setVisible(String selector, boolean visible){
        Node label = scene.lookup(selector);
        label.setVisible(visible);
    }

    public static void setDisable(String selector, boolean visible){
        Node label = scene.lookup(selector);
        label.setDisable(visible);
    }

    public static void setText(String selector, String text){
        Label label = (Label) scene.lookup(selector);
        label.setText(text);
    }

    public static void setImage(String selector, String file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ImageView imageView = (ImageView) scene.lookup(selector);
        imageView.setImage(new Image(fis));
    }
}
