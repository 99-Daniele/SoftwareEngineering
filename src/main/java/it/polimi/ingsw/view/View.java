package it.polimi.ingsw.view;

import java.io.IOException;
import java.util.ArrayList;

public interface View{

    String getNickname() throws IOException;

    void nicknameTaken() throws IOException;

    void myTurn(boolean turn) throws IOException;

    void quit(int clientID) throws IOException;

    void ok(int clientID) throws IOException;

    int available_slot(int clientID, ArrayList<Integer> availableSlots) throws IOException;
}
