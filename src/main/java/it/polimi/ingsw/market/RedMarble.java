package it.polimi.ingsw.market;

import it.polimi.ingsw.games.LightGame;

/**
 * RedMarble is the Marble which increase player faith points by 1.
 */
public class RedMarble extends Marble {

    /**
     * @param game is Game.
     * @return false because marble can't be converted.
     * this method increase current player faith points by 1.
     */
    @Override
    public boolean useMarble(LightGame game){
        game.increaseOneFaithPointCurrentPlayer();
        return false;
    }

    @Override
    public String toString() {
        return "R";
    }
}
