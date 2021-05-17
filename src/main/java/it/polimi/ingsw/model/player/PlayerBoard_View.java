package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class PlayerBoard_View {

    private final String nickName;
    private ArrayList<Resource> depot1;
    private ArrayList<Resource> depot2;
    private ArrayList<Resource> depot3;
    private ArrayList<Resource> extraDepot1;
    private ArrayList<Resource> extraDepot2;
    private int coinAmount;
    private int shieldAmount;
    private int servantAmount;
    private int stoneAmount;
    private ArrayList<Integer> firstSlot;
    private ArrayList<Integer> secondSlot;
    private ArrayList<Integer> thirdSlot;
    private int firstLeaderCard;
    private int secondLeaderCard;
    private int victoryPoints;

    public PlayerBoard_View(String nickName) {
        this.nickName = nickName;
    }
}
