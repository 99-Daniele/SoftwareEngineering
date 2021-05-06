package it.polimi.ingsw;

import java.util.Observable;
import java.util.Observer;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class VirtualView extends Observable implements View, Observer {
    private ArrayList<ViewPos> viewsPos;

    public VirtualView(Scanner in, PrintWriter out, Observer observer){
        viewsPos=new ArrayList<>();
        viewsPos.add(new ViewPos(in, out));
        addObserver(observer);
    }

    public void addViewPos(Scanner in, PrintWriter out){
        viewsPos.add(new ViewPos(in, out));
    }

    public int NumOfViewsPos(){
        return viewsPos.size();
    }

   /* public void start(int currentPos){
        String msg=CurrentViewIn(currentPos).nextLine();
        setChanged();
        notifyObserver(msg);
    } metodo che riceve

    private Scanner CurrentViewIn(int currentPos){
        return viewsPos.get(currentPos).getIn();
    }
    private PrintWriter CurrentViewOut(int currentPos){
        return viewsPos.get(currentPos).getOut();
    }*/

    @Override
    public void update(Observable o, Object arg) {

    }
}
