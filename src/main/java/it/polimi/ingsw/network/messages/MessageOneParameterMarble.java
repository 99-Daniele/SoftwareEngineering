package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.market.Marble;

/**
 * MessageOneParameterMarble is a type of Message with one marble param.
 */
public class MessageOneParameterMarble extends Message{
    private final Marble marble;

    public MessageOneParameterMarble(MessageType messageType, int clientID, Marble marble) {
        super(messageType, clientID);
        this.marble = marble;
    }

    public Marble getMarble() {
        return marble;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "marble:" + marble +
                '}';
    }
}
