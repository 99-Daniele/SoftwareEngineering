package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.parser.CardMapGUI;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.model_view.Game_View;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;


public class YourTurnSceneController extends SceneController{
    private int pos=0;
    private GAME_STATES currentState;
    private int position=1;
    private final Game_View game=new Game_View();
    @FXML
    private Button chooseSlot1;
    @FXML
    private RadioButton radiobutBuyCard;
    @FXML
    private RadioButton row1;
    @FXML
    private RadioButton row2;
    @FXML
    private RadioButton row3;
    @FXML
    private RadioButton column1;
    @FXML
    private RadioButton column2;
    @FXML
    private RadioButton column3;
    @FXML
    private RadioButton column4;
    @FXML
    private RadioButton radiobutTakeMarble;
    @FXML
    private RadioButton radiobutActivProduc;
    @FXML
    private RadioButton radiobutActLeader;
    @FXML
    private RadioButton radiobutDiscardLeader;
    @FXML
    private RadioButton radiobutOtherPlayboard;
    @FXML
    private RadioButton radiobutEndTurn;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView card13;
    @FXML
    private ImageView card21;
    @FXML
    private ImageView card22;
    @FXML
    private ImageView card23;
    @FXML
    private ImageView card31;
    @FXML
    private ImageView card32;
    @FXML
    private ImageView card33;
    @FXML
    private ImageView card41;
    @FXML
    private ImageView card42;
    @FXML
    private ImageView card43;
    @FXML
    private ImageView marbleShow1;
    @FXML
    private ImageView marbleShow2;
    @FXML
    private ImageView marbleShow3;
    @FXML
    private ImageView marbleShow4;
    @FXML
    private Button chooseLeader1;
    @FXML
    private Button chooseLeader2;
    @FXML
    private Button chooseSlot2;
    @FXML
    private Button chooseSlot3;
    @FXML
    private Button chooseBase;
    @FXML
    private Label coinAmount;
    @FXML
    private Label stoneAmount;
    @FXML
    private Label servantAmount;
    @FXML
    private Label shieldAmount;
    @FXML
    private Label croceRossa;
    @FXML
    private Button move;
    @FXML
    private Label message;
    @FXML
    private Button yes;
    @FXML
    private Button no;
    @FXML
    private Button ok;
    @FXML
    private Label chat;
    @FXML
    private RadioButton radiobutEndProd;
    @FXML
    private RadioButton switch1;
    @FXML
    private RadioButton switch2;
    @FXML
    private RadioButton switch3;
    private int marbleCount;
    @FXML
    public void initialize(){
        shieldAmount.setText("0");
        coinAmount.setText("0");
        servantAmount.setText("0");
        stoneAmount.setText("0");
        radiobutTakeMarble.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> TakeMarbleButton());
        radiobutBuyCard.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> buyCardButton());
        radiobutActivProduc.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> activProducButton());
        radiobutActLeader.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> actLeaderButton());
        radiobutDiscardLeader.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> discardLeaderButton());
        radiobutOtherPlayboard.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> otherPlayerboardButton());
        move.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> moveCroce());
        row1.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> take_market_marble_row(1));
        row2.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> take_market_marble_row(2));
        row3.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> take_market_marble_row(3));
        column1.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> take_market_marble_column(1));
        column2.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> take_market_marble_column(2));
        column3.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> take_market_marble_column(3));
        column4.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> take_market_marble_column(4));
    }


    private void chooseSwitch(Message messagee){
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) messagee;
        //ClientView.take_marble_message(message);
        System.out.println("You have chosen this marbles: ");
        message.setVisible(true);
        message.setText("Wanna switch your deposit?");
        yes.setVisible(true);
        no.setVisible(true);
        //show marbles
        yes.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> yesSwitch());
        no.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> noSwitch());
    }

    private void noSwitch(){
        marbleCount--;
        if(marbleCount>0)
            showReducedMarble();

    }

    private void showReducedMarble(){

    }

    private void yesSwitch()
    {
        switch1.setDisable(false);
        switch2.setDisable(false);
        switch3.setDisable(false);
        switch1.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            switch1.setDisable(true);
            clickedSwitch();
        });
        switch2.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            switch2.setDisable(true);
            clickedSwitch();
        });
        switch3.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> {
            switch3.setDisable(true);
            clickedSwitch();
        });
    }

    private void clickedSwitch(){
        int par1=0;
        int par2=0;
        if(switch1.isDisable() && switch2.isDisable())
        {
            par1=1;
            par2=2;
        }
        if(switch1.isDisable() && switch3.isDisable()){
            par1=1;
            par2=3;
        }
        if(switch2.isDisable() && switch3.isDisable()){
            par1=2;
            par2=3;
        }
        if (par1!=0 && par2!=0) {
            try {
                ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, GUI.getPosition(), 1, 2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void take_market_marble_row(int i)  {
        marbleCount=4;
        try {
            ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, GUI.getPosition(), 0, i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void take_market_marble_column(int i)  {
        marbleCount=3;
        try {
            ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, 1, i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void otherPlayerboardButton()  {
        SceneController opsc = new OpponentPlayerboardSceneController();
        GUI.setRoot(opsc, "/fxml/opponentPlayerboardScene");
    }

    private void discardLeaderButton(){
        disableAllButton();
        chooseLeader1.setDisable(false);
        chooseLeader2.setDisable(false);
    }

    private void activProducButton(){
        disableAllButton();
        chooseSlot1.setDisable(false);
        chooseSlot2.setDisable(false);
        chooseSlot3.setDisable(false);
        chooseBase.setDisable(false);
    }

    private void actLeaderButton(){
        disableAllButton();
        chooseLeader1.setDisable(false);
        chooseLeader2.setDisable(false);
    }


    private void TakeMarbleButton(){

        disableAllButton();
        row1.setDisable(false);
        row2.setDisable(false);
        row3.setDisable(false);
        column1.setDisable(false);
        column2.setDisable(false);
        column3.setDisable(false);
        column4.setDisable(false);

    }

    private void buyCardButton(){
        disableAllButton();
        card11.setDisable(false);
        card12.setDisable(false);
        card13.setDisable(false);
        card21.setDisable(false);
        card22.setDisable(false);
        card23.setDisable(false);
        card31.setDisable(false);
        card32.setDisable(false);
        card33.setDisable(false);
        card41.setDisable(false);
        card42.setDisable(false);
        card43.setDisable(false);
    }


    private void disableAllButton(){
        radiobutEndProd.setDisable(true);
        radiobutBuyCard.setDisable(true);
        radiobutActLeader.setDisable(true);
        radiobutActivProduc.setDisable(true);
        radiobutDiscardLeader.setDisable(true);
        radiobutOtherPlayboard.setDisable(true);
        radiobutTakeMarble.setDisable(true);
        radiobutEndTurn.setDisable(true);
    }

    private void moveCroce(){
        pos++;
        if(pos>2 && pos<5 || pos>16 && pos<19)
            croceRossa.setLayoutY(croceRossa.getLayoutY()-28);
        else if (pos>9 && pos<12)
            croceRossa.setLayoutY(croceRossa.getLayoutY()+28);
        else croceRossa.setLayoutX(croceRossa.getLayoutX()+30);
    }
/*
    public void update() {
        Task<Void> task = new Task<Void>() {
            String a = "Initial text";

            @Override
            public Void call() throws Exception {
                int i = 0;

                while (true) {

                    if (i > 4)
                        a = "I is bigger than 4";

                    if (i > 10)
                        a = "I is bigger than 10";

                    Platform.runLater(() -> {
                        testo.setText(a);
                        // If you want to you can also move the text here
                        testo.relocate(10, 10);
                    });

                    i++;
                    Thread.sleep(1000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

 */

}
