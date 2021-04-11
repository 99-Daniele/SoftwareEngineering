package it.polimi.ingsw.model;

import java.util.ArrayList;

public class SinglePlayerGame extends Game{

    private ArrayList<Action> actions = new ArrayList<>(7);
    private SimplePlayerBoard ludovico;

    public SinglePlayerGame() {
        super(1);
    }

    @Override
    public void startGame() {    }

    public void removeAction(){}

    public void shuffleActions(){}

    /**
     * method that looks if ludovico reached the pope space and if true check it out if player is in the vatican section and increase is victory points
     */
    public void LudovicoFaithTrackMovement(){
        if (getFaithTrack().reachPope(ludovico.getFaithPoints()))
        {
            getFaithTrack().victoryPointsVaticanReport(getPlayer(0).getVictoryPoints(),getPlayer(0).getFaithPoints());
        }
    }

    public void discardDeckDevelopmentCards(Color color){}

    @Override
    public SimplePlayerBoard endGame() {
        return super.endGame();
    }
}
