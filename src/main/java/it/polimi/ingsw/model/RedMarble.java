package it.polimi.ingsw.model;

/**
 * this class represent the red marble.
 */
public class RedMarble extends Marble {

    /**
     * @param game is Game
     * @return 0
     * this method move current player in FaithTrack by 1.
     */
    @Override
    public int useMarble(Game game){
        game.faithTrackMovement();
        return 0;
    }
}
