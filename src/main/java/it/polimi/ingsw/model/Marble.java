package it.polimi.ingsw.model;

/**
 * this class represent the marble of the market.
 */
public abstract class Marble {

    /**
     * @return true if player has to chose in which resource convert the marble
     */
    public abstract boolean useMarble(LightGame game);
}
