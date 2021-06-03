package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.messages.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneController {

    private static Scene scene;

    public void setScene(Stage stage, String fxml) throws IOException {
        scene = new Scene(loadFXML(fxml));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public void changeRootPane(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
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
}
