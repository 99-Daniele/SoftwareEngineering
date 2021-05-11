package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.resourceContainers.Resource;

public class Message_Two_Resource_Two_Int extends Message{
    private final Resource resource1;
    private final int amount1;
    private final Resource resource2;
    private final int amount2;

    public Message_Two_Resource_Two_Int(int clientID, MessageType messageType, Resource resource1, int amount1, Resource resource2, int amount2) {
        super(clientID, messageType);
        this.resource1 = resource1;
        this.amount1 = amount1;
        this.resource2 = resource2;
        this.amount2 = amount2;
    }

    public Resource getResource1() {
        return resource1;
    }

    public int getAmount1() {
        return amount1;
    }

    public Resource getResource2() {
        return resource2;
    }

    public int getAmount2() {
        return amount2;
    }

    @Override
    public String toString() {
        return "MessageSwitchReply{" +
                "resource1=" + resource1 +
                ", amount1=" + amount1 +
                ", resource2=" + resource2 +
                ", amount2=" + amount2 +
                '}';
    }
}
