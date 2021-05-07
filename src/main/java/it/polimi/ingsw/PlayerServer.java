package it.polimi.ingsw;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerServer implements Runnable{

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private ControllerGame controllerGame;
    private int position;

    public PlayerServer(Socket socket) throws IOException {
        this.socket=socket;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

     public void run() {
        try {
            if (controllerGame == null) {
                PosControllerGame posControllerGame = ControllerConnection.connection(in, out);
                position = posControllerGame.getPosition();
                controllerGame = posControllerGame.getControllerGame();
                Message loginMessage = new Message_One_Parameter_Int(MessageType.LOGIN, position);
                out.flush();
                out.writeObject(loginMessage);
                while (posControllerGame.getMax() != controllerGame.getNumberOfViews()) {
                    try {
                        Thread.sleep(5000);//dorme per 5 secondi
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message startMessage = new Message_One_Parameter_Int(MessageType.START_GAME, controllerGame.getNumberOfViews());
                out.flush();
                out.writeObject(startMessage);
            }
            while (true) {
                Message msg = (Message) in.readObject();
                if(processMsg(msg))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
     }

     private boolean processMsg(Message message) throws IOException {
         switch (message.getMessageType()) {
             case LOGIN:
                 login_message(message);
                 return false;
             case BUY_CARD:
                 buy_card_message(message);
                 return false;
             case SWITCH_DEPOT:
                 switch_depot_message(message);
                 return false;
             case TURN:
                 turn_message();
                 return false;
             default:
                 return true;
         }
     }
     
     private void login_message(Message message){}
     
     private void buy_card_message(Message message){}
     
     private void switch_depot_message(Message message){}
     
     private void turn_message() throws IOException {
         Message turnMessage;
         if (position != controllerGame.getCurrentTurn()) {
             turnMessage = new Message_One_Parameter_Int(MessageType.TURN, 0);
             out.flush();
             out.writeObject(turnMessage);
         }
         else {
             turnMessage = new Message_One_Parameter_Int(MessageType.TURN, 1);
             out.flush();
             out.writeObject(turnMessage);
             controllerGame.play();
         }
     }
}
//lato client bloccare messaggi quando in attesa altrimenti buffer li memorizza
// esempio server risponde collegato e client non prende ingressi e quando tutti collegati server avvisa e client può prendere in ingresso e inviare
