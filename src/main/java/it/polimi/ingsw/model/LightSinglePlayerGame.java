package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

/**
 * LightSinglePlayerGame is a lighter version of SinglePlayerGame which includes only the method useful for operations of actions.
 */
public interface LightSinglePlayerGame {

    void discardDeckDevelopmentCards(Color color);

    void moveToLastAction();

    void LudovicoFaithTrackMovement(int faithPoints);

    void shuffleActions();
}
