package it.polimi.ingsw.model;

/**
 * this class represent the white marble.
 */
public class WhiteMarble extends Marble {

    /**
     * @param game is Game
     * @return 1 if the marble has to be discarded, otherwise @return 0
     * this method verifies if there are any active WhiteConversionCard and in case convert to him and behave like
     * a ResourceMarble, so increase Warehouse. Otherwise does nothing.
     */
    @Override
    public int useMarble(Game game) {
        return game.whiteConversion();
    }
}
