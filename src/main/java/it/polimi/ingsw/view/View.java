package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.ErrorType;

import java.util.ArrayList;

public interface View{

    String getNickname();

    void setViewID(int viewID);

    void login(int viewID);

    void newPlayer(String nickname, int position);

    void allPlayerConnected(int position, int numPLayer, ArrayList<String> nickNames);

    void startGame(int numPlayer);

    void choseLeaderCards(ArrayList <LeaderCard> leaderCards);

    void isMyTurn(boolean turn);

    void exit(String nickName);

    void quit(String nickName);

    void ok();

    void errorMessage(ErrorType errorType);

    void available_slot(ArrayList<Integer> availableSlots);

    void chosen_marble(Marble[] marbles);

    void choseWhiteConversionCard(LeaderCard[] leaderCards);

    @Override
    String toString();
}
