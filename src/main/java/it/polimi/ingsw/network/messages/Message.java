package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * Message is the class of the standard message exchanged between Client and Server or View and Model or Controller and View.
 */
public class Message implements Serializable {

    private final MessageType messageType;
    private final int clientID;

    public Message(MessageType messageType, int clientID) {
        this.messageType = messageType;
        this.clientID=clientID;
    }

    @Override
    public String toString() {
        return messageType + ": " + clientID;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public int getClientID() {
        return clientID;
    }
}
