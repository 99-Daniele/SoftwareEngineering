package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.market.Marble;

public class Message_One_Parameter_Marble extends Message{
    private final Marble marble;

    public Message_One_Parameter_Marble(int clientID, MessageType messageType, Marble marble) {
        super(clientID, messageType);
        this.marble = marble;
    }

    public Marble getMarble() {
        return marble;
    }

    @Override
    public String toString() {
        return "UseMarble{" +
                "marble=" + marble +
                '}';
    }
}
