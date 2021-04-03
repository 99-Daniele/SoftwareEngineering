package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player tries an incorrect switchDepot()
 */
public class ImpossibleSwitchDepotException extends Exception{

    public ImpossibleSwitchDepotException(){
        super("Non è possibile scambiare questi depositi");
    }
}
