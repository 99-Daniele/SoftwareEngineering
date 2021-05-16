package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.market.Marble;

public class Message_One_Parameter_Marble extends Message{
    private final Marble marble;

    public Message_One_Parameter_Marble(MessageType messageType, int clientID, Marble marble) {
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
