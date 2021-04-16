package it.polimi.ingsw.model.actionsTests;

import it.polimi.ingsw.model.developmentCardsTests.Color;
import it.polimi.ingsw.model.games.LightSinglePlayerGame;

/**
 * DiscardAction is the Action that discards the two lowest level DevelopmentCards of one color
 */
public class DiscardAction extends Action {

    private final Color color;

    public DiscardAction(Color color) {
        this.color = color;
    }

    /**
     * @param game is a SinglePlayerGame.
     * this method discard the two lowest level cards with this.color and move the action to last position.
     */
    @Override
    public void actionTrigger(LightSinglePlayerGame game) {
        game.discardDeckDevelopmentCards(color);
        game.moveToLastAction();
    }
}
