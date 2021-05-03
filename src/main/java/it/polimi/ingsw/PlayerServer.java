package it.polimi.ingsw;

import it.polimi.ingsw.model.games.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class PlayerServer implements Runnable{
    private Socket socket;
    private ControllerGame controllerGame;

    public PlayerServer(Socket socket){
        this.socket=socket;
    }

     public void run()
     {
         Scanner in= null;
         try {
             in = new Scanner(socket.getInputStream());
         } catch (IOException e) {
             e.printStackTrace();
         }
         PrintWriter out= null;
         try {
             out = new PrintWriter(socket.getOutputStream());
         } catch (IOException e) {
             e.printStackTrace();
         }
         if(controllerGame==null)
         {
             GamePos gamePos= ControllerConnection.connection(in, out);
             controllerGame = new ControllerGame(gamePos.getGame(), gamePos.getPosition(), in, out);
             controllerGame.pos();//non serve solo come prova da togliere
         }
         while(true)
         {
             if (controllerGame.isMyTurn())
             {
                 controllerGame.play();
             }
         }
     }
}
