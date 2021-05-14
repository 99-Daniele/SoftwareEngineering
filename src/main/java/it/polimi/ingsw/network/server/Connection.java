package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.PosControllerGame;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;

public class Connection {

    private static int count = 0;
    private static int max;
    private static ControllerGame controllerGame;

    public static synchronized PosControllerGame ConnectionPlayers(VirtualView virtualView, String nickname) throws IOException{
        if (count == 0) {
            count = virtualView.getNumberPlayers();
            if(count == -1)
                throw new IOException();
            max = count;
            controllerGame = new ControllerGame(count);
            controllerGame.addView(virtualView);
            virtualView.addController(controllerGame);
            controllerGame.addPlayer(nickname, 0);
            count--;
            return new PosControllerGame(controllerGame, 0);
        } else {
            controllerGame.addView(virtualView);
            virtualView.addController(controllerGame);
            controllerGame.addPlayer(nickname, max - count);
            PosControllerGame posControllerGame = new PosControllerGame(controllerGame, max-count);
            count--;
            return posControllerGame;

        }
    }

}