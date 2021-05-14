package it.polimi.ingsw.view;

import it.polimi.ingsw.model.market.Marble;

import java.io.IOException;
import java.util.ArrayList;

public interface View{

    String getNickname() throws IOException;

    String nicknameTaken() throws IOException;

    void newPlayer(String nickname, int position) throws IOException;

    void pronto(int numPLayer) throws IOException;

    void myTurn(boolean turn) throws IOException;

    void quit(String nickName) throws IOException;

    void ok() throws IOException;

    int available_slot(int clientID, ArrayList<Integer> availableSlots) throws IOException;

    void chosen_marble(int clientID, ArrayList<Marble> marbles) throws IOException;
}
