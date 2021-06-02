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
    private int position=0;
    @FXML
    private TextField nickname;
    @FXML
    private Button goNext;
    @FXML
    private Label numPlayerLabel;
    @FXML
    private TextField playerNumber;
    @FXML
    private Label nicknameLabel;
    @FXML
    public void initialize()
    {
        if(position==0)
        {
            numPlayerLabel.setVisible(true);
            playerNumber.setVisible(true);
        }
        goNext.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            try {
                goNextButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void goNextButton() throws IOException {
        String string=nickname.getText();
        int num;
        if(position==0)
        {
            nicknameLabel.setVisible(false);
            nickname.setVisible(false);
            num= Integer.parseInt(playerNumber.getText());
            ClientSocket.sendMessage( new Message_One_Parameter_Int(MessageType.NUM_PLAYERS,position,num));
            GUI.setRoot("/fxml/nicknameScene");
        }
        else
        {
            ClientSocket.sendMessage(new Message_One_Parameter_String(MessageType.LOGIN, position, string));
            GUI.setRoot("/fxml/initScene");
        }
    }


}
