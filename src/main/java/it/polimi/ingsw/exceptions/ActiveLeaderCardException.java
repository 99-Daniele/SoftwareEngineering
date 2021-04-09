package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries to activate or discard an already active LeaderCard
 */
public class ActiveLeaderCardException extends Exception{

    public ActiveLeaderCardException(){
        super("Questa carta è stata già attivata in precedenza");
    }
}
