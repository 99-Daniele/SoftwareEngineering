package it.polimi.ingsw.model;

/**
 * this class represent the marble of the market.
 */
public abstract class Marble {

    /**
     * @return false if the marble has to be discarded
     */
    public abstract boolean useMarble(LightGame game);
}
