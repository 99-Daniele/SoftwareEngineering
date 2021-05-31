package it.polimi.ingsw.exceptions;

public class AlreadyUsedProductionPowerException extends Exception{

    public AlreadyUsedProductionPowerException(){
        super("You already used this production power");
    }
}
