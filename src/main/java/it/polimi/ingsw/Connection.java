package it.polimi.ingsw;


public class Connection {

    private static int count = 0;
    private static int max;
    private static ControllerGame controllerGame;

    public static synchronized PosControllerGame ConnectionPlayers(VirtualView virtualView, String nickname) {
        if(count==0){
            count=Integer.parseInt(virtualView.numberPlayers());
            max=count;
            controllerGame=new ControllerGame(count);
            controllerGame.addView(virtualView);
            virtualView.addController(controllerGame);
            controllerGame.addNickname(nickname);
            count--;
            return new PosControllerGame(controllerGame,0);
        }
        else {
            controllerGame.addView(virtualView);
            virtualView.addController(controllerGame);
            controllerGame.addNickname(nickname);
            PosControllerGame posControllerGame=new PosControllerGame(controllerGame, max-count);
            count--;
            return posControllerGame;
        }
    }

}