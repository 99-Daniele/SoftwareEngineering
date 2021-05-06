package it.polimi.ingsw;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PlayerServer implements Runnable{

    private Socket socket;
    private ControllerGame controllerGame;
    private int position;

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
             PosControllerGame posControllerGame= ControllerConnection.connection(in, out);
             position=posControllerGame.getPosition();
             controllerGame=posControllerGame.getControllerGame();
             out.println("posizione "+position); //usati per vedere se funz
             out.flush();
             while(posControllerGame.getMax()!=controllerGame.getNumberOfViews()){
                 try {
                     Thread.sleep(5000);//dorme per 5 secondi
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
             out.println("tutti collegati");//usati per vedere se funz
             out.flush();
         }
         while(true)
         {
             String msg=in.nextLine();
             if (position!=controllerGame.getCurrentTurn())
             {
                 out.println("non tuo turno");
                 out.flush();
             }
             else
             {
                 out.println("ok");
                 out.flush();
                 controllerGame.play();
             }
         }
     }
}
//lato client bloccare messaggi quando in attesa altrimenti buffer li memorizza
// esempio server risponde collegato e client non prende ingressi e quando tutti collegati server avvisa e client può prendere in ingresso e inviare
