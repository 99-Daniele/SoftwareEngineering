package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;
import it.polimi.ingsw.network.messages.Message_One_Parameter_String;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ControllerConnection {

    private static int count=0;
    private static Game game;
    private static  int max;
    private static ControllerGame controllerGame;

    public static synchronized PosControllerGame connection(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        Message_One_Parameter_String message = (Message_One_Parameter_String) in.readObject();
        String nickname = message.getPar();
        boolean error=true;
        /*
        if(count==0){
            Message firstLoginMessage = new Message_One_Parameter_Int(MessageType.LOGIN, 1);
            out.flush();
            out.writeObject(firstLoginMessage);
            count = Integer.parseInt(in.nextLine());
            max = count;
            game = new Game(count);
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
                    error = false;
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
         */
        return null;
    }
}
