package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyDiscardLeaderCardException;

/**
 * LightGame is a lighter version of Game class which includes only the method useful for operations of marbles.
 */
public interface LightGame {

    void faithTrackMovement();

    boolean isActiveWhiteConversionCard(int chosenLeaderCard);

    LeaderCard getCurrentPlayerLeaderCard(int chosenLeaderCard) throws AlreadyDiscardLeaderCardException;

    boolean increaseWarehouse(Resource resource);

    void whiteMarbleConversion(LeaderCard leaderCard);

    void increaseOneFaithPointOtherPlayers();

    void increaseOneFaithPointCurrentPlayer();
}
