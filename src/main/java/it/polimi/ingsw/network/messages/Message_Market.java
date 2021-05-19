package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.model_view.Market_View;

public class Message_Market extends Message{

    private final Market_View market;

    public Message_Market(MessageType messageType, int clientID, Market_View market) {
        super(messageType, clientID);
        this.market = market;
    }

    public Market_View getMarket() {
        return market;
    }

    @Override
    public String toString() {
        return super.toString() + "Market";
    }
}
