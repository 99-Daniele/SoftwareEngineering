package it.polimi.ingsw.model;

public class MultiPlayerGame extends Game{

    public MultiPlayerGame(int numOfPlayers) {
        super(numOfPlayers);
    }

    @Override
    public void startGame() {    }


    @Override
    public PlayerBoard endGame() {
        return (PlayerBoard) super.endGame();
    }
}
