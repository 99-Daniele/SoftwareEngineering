package it.polimi.ingsw;

import it.polimi.ingsw.model.games.Game;

import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ControllerGame {
    private Game game;
    private int position;
    private Scanner in;
    private PrintWriter out;

    public ControllerGame(Game game, int position, Scanner in, PrintWriter out){
        this.game=game;
        this.position=position;
        this.in=in;
        this.out=out;
    }

    public boolean isMyTurn()
    {
        String msg=in.nextLine();
        if(!(position==game.getCurrentPosition()))
        {
            out.println("non tuo turno");
            out.flush();
            return false;
        }
        else
        {
            out.println("ok");
            out.flush();
            return true;
        }
    }

    private void nextTurn()
    {
        game.nextPlayer();
    }

    public void play()
    {
        System.out.println("ciao");
        // controllo fine partita
        nextTurn();
    }

    public void pos(){
        out.println("posizione"+position);
        out.flush();
    }
}
