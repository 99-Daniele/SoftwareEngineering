package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import it.polimi.ingsw.model.games.Game;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import java.io.PrintWriter;
import java.util.Scanner;

public class ControllerGame implements Observer{
    private Game game;
    private ArrayList<View> views;

    public ControllerGame(int numPlayers){
        this.game=new Game(numPlayers);
        views=new ArrayList<>();
    }

    public void addView(View view){
        views.add(view);
        game.addObservers((VirtualView) view);
    }

    public void addNickname(String nickname){
        boolean error=true;
        while (error) {
            try {
                game.createPlayer(nickname);
                error = false;
            } catch (AlreadyTakenNicknameException e) {
                views.get(views.size()-1).nicknameTaken();
                nickname=views.get(views.size()-1).nickname();
            }
        }
    }

    public synchronized void waitPlayers(){
        if(views.size()==game.getNumOfPlayers())
            notifyAll();
        else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();// se interrotto durante wait
            }
        }
    }

    public void isMyTurn(int pos){
        if(pos!=game.getCurrentPosition())
            views.get(pos).myTurn(false);
        else
            views.get(pos).myTurn(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        String s=(String)arg;
        int i=Integer.parseInt(s);
        isMyTurn(i);
        game.nextPlayer();
    }


    private void nextTurn()
    {
        game.nextPlayer();
    }

   /* public int getCurrentTurn(){
        return game.getCurrentPosition();
    }*/
}

