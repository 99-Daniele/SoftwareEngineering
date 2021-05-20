package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.view.model_view.Game_View;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class View extends Observable implements Observer {

    private Game_View game_view;
    private Thread inThread;
    private BufferedReader stdIn;
    private final Object okLock = new Object();

    public abstract String getNickname();

    public abstract void setViewID(int viewID);

    public abstract void login(int viewID);

    public abstract void newPlayer(String nickname, int position);

    public abstract void allPlayerConnected(int position, int numPLayer, ArrayList<String> nickNames);

    public abstract void startGame(int numPlayer);

    public abstract void choseLeaderCards(ArrayList <LeaderCard> leaderCards);

    public abstract void available_slot(ArrayList<Integer> availableSlots);

    public abstract void chosen_marble(Marble[] marbles);

    public abstract void choseWhiteConversionCard(LeaderCard[] leaderCards);

    public abstract void isMyTurn(boolean turn);

    public abstract void exit(String nickName);

    public abstract void quit(String nickName);

    public abstract void ok();

    public abstract void errorMessage(ErrorType errorType);

    @Override
    public void update(Observable o, Object arg) {

    }
}
