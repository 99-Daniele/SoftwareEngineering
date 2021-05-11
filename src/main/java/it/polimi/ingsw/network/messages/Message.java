package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * Message is the class of the standard message exchanged between Client and Server or View and Model or Controller and View.
 */
public class Message implements Serializable {
    private final int clientID;
    private final MessageType messageType;

    public Message(int clientID,MessageType messageType) {
        this.messageType = messageType;
        this.clientID=clientID;
    }

    @Override
    public String toString() {
        return "" + messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public int getClientID() {
        return clientID;
    }
}
