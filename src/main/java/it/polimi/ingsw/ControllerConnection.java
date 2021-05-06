package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import it.polimi.ingsw.model.games.Game;

import java.io.PrintWriter;
import java.util.Scanner;

public class ControllerConnection {
    private static int count=0;
    private static Game game;
    private static  int max;
    private static ControllerGame controllerGame;

    public static synchronized PosControllerGame connection(Scanner in, PrintWriter out){
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
            controllerGame=new ControllerGame(game,in,out);
            PosControllerGame posControllerGame=new PosControllerGame(controllerGame,0, max);
            return posControllerGame;
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
            controllerGame.getView().addViewPos(in, out);
            PosControllerGame posControllerGame=new PosControllerGame(controllerGame, max-count, max);
            count--;
            return posControllerGame;
        }
    }
}
