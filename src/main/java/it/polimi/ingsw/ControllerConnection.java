package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import it.polimi.ingsw.model.games.Game;

import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ControllerConnection {
    private static int count=0;
    private static int max;
    private static Game game;

    public static synchronized GamePos connection(Scanner in, PrintWriter out){
        String nickname=in.nextLine();
        boolean error=true;
        if(count==0){
            out.println("dammi num giocatori");
            out.flush();
            count=Integer.parseInt(in.nextLine());
            max=count;
            game= new Game(count);
            try {
                game.createPlayer(nickname);
            } catch (AlreadyTakenNicknameException e){}
            out.println("ok");
            out.flush();
            count--;
            GamePos gamePos=new GamePos(game,0);
            return gamePos;
        }
        else
        {
            while (error)
            {
                try {
                    game.createPlayer(nickname);
                    error=false;
                } catch (AlreadyTakenNicknameException e) {
                    out.println("questo nickname già usato");
                    out.flush();
                    nickname = in.nextLine();
                }
            }
            GamePos gamePos=new GamePos(game,max-count);
            count--;
            return gamePos;
        }
    }
}
