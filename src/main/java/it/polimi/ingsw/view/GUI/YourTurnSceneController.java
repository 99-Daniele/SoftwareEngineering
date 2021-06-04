package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.model.resourceContainers.Resource;
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
    @FXML
    private Pane panelBuy;
    @FXML
    private RadioButton radiobutWarehouse;
    @FXML
    private RadioButton radiobutStrongbox;
    @FXML
    private ImageView slot11;
    @FXML
    private ImageView slot12;
    @FXML
    private ImageView slot13;
    @FXML
    private ImageView slot21;
    @FXML
    private ImageView slot22;
    @FXML
    private ImageView slot23;
    @FXML
    private ImageView slot31;
    @FXML
    private ImageView slot32;
    @FXML
    private ImageView slot33;
    @FXML
    private Pane basePanel;
    @FXML
    private ImageView servant1;
    @FXML
    private ImageView stone1;
    @FXML
    private ImageView coin1;
    @FXML
    private ImageView shield1;
    @FXML
    private Label baseLabel;

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
        chooseSlot1.setOnMouseClicked(mouseEvent -> {
            chooseSlot1.setDisable(true);
            cardProduction(1);
        });
        chooseSlot2.setOnMouseClicked(mouseEvent -> {
            chooseSlot2.setDisable(true);
            cardProduction(2);
        });
        chooseSlot3.setOnMouseClicked(mouseEvent -> {
            chooseSlot3.setDisable(true);
            cardProduction(3);
        });
        chooseBase.setOnMouseClicked(mouseEvent -> {
            basePanel.setVisible(true);
            Message_Three_Resource_One_Int messageToSend=new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, null, null, null, 0);
            coin1.setOnMouseClicked(MouseEvent->{
                baseProduction(Resource.COIN,messageToSend);
            });

            servant1.setOnMouseClicked(MouseEvent->{
                baseProduction(Resource.SERVANT,messageToSend);
            });

            shield1.setOnMouseClicked(MouseEvent->{
                baseProduction(Resource.SHIELD,messageToSend);
            });

            stone1.setOnMouseClicked(MouseEvent->{
                baseProduction(Resource.STONE,messageToSend);
            });
        });
    }

    private void baseProduction(Resource resource, Message_Three_Resource_One_Int messageToSend){
            if(messageToSend.getResource1()==null)
                messageToSend.setResource1(resource);
            else
            if (messageToSend.getResource2()==null)
            {
                messageToSend.setResource2(resource);
                baseLabel.setText("Choose produced resource");
            }
            else{
                basePanel.setVisible(false);
                baseLabel.setText("Choose resources required");
                messageToSend.setResource3(resource);
                panelBuy.setVisible(true);
                radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
                    panelBuy.setVisible(false);
                    messageToSend.setPar(0);
                    try {
                        ClientSocket.sendMessage(messageToSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
                    panelBuy.setVisible(false);
                    messageToSend.setPar(1);
                    try {
                        ClientSocket.sendMessage(messageToSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
    }

    private void cardProduction(int slot){
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, slot, 0);
            try {
              ClientSocket.sendMessage(message);
             } catch (IOException e) {
               e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, slot, 1);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private void buyCardButton(){
        disableAllButton();
        setDisableAllDecks(false);
        card11.setOnMouseClicked(mouseEvent -> deckEvent(1,1));
        card12.setOnMouseClicked(mouseEvent -> deckEvent(1,2));
        card13.setOnMouseClicked(mouseEvent -> deckEvent(1,3));
        card21.setOnMouseClicked(mouseEvent -> deckEvent(2,1));
        card22.setOnMouseClicked(mouseEvent -> deckEvent(2,2));
        card23.setOnMouseClicked(mouseEvent -> deckEvent(2,2));
        card31.setOnMouseClicked(mouseEvent -> deckEvent(3,1));
        card32.setOnMouseClicked(mouseEvent -> deckEvent(3,1));
        card33.setOnMouseClicked(mouseEvent -> deckEvent(3,1));
        card41.setOnMouseClicked(mouseEvent -> deckEvent(4,1));
        card42.setOnMouseClicked(mouseEvent -> deckEvent(4,2));
        card43.setOnMouseClicked(mouseEvent -> deckEvent(4,3));
    }
    /*
    card11.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(1,1));
        card12.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(1,2));
        card13.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(1,3));
        card21.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(2,1));
        card22.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(2,2));
        card23.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(2,2));
        card31.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(3,1));
        card32.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(3,1));
        card33.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(3,1));
        card41.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(4,1));
        card42.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(4,2));
        card43.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> deckEvent(4,3));
     */

    private void deckEvent(int column, int row)
    {
        setDisableAllDecks(true);
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, row, column, 0);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, row, column, 1);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setDisableAllDecks(boolean b)
    {
        card11.setDisable(b);
        card12.setDisable(b);
        card13.setDisable(b);
        card21.setDisable(b);
        card22.setDisable(b);
        card23.setDisable(b);
        card31.setDisable(b);
        card32.setDisable(b);
        card33.setDisable(b);
        card41.setDisable(b);
        card42.setDisable(b);
        card43.setDisable(b);
    }


    public void buy_card_message(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        super.buy_card_message(message);
        if(m.getClientID() != position)
            System.out.println("Player " + super.getNickname(m.getClientID()) + " bought a new card and inserted it in " +
                    "the " + m.getPar2() + "° slot.");//metetre nel quadrato gui laterale
        else
            switch (m.getPar2()){
                case 1:
                    if(slot11.getImage()==null)
                        slot11.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    else
                    if (slot12.getImage()==null)
                        slot12.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    else
                        slot13.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    break;
                case 2:
                    if(slot21.getImage()==null)
                        slot21.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    else
                    if (slot22.getImage()==null)
                        slot22.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    else
                        slot23.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    break;
                case 3:
                    if(slot31.getImage()==null)
                        slot31.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    else
                    if (slot32.getImage()==null)
                        slot32.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    else
                        slot33.setImage(new Image(CardMapGUI.getCard(m.getPar1())));
                    break;
            }
        radiobutActLeader.setDisable(false);
        radiobutDiscardLeader.setDisable(false);
        radiobutOtherPlayboard.setDisable(false);
        radiobutEndTurn.setDisable(false);
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
