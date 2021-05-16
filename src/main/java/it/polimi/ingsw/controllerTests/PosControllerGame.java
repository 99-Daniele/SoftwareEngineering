package it.polimi.ingsw.controllerTests;

public class PosControllerGame {
    private final ControllerGame controllerGame;
    private final int position;

    public PosControllerGame(ControllerGame controllerGame, int position){
        this.controllerGame=controllerGame;
        this.position=position;
    }

    public int getPosition() {
        return position;
    }

    public ControllerGame getControllerGame() {
        return controllerGame;
    }
}
