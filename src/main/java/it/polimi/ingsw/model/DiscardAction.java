package it.polimi.ingsw.model;

public class DiscardAction extends Action{

    private Color color;

    public DiscardAction(Color color) {
        this.color = color;
    }

    /**
     * @param game is SinglePlayerGame
     * this method discard the two lowest level cards with this.color and move the action to last position
     */
    @Override
    public void actionTrigger(LightSinglePlayerGame game) {
        game.discardDeckDevelopmentCards(color);
        game.moveToLastAction();
    }
}
