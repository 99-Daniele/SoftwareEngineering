package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries to activate or discard an already active LeaderCard
 */
public class ActiveLeaderCardException extends Exception{

    public ActiveLeaderCardException(){
        super("This card has already been activate");
    }
}
