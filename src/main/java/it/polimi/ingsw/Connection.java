package it.polimi.ingsw;

import java.io.IOException;

public class Connection {

    private static int count = 0;
    private static int max;
    private static ControllerGame controllerGame;

    public static synchronized PosControllerGame ConnectionPlayers(VirtualView virtualView, String nickname) throws IOException, ClassNotFoundException {
        if (count == 0) {
            count = virtualView.getNumberPlayers();
            max = count;
            controllerGame = new ControllerGame(count);
            controllerGame.addView(virtualView);
            virtualView.addController(controllerGame);
            controllerGame.addNickname(nickname);
            count--;
            return new PosControllerGame(controllerGame, 1);
        } else {
            controllerGame.addView(virtualView);
            virtualView.addController(controllerGame);
            controllerGame.addNickname(nickname);
            count--;
            return new PosControllerGame(controllerGame, max - count);

        }
    }

}