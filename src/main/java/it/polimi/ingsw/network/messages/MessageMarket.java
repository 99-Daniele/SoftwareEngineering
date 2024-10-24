package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.modelView.MarketView;

/**
 * MessageMarket is a type of Message with MarketView param.
 */
public class MessageMarket extends Message{

    private final MarketView market;

    public MessageMarket(MessageType messageType, int clientID, MarketView market) {
        super(messageType, clientID);
        this.market = market;
    }

    public MarketView getMarket() {
        return market;
    }

    @Override
    public String toString() {
        return super.toString() + "Market";
    }
}
