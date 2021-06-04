package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.parser.CardMapGUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class SceneController {

    private static Scene scene;

    public void setScene(Stage stage, SceneController sceneController, String fxml) throws IOException {
        scene = new Scene(loadFXML(sceneController, fxml));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public void changeRootPane(SceneController sceneController, String fxml) {
        try {
            scene.setRoot(loadFXML(sceneController, fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askNumPlayer(){
        Node label = scene.lookup("#nickname");
        label.setVisible(false);
        label = scene.lookup("#nicknameLabel");
        label.setVisible(false);
        label = scene.lookup("#playerNumber");
        label.setVisible(true);
        label = scene.lookup("#numPlayerLabel");
        label.setVisible(true);
    }

    private static Parent loadFXML(SceneController sceneController, String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        if(fxml.equals("/fxml/initScene"))
            fxmlLoader.setController(sceneController);
        return fxmlLoader.load();
    }
}
