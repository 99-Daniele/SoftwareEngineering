package it.polimi.ingsw.view.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class GUI extends Application implements Observer {


    @Override
    public void update(Observable o, Object arg) {

    }

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/fxml/opponentPlayerboardScene"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }


    private static Parent loadFXML(String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch();
    }
}
