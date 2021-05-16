package it.polimi.ingsw.exceptions;

/**
 * this exceptions is thrown when player enter an already used nickname
 */
public class AlreadyTakenNicknameException extends Exception{

    public AlreadyTakenNicknameException(){
        super("This nickname has already been taken");
    }
}