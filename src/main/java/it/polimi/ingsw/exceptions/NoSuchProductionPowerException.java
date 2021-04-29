package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player chose to activate the production of an empty SlotDevelopmentCards
 */
public class NoSuchProductionPowerException extends Exception{

    public NoSuchProductionPowerException(){
        super("Non esistono carte per attivare questo potere di produzione");
    }
}