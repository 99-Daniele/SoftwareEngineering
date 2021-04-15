package it.polimi.ingsw.model;

/**
 * this class represent the red marble.
 */
public class RedMarble extends Marble {

    /**
     * @param game is Game
     * @return false because marble can't be converted
     * this method increase current player faith points by 1.
     */
    @Override
    public boolean useMarble(LightGame game){
        game.increaseOneFaithPointCurrentPlayer();
        return false;
    }
}
