package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GUI implements Observer {

    public String getNickname() {
        return null;
    }

    public void setViewID(int viewID) {

    }

    public void login(int viewID) {

    }

    public void newPlayer(String nickname, int position) {

    }

    public void allPlayerConnected(int position, int numPLayer, ArrayList<String> nickNames) {

    }

    public void startGame(int numPlayer) {

    }

    public void choseLeaderCards(ArrayList<LeaderCard> leaderCards) {

    }

    public void available_slot(ArrayList<Integer> availableSlots) {

    }

    public void chosen_marble(Marble[] marbles) {

    }

    public void choseWhiteConversionCard(LeaderCard[] leaderCards) {

    }

    public void isMyTurn(boolean turn) {

    }

    public void exit(String nickName) {

    }

    public void quit(String nickName) {

    }

    public void ok() {

    }

    public void errorMessage(ErrorType errorType) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
