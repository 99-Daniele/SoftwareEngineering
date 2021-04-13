package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyFinishedGameException;
import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

public class DiscardAction extends Action{

    private Color color;

    public DiscardAction(Color color) {
        this.color = color;
    }

    /**
     * @param game is SinglePlayerGame
     * @throws EmptyDevelopmentCardDeckException by the signature of discardDeckDevelopmentCard
     * this method discard the two lowest level cards with this.color and move the action to last position
     */
    @Override
    public void actionTrigger(LightSinglePlayerGame game) {
        game.discardDeckDevelopmentCards(color);
        game.moveToLastAction();
    }
}
