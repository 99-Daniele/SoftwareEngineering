package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when game is still going even if it should have ended
 */
public class AlreadyFinishedGameException extends Exception{

    public AlreadyFinishedGameException(){
        super("Il gioco è finito! Non puoi più compiere operazioni.");
    }
}
