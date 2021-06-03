package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;
import it.polimi.ingsw.network.messages.Message_One_Parameter_String;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.events.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class NicknameSceneController{

    @FXML
    private TextField nickname;
    @FXML
    private Label nicknameLabel;
    @FXML
    private TextField playerNumber;
    @FXML
    private Label numPlayerLabel;
    @FXML
    private Button goNext;
    @FXML
    public void initialize() {
        if(GUI.getPosition() == 0) {
            nickname.setVisible(false);
            nicknameLabel.setVisible(true);
        }
        else{
            playerNumber.setVisible(false);
            numPlayerLabel.setVisible(false);
        }
        goNext.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                goNextButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void goNextButton() throws IOException {
        if(GUI.getPosition() == 0) {
            try {
                int num = Integer.parseInt(playerNumber.getText());
                if(num < 1 || num > 4)
                    GUI.setRoot("/fxml/nicknameScene");
                else
                    ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, GUI.getPosition(), num));
            } catch (NumberFormatException e){
                GUI.setRoot("/fxml/nicknameScene");
            }
        }
        else {
            String username = nickname.getText();
            if(username.isBlank())
                GUI.setRoot("/fxml/nicknameScene");
            else
                ClientSocket.sendMessage(new Message_One_Parameter_String(MessageType.LOGIN, GUI.getPosition(), username));
        }
    }
}
