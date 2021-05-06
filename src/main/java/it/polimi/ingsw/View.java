package it.polimi.ingsw;

import java.io.PrintWriter;
import java.util.Observable;
import java.util.Scanner;

public interface View{
    void addViewPos(Scanner in, PrintWriter out);

    int NumOfViewsPos();
}
