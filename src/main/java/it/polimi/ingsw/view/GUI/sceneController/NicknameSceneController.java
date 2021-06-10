package it.polimi.ingsw.view.GUI.sceneController;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;
import it.polimi.ingsw.network.messages.Message_One_Parameter_String;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;


public class NicknameSceneController{

    private boolean nickNameError = true;

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
        ok.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            errorLabel.setVisible(false);
            ok.setVisible(false);
            if(nickNameError) {
                nickname.setVisible(true);
                nicknameLabel.setVisible(true);
            }
            else {
                playerNumber.setVisible(true);
                numPlayerLabel.setVisible(true);
            }
            goNext.setVisible(true);
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
                else {
                    errorLabel.setText("INSERT A CORRECT NUM OF PLAYERS (1 - 4)");
                    nickNameError = false;
                    playerNumber.clear();
                    error();
                }
            } catch (NumberFormatException e){
                errorLabel.setText("INSERT A NUMBER");
                nickNameError = false;
                playerNumber.clear();
                error();
            }
        }
        else {
            String username = nickname.getText();
            if(!username.isBlank()){
                ClientSocket.sendMessage(new Message_One_Parameter_String(MessageType.LOGIN, GUI.getPosition(), username));
                goNext.setVisible(false);
            }
            else {
                errorLabel.setText("INSERT A VALID NICKNAME");
                nickname.clear();
                error();
            }
        }
    }

    public static void askNumPlayer(){
        SceneController.setVisible("#nickname", false);
        SceneController.setVisible("#nicknameLabel", false);
        SceneController.setVisible("#playerNumber", true);
        SceneController.setVisible("#numPlayerLabel", true);
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
        SceneController.setText("#errorLabel", "NICKNAME ALREADY CHOSEN BY ANOTHER PLAYER");
        error();
        SceneController.clearText("#nickname");
    }

    private static void error(){
        SceneController.setVisible("#nickname", false);
        SceneController.setVisible("#nicknameLabel", false);
        SceneController.setVisible("#playerNumber", false);
        SceneController.setVisible("#numPlayerLabel", false);
        SceneController.setVisible("#goNext", false);
        SceneController.setVisible("#errorLabel", true);
        SceneController.setVisible("#ok", true);
    }
}
