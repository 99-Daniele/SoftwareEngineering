package it.polimi.ingsw.network.messages;

public class ErrorMessage extends Message{

    private final ErrorType errorType;

    public ErrorMessage(MessageType messageType, int clientID, ErrorType errorType) {
        super(messageType, clientID);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String toString() {
        return super.toString() +
                "errorType=" + errorType +
                "} ";
    }
}
