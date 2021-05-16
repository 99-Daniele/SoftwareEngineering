package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;

public interface View{

    void newPlayer(String nickname, int position);

    void sendMessage(Message m);

    void startGame(int numPLayer);

    void isMyTurn(boolean turn);

    void exit(String nickName);

    void quit(String nickName);

    void ok();

    void errorMessage(ErrorType errorType);

    void available_slot(ArrayList<Integer> availableSlots);

    void chosen_marble(Marble[] marbles);

    void choseWhiteConversionCard(LeaderCard[] leaderCards);
}
