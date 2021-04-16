package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.games.LightSinglePlayerGame;

/**
 * Action is the standard type of Action of SinglePlayerGame.
 */
public abstract class Action {

    /**
     * @param game is a SinglePlayerGame.
     * this method is the standard method of actionTrigger() which will be override by inherited classes.
     */
    public abstract void actionTrigger(LightSinglePlayerGame game);
}
