package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player chose to activate the production of an empty SlotDevelopmentCards
 */
public class NoSuchProductionPowerException extends Exception{

    public NoSuchProductionPowerException(){
        super("You don't have any card to activate this power");
    }
}