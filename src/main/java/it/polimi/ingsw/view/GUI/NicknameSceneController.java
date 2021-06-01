package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.client.ClientSocket;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.events.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NicknameSceneController {
    @FXML
    private TextField nickname;
    @FXML
    private Button goNext;
    private List<String> nicknames;
    public NicknameSceneController(){
        nicknames=new ArrayList<>();
    }
    @FXML
    public void initialize()
    {
        goNext.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            try {
                method();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void method() throws IOException {
        GUI.setRoot("/fxml/connectionScene");
        String string=nickname.getText();

    }




}
