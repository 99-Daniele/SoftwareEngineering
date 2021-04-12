package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

public class MultiPlayerGame extends Game{

    public MultiPlayerGame(int numOfPlayers) {
        super(numOfPlayers);
    }

    @Override
    public void startGame() {    }
}
