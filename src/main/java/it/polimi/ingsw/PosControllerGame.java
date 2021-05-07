package it.polimi.ingsw;

public class PosControllerGame {
    private final ControllerGame controllerGame;
    private final int position;
    private final int max;

    public PosControllerGame(ControllerGame controllerGame, int position, int max){
        this.controllerGame=controllerGame;
        this.position=position;
        this.max=max;
    }

    public int getPosition() {
        return position;
    }

    public ControllerGame getControllerGame() {
        return controllerGame;
    }

    public int getMax() {
        return max;
    }
}
