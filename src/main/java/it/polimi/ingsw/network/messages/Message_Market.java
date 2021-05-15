package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.market.Market;

public class Message_Market extends Message{

    private final Market market;

    public Message_Market(MessageType messageType, int clientID, Market market) {
        super(messageType, clientID);
        this.market = market;
    }

    public Market getMarket() {
        return market;
    }

    @Override
    public String toString() {
        return super.toString() + "Market";
    }
}
