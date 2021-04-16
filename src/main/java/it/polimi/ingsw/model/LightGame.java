package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyDiscardLeaderCardException;

/**
 * LightGame is a lighter version of Game class which includes only the method useful for operations of marbles.
 */
public interface LightGame {

    boolean increaseWarehouse(Resource resource);

    void increaseOneFaithPointOtherPlayers();

    void increaseOneFaithPointCurrentPlayer();

    LeaderCard getCurrentPlayerLeaderCard(int chosenLeaderCard) throws AlreadyDiscardLeaderCardException;

    void faithTrackMovement();

    boolean isActiveWhiteConversionCard(int chosenLeaderCard);

    void whiteMarbleConversion(LeaderCard leaderCard);
}
