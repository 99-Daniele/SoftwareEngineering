package it.polimi.ingsw;

import java.io.PrintWriter;
import java.util.Scanner;

public class ViewPos {
    private Scanner in;
    private PrintWriter out;

    public ViewPos(Scanner in,PrintWriter out){
        this.in=in;
        this.out=out;
    }

    public Scanner getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }
}
