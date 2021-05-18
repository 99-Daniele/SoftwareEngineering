package it.polimi.ingsw.model.player;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;

import java.util.Observable;

/**
 * SimplePlayerBoard is Ludovico PlayerBoard. It can only increase his faith points.
 */
public class SimplePlayerBoard extends Observable {

    private int faithPoints;

    public int getFaithPoints() {
        return faithPoints;
    }

    public void increaseFaithPoints(int faithPoints) {
        this.faithPoints += faithPoints;
        Message message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,2, this.faithPoints);
        setChanged();
        notifyObservers(message_one_parameter_int);
    }

}
