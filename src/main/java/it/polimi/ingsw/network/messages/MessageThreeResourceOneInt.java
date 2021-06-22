package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.resourceContainers.Resource;

/**
 * MessageThreeResourceOneInt is a type of Message with three resources and one int params.
 */
public class MessageThreeResourceOneInt extends Message{

    private Resource resource1;
    private Resource resource2;
    private Resource resource3;
    private int par;

    public MessageThreeResourceOneInt(MessageType messageType, int clientID, Resource resourceDeleted1, Resource resourceDeleted2, Resource resourceObtained, int par) {
        super(messageType, clientID);
        this.resource1 = resourceDeleted1;
        this.resource2 = resourceDeleted2;
        this.resource3 = resourceObtained;
        this.par = par;
    }

    public MessageThreeResourceOneInt(MessageType messageType, int clientID) {
        super(messageType, clientID);
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

    public void setResource1(Resource resource1){
        this.resource1=resource1;
    }

    public void setResource2(Resource resource2) {
        this.resource2 = resource2;
    }

    public void setResource3(Resource resource3) {
        this.resource3 = resource3;
    }

    public void setPar(int par) {
        this.par = par;
    }
}
