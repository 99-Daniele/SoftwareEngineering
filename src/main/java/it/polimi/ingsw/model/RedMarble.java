package it.polimi.ingsw.model;

/**
 * this class represent the red marble.
 */
public class RedMarble extends Marble {

    /**
     * @param game is Game
     * @return false
     * this method move current player in FaithTrack by 1.
     */
    @Override
    public boolean useMarble(Game game){
        game.getCurrentPlayer().increaseFaithPoints(1);
        game.faithTrackMovement();
        return false;
    }
}
