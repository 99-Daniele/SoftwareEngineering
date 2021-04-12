package it.polimi.ingsw.model;

public class LudovicoMoveAndShuffleAction extends Action{

    /**
     * @param game is SinglePlayerGame
     * this method move ludovico in FaithTrack by 1 and shuffle actions
     */
    @Override
    public void actionTrigger(LightSinglePlayerGame game) {
        game.LudovicoFaithTrackMovement(1);
        game.shuffleActions();
    }
}
