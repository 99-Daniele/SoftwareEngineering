package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.games.LightSinglePlayerGame;

/**
 * LudovicoTwoMoveAction is the Action that move ludovico in FaithTrack by 2.
 */
public class LorenzoTwoMoveAction extends Action {

    /**
     * @param game is SinglePlayerGame.
     *
     * this method move ludovico in FaithTrack by 2 and move the action to last position.
     */
    @Override
    public void actionTrigger(LightSinglePlayerGame game) {
        game.LorenzoFaithTrackMovement(2);
        game.moveToLastAction();
    }
}
