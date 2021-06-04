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
    private Label errorLabel;
    @FXML
    private Button goNext;
    @FXML
    private Button ok;
    @FXML
    public void initialize() {
        goNext.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                goNextButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void goNextButton() throws IOException {
        if(!nicknameLabel.isVisible()) {
            try {
                int num = Integer.parseInt(playerNumber.getText());
                if(num >= 1 && num <= 4) {
                    ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, GUI.getPosition(), num));
                    goNext.setVisible(false);
                }
            } catch (NumberFormatException e){
            }
        }
        else {
            String username = nickname.getText();
            if(!username.isBlank()){
                ClientSocket.sendMessage(new Message_One_Parameter_String(MessageType.LOGIN, GUI.getPosition(), username));
                goNext.setVisible(false);
            }
        }
    }

    public static void askNumPlayer(){
        SceneController.setVisible("#nickname", false);
        SceneController.setVisible("#nicknameLabel", false);
        SceneController.setVisible("#playerNumber", true);
        SceneController.setVisible("#numPlayerLabel", true);
        SceneController.setText("#numPlayerLabel", "ciao");
        SceneController.setVisible("#goNext", true);
    }

    public static void waitPlayers(){
        SceneController.setVisible("#nickname", false);
        SceneController.setVisible("#nicknameLabel", false);
        SceneController.setVisible("#playerNumber", false);
        SceneController.setVisible("#numPlayerLabel", false);
        SceneController.setVisible("#waitingLabel", true);
    }

    public static void alreadyTakenNickName(){

    }

    private static void error(){
        SceneController.setVisible("#nickname", false);
        SceneController.setVisible("#nicknameLabel", false);
        SceneController.setVisible("#playerNumber", false);
        SceneController.setVisible("#numPlayerLabel", false);
        SceneController.setVisible("#goNext", false);

    }
}
