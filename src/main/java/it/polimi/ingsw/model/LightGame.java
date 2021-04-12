package it.polimi.ingsw.model;

/**
 * LightGame is a lighter version of Game class which includes only the method useful for operations of marbles.
 */
public interface LightGame {

    PlayerBoard getCurrentPlayer();

    void faithTrackMovement();

    Resource askWhiteMarbleResourceConversionToPlayer(Resource r1, Resource r2);
}
