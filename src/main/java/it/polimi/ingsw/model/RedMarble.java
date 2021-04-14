package it.polimi.ingsw.model;

/**
 * this class represent the red marble.
 */
public class RedMarble extends Marble {

    /**
     * @param game is Game
     * @return always true because marble don't have to be discarded
     * this method move current player in FaithTrack by 1.
     */
    @Override
    public boolean useMarble(LightGame game){
        game.getCurrentPlayer().increaseFaithPoints(1);
        game.faithTrackMovement();
        return true;
    }
}
