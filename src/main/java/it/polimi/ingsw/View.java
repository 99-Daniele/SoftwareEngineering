package it.polimi.ingsw;

import java.io.IOException;

public interface View{

    String getNickname() throws IOException;

    void nicknameTaken() throws IOException;

    void myTurn(boolean turn) throws IOException;

    void quit(int clientID) throws IOException;
}
