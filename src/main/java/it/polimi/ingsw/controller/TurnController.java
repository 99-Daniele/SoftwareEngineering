package it.polimi.ingsw.controller;

/**
 * TurnController handled game turns. In particular when one player ended the game, proceed to permit to all players to
 * take their last turn.
 */
public class TurnController {

    private int currentPlayer;
    private final int numPlayers;
    private boolean endGame;

    public TurnController(int numPlayers) {
        this.currentPlayer = 0;
        this.numPlayers = numPlayers;
        this.endGame = false;
    }

    /**
     * @param player is a player.
     * @return if it is @param player turn.
     */
    public boolean isMyTurn(int player){
        return currentPlayer == player;
    }

    /**
     * go to the next player, or return to the first one.
     */
    public void nextTurn(){
        currentPlayer++;
        if(currentPlayer == numPlayers)
            currentPlayer = 0;
    }

    public void endGame(){
        endGame = true;
    }

    /**
     * @return if one player has ended the game and all players have taken their last turn.
     */
    public boolean isEndGame(){
        return endGame && (currentPlayer == numPlayers -1);
    }
}
