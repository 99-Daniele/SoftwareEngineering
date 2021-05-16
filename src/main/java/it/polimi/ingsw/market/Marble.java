package it.polimi.ingsw.market;

import it.polimi.ingsw.games.LightGame;

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
}
