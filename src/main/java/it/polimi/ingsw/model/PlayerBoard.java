package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PlayerBoard extends SimplePlayerBoard{

    private final String nickname;
    private Warehouse warehouse;
    private Strongbox strongbox;
    private SlotDevelopmentCards[] slotDevelopmentCards;
    private ArrayList<LeaderCard> leaderCards;
    private VictoryPoints victoryPoints;

    public PlayerBoard(String nickname) {
        super();
        this.nickname = nickname;
        warehouse = new Warehouse();
        strongbox = new Strongbox();
        slotDevelopmentCards = new SlotDevelopmentCards[3];
        leaderCards = new ArrayList<>();
        victoryPoints = new VictoryPoints();
    }

    public String getNickname() {
        return nickname;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }

    public VictoryPoints getVictoryPoints() {
        return victoryPoints;
    }

    public void addLeaderCard(LeaderCard card){}

    public void switchDepots(int Depot1, int Depot2){}

    public void addDevelopmentCard(DevelopmentCard card){}

    public boolean enoughTotalResource(){ return false;}

    private boolean enoughBasicProductionResource(Resource r1, Resource r2){ return false;}

    public void activateProduction(){}

    private void activateBasicProduction(Resource r1, Resource r2, Resource r3){}

    public void activateLeaderCard(int chosenLeaderCard){}

    public LeaderRequirements createMyLeaderRequirements(){
        return new LeaderRequirements();
    }

    public void discardLeaderCard(int chosenLeaderCard){}

    public Resource whiteConversion(int chosenLeaderCard){
        return Resource.WHITE;
    }

    public boolean haveSevenDevelopmentCards(){ return false;}

    public int sumVictoryPoints(){ return 0;}

    public int sumTotalResource(){ return 0;}
}
