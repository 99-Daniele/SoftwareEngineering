package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.resourceContainers.Resource;

public class MessageOneIntOneResource extends Message{

    private final int par1;
    private final Resource resource;

    public MessageOneIntOneResource(MessageType messageType, int clientID, Resource resource, int par1) {
        super( messageType,clientID);
        this.par1 = par1;
        this.resource = resource;
    }

    public int getPar1() {
        return par1;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "resource: " + resource +
                ", par1: " + par1 +
                '}';
    }
}
