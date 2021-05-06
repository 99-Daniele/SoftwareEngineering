package it.polimi.ingsw;

import it.polimi.ingsw.model.games.Game;
import java.util.Observable;
import java.util.Observer;

import java.io.PrintWriter;
import java.util.Scanner;

public class ControllerGame implements Observer{
    private Game game;
    private View view;

    public ControllerGame(Game game, Scanner in, PrintWriter out){
        this.game=game;
        VirtualView virtualView=new VirtualView(in, out, this);
        this.view=virtualView;
        game.addObservers(virtualView);
    }

    public View getView(){
        return view;
    }

    public int getCurrentTurn(){
        return game.getCurrentPosition();
    }

    public int getNumberOfViews(){
        return  view.NumOfViewsPos();
    }

    @Override
    public void update(Observable o, Object arg) {

    }




    private void nextTurn()
    {
        game.nextPlayer();
    }

    public void play()//metodo update
    {
        System.out.println("ciao");
        // controllo fine partita
        nextTurn();
    }
}
