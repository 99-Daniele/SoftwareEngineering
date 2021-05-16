package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.cards.developmentCards.Color;

/**
 * LightSinglePlayerGame is a lighter version of SinglePlayerGame which includes only the method useful for operations of actions.
 */
public interface LightSinglePlayerGame {

    void discardDeckDevelopmentCards(Color color);

    void moveToLastAction();

    void LorenzoFaithTrackMovement(int faithPoints);

    void shuffleActions();
}
