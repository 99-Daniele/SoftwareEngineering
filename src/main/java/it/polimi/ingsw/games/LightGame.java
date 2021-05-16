package it.polimi.ingsw.games;

import it.polimi.ingsw.exceptions.AlreadyDiscardLeaderCardException;

import it.polimi.ingsw.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.resourceContainers.Resource;

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