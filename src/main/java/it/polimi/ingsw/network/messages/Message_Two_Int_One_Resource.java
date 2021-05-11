package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.resourceContainers.Resource;

public class Message_Two_Int_One_Resource extends Message{
    private final int par1;
    private final Resource resource;
    private final int par2;

    public Message_Two_Int_One_Resource(MessageType messageType,int clientID,int chosenLeaderCard, Resource resourceObtained, int warehouse) {
        super( messageType,clientID);
        this.par1 = chosenLeaderCard;
        this.resource = resourceObtained;
        this.par2 = warehouse;
    }

    public int getChosenLeaderCard() {
        return par1;
    }

    public Resource getResourceObtained() {
        return resource;
    }

    public int isWarehouse() {
        return par2;
    }

    @Override
    public String toString() {
        return "Message_Two_Int_One_Resource{" +
                "par1=" + par1 +
                ", resource=" + resource +
                ", par2=" + par2 +
                '}';
    }
}
