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

    public void LudovicoFaithTrackMovement(){}

    public void discardDeckDevelopmentCards(Color color){}

    @Override
    public SimplePlayerBoard endGame() {
        return super.endGame();
    }
}
