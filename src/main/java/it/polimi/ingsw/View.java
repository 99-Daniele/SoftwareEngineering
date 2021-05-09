package it.polimi.ingsw;

import java.io.PrintWriter;
import java.util.Scanner;

public interface View{
    String nickname();

    void nicknameTaken();

    void myTurn(boolean turn);
}
