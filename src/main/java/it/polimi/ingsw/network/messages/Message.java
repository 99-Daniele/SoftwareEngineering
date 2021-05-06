package it.polimi.ingsw.network.messages;

/**
 * Message is the class of the standard message exchanged between Client and Server or View and Model or Controller and View.
 */
public class Message {

    private final MessageType messageType;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "" + messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

}
