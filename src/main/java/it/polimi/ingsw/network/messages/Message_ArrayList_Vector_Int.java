package it.polimi.ingsw.network.messages;

import java.util.ArrayList;

public class Message_ArrayList_Vector_Int extends Message{

    private final ArrayList<int[]> switches = new ArrayList<>();

    public Message_ArrayList_Vector_Int(MessageType messageType, int clientID, int[] switch1) {
        super( messageType, clientID);
        switches.add(switch1);
    }

    public void addSwitch(int[] newSwitch){
        switches.add(newSwitch);
    }

    public ArrayList<int[]> getSwitches() {
        return switches;
    }

    @Override
    public String toString() {
        return "MessageAvailableSwitchReply{" +
                "switches=" +
                '}';
    }
}
