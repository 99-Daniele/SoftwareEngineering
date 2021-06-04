package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;
import it.polimi.ingsw.network.messages.Message_One_Parameter_String;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;


public class NicknameSceneController extends SceneController {

    private String name;

    @FXML
    private TextField nickname;
    @FXML
    private Label nicknameLabel;
    @FXML
    private TextField playerNumber;
    @FXML
    private Label numPlayerLabel;
    @FXML
    private Label waitingLabel;
    @FXML
    private Button goNext;
    @FXML
    public void initialize() {
        if(name != null) {
            name = null;
            nickname.setVisible(false);
            nicknameLabel.setVisible(false);
            playerNumber.setVisible(true);
            numPlayerLabel.setVisible(true);
        }
        goNext.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                goNextButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    @Override
    public void askNumPlayer(){
        nickname.setVisible(false);
        nicknameLabel.setVisible(false);
        playerNumber.setVisible(true);
        numPlayerLabel.setVisible(true);
    }

    private void goNextButton() throws IOException {
        if(nicknameLabel.isDisabled()) {
            try {
                int num = Integer.parseInt(playerNumber.getText());
                if(num >= 1 && num <= 4)
                    ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, GUI.getPosition(), num));
            } catch (NumberFormatException e){
            }
        }
        else {
            String username = nickname.getText();
            if(!username.isBlank()){
                name = username;
                ClientSocket.sendMessage(new Message_One_Parameter_String(MessageType.LOGIN, GUI.getPosition(), username));
            }
        }
    }
}
