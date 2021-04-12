package it.polimi.ingsw.model;

public class LudovicoTwoMoveAction extends Action{

    /**
     * @param game is SinglePlayerGame
     * this method move ludovico in FaithTrack by 2 and move the action to last position
     */
    @Override
    public void actionTrigger(LightSinglePlayerGame game) {
        game.LudovicoFaithTrackMovement(2);
        game.moveToLastAction();
    }
}
