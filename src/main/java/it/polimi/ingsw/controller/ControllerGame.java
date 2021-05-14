package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.games.SinglePlayerGame;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.RedMarble;
import it.polimi.ingsw.model.market.ResourceMarble;
import it.polimi.ingsw.model.market.WhiteMarble;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class ControllerGame implements Observer{

    private final Game game;
    private final ArrayList<View> views;

    public ControllerGame(int numPlayers){
        if(numPlayers==1)
            this.game=new SinglePlayerGame();
        else
            this.game = new Game(numPlayers);
        views = new ArrayList<>();
    }

    public int getNumPlayers() {
        return game.getNumOfPlayers();
    }

    /**
     *
     * @param view is the view that is added to the list of views in controllerGame and also added to the observers of game
     */
    public void addView(View view){
        views.add(view);
        game.addObservers((VirtualView) view);
    }

    public void addPlayer(String nickname, int position) throws IOException, ClassNotFoundException {
        boolean error=true;
        while (error) {
            try {
                game.createPlayer(nickname);
                for (View view: views)
                    view.newPlayer(nickname, position);
                error = false;
            } catch (AlreadyTakenNicknameException e) {
                nickname = views.get(views.size()-1).nicknameTaken();
            }
        }
    }

    public synchronized void waitPlayers() {
        if (views.size() == game.getNumOfPlayers()) {
            System.out.println("\nInizia nuova partita\n");
            notifyAll();
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void removeView(String nickName, int position){
        game.deletePlayer(nickName);
        try {
            views.remove(position);
        }catch (IndexOutOfBoundsException e){}
    }

    public void isMyTurn(int pos) throws IOException {
        if(pos!= game.getCurrentPosition())
            views.get(pos).myTurn(false);
        else
            views.get(pos).myTurn(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        /*Message m = (Message) arg;
        isMyTurn(i);
        game.nextPlayer();
         */
        Message m = (Message) arg;
        int pos = m.getClientID();
        try {
            View selectedView = views.get(pos -1);
            switch (m.getMessageType()) {
                case TURN:
                    isMyTurn(pos -1);
                    break;
                case BUY_CARD:
                    ArrayList<Integer> slots = new ArrayList<>(3);
                    slots.add(0);
                    slots.add(1);
                    slots.add(2);
                    selectedView.available_slot(pos, slots);
                    break;
                case TAKE_MARBLE:
                    ArrayList<Marble> marbles = new ArrayList<>(4);
                    marbles.add(new RedMarble());
                    marbles.add(new WhiteMarble());
                    marbles.add(new ResourceMarble(Resource.COIN));
                    marbles.add(new WhiteMarble());
                    for (int i = 0; i < 3; i++) {
                        selectedView.chosen_marble(pos, marbles);
                    }
                    selectedView.ok();
                    break;
                case END_TURN:
                    selectedView.ok();
                    game.nextPlayer();
                    break;
                default:
                    System.err.println("DEFAULT");
                    selectedView.ok();
                    break;
            }
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void quitGame(String nickName, int position) throws IOException {
        removeView(nickName, position -1);
        for(View view: views)
            view.quit(nickName);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nPartita finita\n");
    }

    public void endGame() throws IOException {
        game.endGame();
        System.out.println("\nPartita finita\n");
    }


    private void nextTurn()
    {
        game.nextPlayer();
    }

   /* public int getCurrentTurn(){
        return game.getCurrentPosition();
    }*/

    public Message inGame(Message message) {
        switch(message.getMessageType()) {
            case BUY_CARD:
                return buyCardHandler(message);
            case TAKE_MARBLE:
                return takeMarbleHandler(message);
            case USE_MARBLE:
                return useMarbleHandler(message);
            case SWITCH_DEPOT:
                return switchHandler(message);
            case CHOSEN_SLOT:
                return chosenSlotHandler(message);
            case WHITE_CONVERSION_CARD:
                return whiteConversionCardHandler(message);
            case LEADER_CARD_POWER:
                return leaderCardPowerHandler(message);
            case LEADER_CARD_DISCARD:
                return leaderDiscardHandler(message);
            case LEADER_CARD_ACTIVATION:
                return leaderActivationHandler(message);
            case DEVELOPMENT_CARD_POWER:
            case BASIC_POWER:
            case END_PRODUCTION:
            case LOGIN:
            case END_GAME:
            case DECKBOARD:
            case NEW_PLAYER:
            case LEADER_CARD:
            case MARKET:
            case START_GAME:
            case PING:
            default:
                return new Message_One_Parameter_String(MessageType.ERR,message.getClientID(),"Selected action doesn't exist");
        }
    }

    public Message buyCardHandler(Message message) {
        ArrayList<Integer> arrayList;
        Message_Three_Parameter_Int message_three_parameter_int=(Message_Three_Parameter_Int) message;
        try {
            arrayList=game.findAvailableSlots(message_three_parameter_int.getPar1(), message_three_parameter_int.getPar2());
        } catch (EmptyDevelopmentCardDeckException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_three_parameter_int.getClientID(),"Selected empty deck");
        }
        if (arrayList.isEmpty())
            return new Message_One_Parameter_String(MessageType.ERR,message_three_parameter_int.getClientID(),"Not available free slot");
        if(arrayList.size()==1)
            /* manca la gestione dei slot che viene scelto dall'utente*/
        try {
            game.buyDevelopmentCardFromMarket(message_three_parameter_int.getPar1(), message_three_parameter_int.getPar2(), message_three_parameter_int.getPar3(),arrayList.get(0));
        } catch (InsufficientResourceException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_three_parameter_int.getClientID(),"Not enough resources");
        } catch (ImpossibleDevelopmentCardAdditionException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_three_parameter_int.getClientID(),"Not available free slot");
        } catch (EmptyDevelopmentCardDeckException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_three_parameter_int.getClientID(),"Selected empty deck");
        }
        return new Message_One_Parameter_String(MessageType.OK, message_three_parameter_int.getClientID(), "success");
    }


    public Message chosenSlotHandler(Message message) {
        Message_One_Parameter_Int message_one_parameter_int=(Message_One_Parameter_Int) message;
        /*gestione dello slot scelto dall'utente
        message_one_parameter_int.getPar();
         */
        return new Message_One_Parameter_String(MessageType.ERR,message_one_parameter_int.getClientID(),"Success");
    }


    public Message takeMarbleHandler(Message message) {
        Message_Two_Parameter_Int message_two_parameter_int=(Message_Two_Parameter_Int)message;
        boolean choice= message_two_parameter_int.getPar1()==0 ? true : false;
        try {
            game.takeMarketMarble(choice,message_two_parameter_int.getPar2());
        } catch (WrongParametersException e) {
            return new Message_One_Parameter_String(MessageType.ERR, message_two_parameter_int.getClientID(), "Wrong parameter");
        }
        return new Message_One_Parameter_String(MessageType.OK,message_two_parameter_int.getClientID(),"Success");
    }

    public Message useMarbleHandler(Message message) {
        Message_One_Parameter_Marble message_one_parameter_marble=(Message_One_Parameter_Marble) message;
        message_one_parameter_marble.getMarble().useMarble(game);
        return new Message_One_Parameter_String(MessageType.OK,message_one_parameter_marble.getClientID(),"success");
    }

    /* richiede l'identificativo leaderCard*/
    public Message whiteConversionCardHandler(Message message) {
        Message_One_Parameter_Int message_one_parameter_int=(Message_One_Parameter_Int) message;
        /*
        game.whiteMarbleConversion();
        */
        return new Message_One_Parameter_String(MessageType.ERR,message_one_parameter_int.getClientID(),"Success");
    }


    public Message switchHandler(Message message) {
        Message_Two_Parameter_Int message_two_parameter_int=(Message_Two_Parameter_Int)message;
        try {
            game.switchDepots(message_two_parameter_int.getPar1(),message_two_parameter_int.getPar2());
        } catch (ImpossibleSwitchDepotException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_two_parameter_int.getClientID(),"Switch not possible");
        }
        return new Message_One_Parameter_String(MessageType.OK,message_two_parameter_int.getClientID(),"success");
    }

/* richiedono lo strongbox
    public Message developmentCardPowerHandler(Message_Two_Parameter_Int message_two_parameter_int)
    {
        PlayerBoard playerBoard=game.getPlayer(message_two_parameter_int.getClientID());
        playerBoard.activateDevelopmentCardProductionPower(message_two_parameter_int.getPar1(),);
    }

    public Message basicPowerHandler(Message_Three_Resource_One_Int message_three_resource_one_int){
        PlayerBoard playerBoard= game.getPlayer(message_three_resource_one_int.getClientID());
        game.basicProductionPower(message_three_resource_one_int.getResource1(),message_three_resource_one_int.getResource2(),message_three_resource_one_int.getResource3(),4,message_three_resource_one_int.getPar());
    }
*/

    public Message leaderCardPowerHandler(Message message) {
        Message_One_Resource_Two_Int message_one_resource_two_int=(Message_One_Resource_Two_Int) message;
        PlayerBoard playerBoard= game.getPlayer(message_one_resource_two_int.getClientID());
        try {
            playerBoard.activateAdditionalProductionPower(message_one_resource_two_int.getPar1(), message_one_resource_two_int.getPar2());
        } catch (NoSuchProductionPowerException e) {
            return new Message_One_Parameter_String(MessageType.ERR, message_one_resource_two_int.getClientID(), "Slot empty");
        } catch (InsufficientResourceException e) {
            return new Message_One_Parameter_String(MessageType.ERR, message_one_resource_two_int.getClientID(), "Not enough resources");
        }
        return new Message_One_Parameter_String(MessageType.OK, message_one_resource_two_int.getClientID(), "Success");
    }


    public Message leaderActivationHandler(Message message) {
        Message_One_Parameter_Int message_one_parameter_int=(Message_One_Parameter_Int)message;
        try {
            game.activateLeaderCard(message_one_parameter_int.getPar());
        } catch (InsufficientResourceException e) {
            e.printStackTrace();
        } catch (AlreadyDiscardLeaderCardException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_one_parameter_int.getClientID(),"LeaderCard has been discarded already");
        } catch (ActiveLeaderCardException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_one_parameter_int.getClientID(),"LeaderCard already active");
        } catch (InsufficientCardsException e) {
            return new Message_One_Parameter_String(MessageType.ERR,message_one_parameter_int.getClientID(),"Not enough card");
        }
        return new Message_One_Parameter_String(MessageType.OK, message_one_parameter_int.getClientID(), "Success");
    }

    public Message leaderDiscardHandler(Message message) {
        Message_One_Parameter_Int message_one_parameter_int=(Message_One_Parameter_Int)message;
        try {
            game.discardLeaderCard(message_one_parameter_int.getPar());
        } catch (ActiveLeaderCardException e) {
            return new Message_One_Parameter_String(MessageType.ERR, message_one_parameter_int.getClientID(),"LeaderCard already active");
        } catch (AlreadyDiscardLeaderCardException e) {
            return new Message_One_Parameter_String(MessageType.ERR, message_one_parameter_int.getClientID(),"LeaderCard already discarded");
        }
        return new Message_One_Parameter_String(MessageType.OK, message_one_parameter_int.getClientID(), "Success");
    }
}

