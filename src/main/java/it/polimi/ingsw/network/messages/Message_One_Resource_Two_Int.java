package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.resourceContainers.Resource;

public class Message_One_Resource_Two_Int extends Message{

    private final Resource resource;
    private final int par1;
    private final int par2;

    public Message_One_Resource_Two_Int( MessageType messageType, int clientID, Resource resource, int par1, int par2) {
        super(messageType, clientID);
        this.resource = resource;
        this.par1 = par1;
        this.par2 = par2;
    }

    public Resource getResource() {
        return resource;
    }

    public int getPar1() {
        return par1;
    }

    public int getPar2() {
        return par2;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "resource: " + resource +
                ", par1: " + par1 +
                ", par2: " + par2 +
                '}';
    }
}
