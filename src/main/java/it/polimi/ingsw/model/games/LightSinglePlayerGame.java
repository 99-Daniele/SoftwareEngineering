package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.developmentCardsTests.Color;

/**
 * LightSinglePlayerGame is a lighter version of SinglePlayerGame which includes only the method useful for operations of actions.
 */
public interface LightSinglePlayerGame {

    void discardDeckDevelopmentCards(Color color);

    void moveToLastAction();

    void LudovicoFaithTrackMovement(int faithPoints);

    void shuffleActions();
}
