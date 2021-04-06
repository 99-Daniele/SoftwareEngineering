package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when try to insert DevelopmentCard with different color or level of Deck
 */
public class WrongDevelopmentCardInsertionException extends Exception{

    public WrongDevelopmentCardInsertionException(){
        super("Questa carta non può essere inserita in questo mazzetto");
    }
}
