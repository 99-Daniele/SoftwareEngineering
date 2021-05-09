package it.polimi.ingsw;

import java.util.Observable;
import java.util.Observer;

import java.io.PrintWriter;
import java.util.Scanner;

public class VirtualView extends Observable implements View, Observer {
    private Scanner in;
    private PrintWriter out;

    public VirtualView(Scanner in, PrintWriter out){
        this.in=in;
        this.out=out;
    }

    public void addController(ControllerGame controllerGame){
        addObserver(controllerGame);
    }

    public String numberPlayers(){
        out.println("numero giocatori");
        out.flush();
        String msg=in.nextLine();
        return msg;
    }

    public String nickname(){
        String msg=in.nextLine();
        return msg;
    }

    public void nicknameTaken(){
        out.println("nickname già preso");
        out.flush();
    }

    public void myTurn(boolean turn){
        if(turn)
        {
            out.println("ok");
            out.flush();
        }
        else {
            out.println("non mio turno");
            out.flush();
        }
    }



    public void start() {
        String msg=in.nextLine();
        setChanged();
        notifyObservers(msg);
    }



    public void position(int position){
        out.println(""+position);
        out.flush();
    }

    public void pronto()
    {
        out.println("tutti connessi");
        out.flush();
    }


/*
    private Scanner CurrentViewIn(int currentPos){
        return viewsPos.get(currentPos).getIn();
    }
    private PrintWriter CurrentViewOut(int currentPos){
        return viewsPos.get(currentPos).getOut();
    }
    */

    @Override
    public void update(Observable o, Object arg) {

    }
}
