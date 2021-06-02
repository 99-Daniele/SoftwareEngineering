package it.polimi.ingsw.network.messages;

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
