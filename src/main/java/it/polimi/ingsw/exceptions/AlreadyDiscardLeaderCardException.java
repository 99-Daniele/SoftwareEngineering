package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries to activate or discard a LeaderCard previously discarded
 */
public class AlreadyDiscardLeaderCardException extends Exception{

    public AlreadyDiscardLeaderCardException(){
        super("Questa carta è stata già scartata in precedenza");
    }
}
