package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.view.model_view.Game_View;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public interface View{

    String getNickname();

    void login(int viewID);

    void allPlayerConnected(int position, int numPLayer, ArrayList<String> nickNames);

    void startGame();

    void choseLeaderCards(ArrayList <LeaderCard> leaderCards);

    void available_slot(ArrayList<Integer> availableSlots);

    void chosen_marble(Marble[] marbles);

    void choseWhiteConversionCard(LeaderCard[] leaderCards);

    void isMyTurn(boolean turn);

    void exit(String nickName);

    void quit(String nickName);

    void ok();

    void errorMessage(ErrorType errorType);

}
