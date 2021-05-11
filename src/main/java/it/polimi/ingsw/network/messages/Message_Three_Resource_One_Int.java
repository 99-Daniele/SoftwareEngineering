package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.resourceContainers.Resource;

public class Message_Three_Resource_One_Int extends Message{
    private final Resource resource1;
    private final Resource resource2;
    private final Resource resource3;
    private final int par;

    public Message_Three_Resource_One_Int(int clientID, MessageType messageType, Resource resourceDeleted1, Resource resourceDeleted2, Resource resourceObtained, int par) {
        super(clientID, messageType);
        this.resource1 = resourceDeleted1;
        this.resource2 = resourceDeleted2;
        this.resource3 = resourceObtained;
        this.par = par;
    }

    public Resource getResourceDeleted1() {
        return resource1;
    }

    public Resource getResourceDeleted2() {
        return resource2;
    }

    public Resource getResourceObtained() {
        return resource3;
    }

    @Override
    public String toString() {
        return "MessageBasicProduction{" +
                "resourceDeleted1=" + resource1 +
                ", resourceDeleted2=" + resource2 +
                ", resourceObtained=" + resource3 +
                '}';
    }
}
