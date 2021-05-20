package it.polimi.ingsw.view.CLI;


import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CLI extends View {


    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public void setViewID(int viewID) {

    }

    @Override
    public void login(int viewID) {

    }

    @Override
    public void newPlayer(String nickname, int position) {

    }

    @Override
    public void allPlayerConnected(int position, int numPLayer, ArrayList<String> nickNames) {

    }

    @Override
    public void startGame(int numPlayer) {

    }

    @Override
    public void choseLeaderCards(ArrayList<LeaderCard> leaderCards) {

    }

    @Override
    public void available_slot(ArrayList<Integer> availableSlots) {

    }

    @Override
    public void chosen_marble(Marble[] marbles) {

    }

    @Override
    public void choseWhiteConversionCard(LeaderCard[] leaderCards) {

    }

    @Override
    public void isMyTurn(boolean turn) {

    }

    @Override
    public void exit(String nickName) {

    }

    @Override
    public void quit(String nickName) {

    }

    @Override
    public void ok() {

    }

    @Override
    public void errorMessage(ErrorType errorType) {

    }
}
