package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.resourceContainers.Resource;

public class Message_Three_Resource_One_Int extends Message{
    private final Resource resource1;
    private final Resource resource2;
    private final Resource resource3;
    private final int par;

    public Message_Three_Resource_One_Int( MessageType messageType, int clientID, Resource resourceDeleted1, Resource resourceDeleted2, Resource resourceObtained, int par) {
        super(messageType, clientID);
        this.resource1 = resourceDeleted1;
        this.resource2 = resourceDeleted2;
        this.resource3 = resourceObtained;
        this.par = par;
    }

    public Resource getResource1() {
        return resource1;
    }

    public Resource getResource2() {
        return resource2;
    }

    public Resource getResource3() {
        return resource3;
    }

    public int getPar() {
        return par;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "resourceDeleted1: " + resource1 +
                ", resourceDeleted2: " + resource2 +
                ", resourceObtained: " + resource3 +
                '}';
    }
}
