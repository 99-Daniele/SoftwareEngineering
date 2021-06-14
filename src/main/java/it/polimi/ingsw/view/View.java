package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.ErrorType;

import java.util.ArrayList;

public interface View{

    String getNickname();

    void login(int viewID);

    void allPlayerConnected(int position, ArrayList<String> nickNames);

    void startGame();

    void choseLeaderCards(ArrayList <LeaderCard> leaderCards);

    void availableSlot(ArrayList<Integer> availableSlots);

    void chosenMarble(Marble[] marbles);

    void choseWhiteConversionCard(LeaderCard[] leaderCards);

    void isMyTurn(boolean turn);

    void exit(String nickName);

    void quit(String nickName);

    void ok();

    void errorMessage(ErrorType errorType);

}
