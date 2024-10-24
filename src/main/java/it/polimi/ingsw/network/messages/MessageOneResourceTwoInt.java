package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.resourceContainers.Resource;

/**
 * MessageOneResourceTwoIntt is a type of Message with two int and one resource params.
 */
public class MessageOneResourceTwoInt extends Message{

    private Resource resource;
    private final int par1;
    private int par2;

    public MessageOneResourceTwoInt(MessageType messageType, int clientID, Resource resource, int par1, int par2) {
        super(messageType, clientID);
        this.resource = resource;
        this.par1 = par1;
        this.par2 = par2;
    }

    public MessageOneResourceTwoInt(MessageType messageType, int clientID, int par1) {
        super(messageType, clientID);
        this.par1 = par1;
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

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setPar2(int par2) {
        this.par2 = par2;
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
