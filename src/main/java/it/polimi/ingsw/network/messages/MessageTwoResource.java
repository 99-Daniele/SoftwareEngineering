package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.resourceContainers.Resource;

public class MessageTwoResource extends Message{

    private final Resource r1;
    private final Resource r2;

    public MessageTwoResource(MessageType messageType, int clientID, Resource r1, Resource r2) {
        super(messageType, clientID);
        this.r1 = r1;
        this.r2 = r2;
    }

    public Resource getR1() {
        return r1;
    }

    public Resource getR2() {
        return r2;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "resource1: " + r1 +
                ", resource2: " + r2 +
                '}';
    }
}
