package it.polimi.ingsw.network.messages;

/**
 * ErrorMessage is a type of Message where messageType is ERR.
 */
public class ErrorMessage extends Message{

    private final ErrorType errorType;

    public ErrorMessage(int clientID, ErrorType errorType) {
        super(MessageType.ERR, clientID);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String toString() {
        return super.toString() + errorType;
    }
}
