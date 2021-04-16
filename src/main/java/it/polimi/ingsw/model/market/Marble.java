package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.games.LightGame;

/**
 * this class represent the generic marble of the market.
 */
public abstract class Marble {

    /**
     * @return true if player has to chose in which resource convert the marble.
     */
    public abstract boolean useMarble(LightGame game);
}
