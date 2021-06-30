package it.polimi.ingsw.exceptions;

/**
 * this exception is thrown when player use production power twice in one turn.
 */
public class AlreadyUsedProductionPowerException extends Exception{

    public AlreadyUsedProductionPowerException(){
        super("You already used this production power");
    }
}
