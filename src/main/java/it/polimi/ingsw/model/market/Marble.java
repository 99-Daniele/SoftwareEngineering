package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.games.LightGame;

import java.io.Serializable;

/**
 * this class represent the generic marble of the market.
 */
public abstract class Marble implements Serializable {

    /**
     * @return true if player has to chose in which resource convert the marble.
     */
    public abstract boolean useMarble(LightGame game);

    @Override
    public abstract String toString();

    public abstract String colorString();
}
