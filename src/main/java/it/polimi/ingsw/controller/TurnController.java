package it.polimi.ingsw.controller;

public class TurnController {

    private int currentPlayer;
    private final int numPlayers;
    private boolean endGame;

    public TurnController(int numPlayers) {
        this.currentPlayer = 0;
        this.numPlayers = numPlayers;
        this.endGame = false;
    }

    public boolean isMyTurn(int player){
        return currentPlayer == player;
    }

    public void nextTurn(){
        currentPlayer++;
        if(currentPlayer == numPlayers)
            currentPlayer = 0;
    }

    public void endGame(){
        endGame = true;
    }

    public boolean isEndGame(){
        return endGame && (currentPlayer == numPlayers -1);
    }
}
