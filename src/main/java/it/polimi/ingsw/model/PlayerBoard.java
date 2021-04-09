package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

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

    /**
     * @param card is a LeaderCard chosen by player at the beginning
     */
    public void addLeaderCard(LeaderCard card){
        leaderCards.add(card);
    }

    /**
     * @param depot1 stands for position of the first Depot in Warehouse to switch.
     * @param depot2 stands for position of the second Depot in Warehouse to switch.
     * @throws ImpossibleSwitchDepotException if the switch is not possible.
     */
    public void switchDepots(int depot1, int depot2) throws ImpossibleSwitchDepotException {
        warehouse.switchDepots(depot1, depot2);
    }

    /**
     * @param card is DevelopmentCard to buy
     * @param slot is player's choice about in which slot @param card will be inserted
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased
     * @throws WrongDevelopmentCardsSlotException if chosen slot can't contain @param card
     * @throws InsufficientResourceException if player has not enough resources to buy @param card
     */
    public void buyDevelopmentCard(DevelopmentCard card, int slot, int choice)
            throws WrongDevelopmentCardsSlotException, InsufficientResourceException {
        if(!(slotDevelopmentCards[slot -1].haveRequiredLevel(card)))
            throw new WrongDevelopmentCardsSlotException();
        card.buyCard(warehouse, strongbox, choice);
        slotDevelopmentCards[slot -1].addDevelopmentCard(card);
        victoryPoints.increaseVictoryPointsByCards(card.getVictoryPoints());
    }

    /**
     * @return true if player has enough resource to activate all chosen production powers together
     */
    public boolean enoughTotalProductionPowerResource(){ return false;}

    /**
     * @param r1 is player's choice to a resource to be removed
     * @param r2 is player's choice to a resource to be removed
     * @return true if has at least 1 @param r1 and @param r2
     */
    private boolean enoughBasicProductionResource(Resource r1, Resource r2){
        return (((warehouse.getNumOfResource(r1) > 0) || (strongbox.getNumOfResource(r1) > 0))
                 &&((warehouse.getNumOfResource(r2) > 0) || (strongbox.getNumOfResource(r2) > 0)));
    }

    /**
     * this method activate the production of all production powers chosen by player
     */
    public void activateProduction(){}

    /**
     * @param r1 is player's choice to a resource to be removed
     * @param r2 is player's choice to a resource to be removed
     * @param r3 is player's choice to a resource to be added
     * @param w is a warehouse
     * @param s is a strongbox
     * @param choice is player's choice about which between @param w and @param s has the priority to be decreased
     * @throws InsufficientResourceException if w and s has not enough resources
     */
    private void activateBasicProduction(Resource r1, Resource r2, Resource r3, Warehouse w, Strongbox s, int choice)
            throws InsufficientResourceException {
        if(!enoughBasicProductionResource(r1, r2))
            throw new InsufficientResourceException();
        if(choice == 1){
            decreaseWarehouseResource(r1, r2, w, s);
        }
        else{
            decreaseStrongboxResource(r1, r2, w, s);
        }
        s.increaseResourceType(r3, 1);
    }

    /**
     * @param r1 is player's choice to a resource to be removed
     * @param r2 is player's choice to a resource to be removed
     * @param w is a warehouse
     * @param s is a strongbox
     * this method decrease firstly @param w and then, if necessary, @param s
     */
    private void decreaseWarehouseResource(Resource r1, Resource r2, Warehouse w, Strongbox s){
        if(w.decreaseResource(r1, 1) > 0)
            s.decreaseResourceType(r1, 1);
        if(w.decreaseResource(r2, 1) > 0)
            s.decreaseResourceType(r2, 1);
    }

    /**
     * @param r1 is player's choice to a resource to be removed
     * @param r2 is player's choice to a resource to be removed
     * @param w is a warehouse
     * @param s is a strongbox
     * this method decrease firstly @param s and then, if necessary, @param w
     */
    private void decreaseStrongboxResource(Resource r1, Resource r2, Warehouse w, Strongbox s){
        if(s.decreaseResourceType(r1, 1) > 0)
            w.decreaseResource(r1, 1);
        if(s.decreaseResourceType(r2, 1) > 0)
            w.decreaseResource(r2, 1);
    }

    /**
     * @param chosenLeaderCard is player's choice about which leader card to activate
     * @throws InsufficientResourceException if player has not enough resources
     * @throws InsufficientCardsException if player has not enough cards
     * @throws AlreadyDiscardLeaderCardException if player already discard this LeaderCard previously
     * @throws ActiveLeaderCardException if this LeaderCard is already active
     */
    public void activateLeaderCard(int chosenLeaderCard) throws InsufficientResourceException, InsufficientCardsException,
            AlreadyDiscardLeaderCardException, ActiveLeaderCardException {
        if (leaderCards.size() < chosenLeaderCard)
            throw new AlreadyDiscardLeaderCardException();
        LeaderRequirements leaderRequirements = createMyLeaderRequirements();
        leaderCards.get(chosenLeaderCard -1).activateCard(warehouse, strongbox, leaderRequirements);
        victoryPoints.increaseVictoryPointsByCards(leaderCards.get(chosenLeaderCard -1).getVictoryPoints());
    }

    /**
     * @return a summary of player owned DevelopmentCards
     */
    private LeaderRequirements createMyLeaderRequirements(){
        LeaderRequirements leaderRequirements = new LeaderRequirements();
        for (int i = 0; i < 3; i++)
            slotDevelopmentCards[i].updateLeaderRequirements(leaderRequirements);
        return leaderRequirements;
    }

    /**
     * @param chosenLeaderCard is player's choice about which leader card to discard
     * @throws AlreadyDiscardLeaderCardException if player already discard this LeaderCard previously
     * @throws ActiveLeaderCardException if this LeaderCard is already active
     */
    public void discardLeaderCard(int chosenLeaderCard)
            throws AlreadyDiscardLeaderCardException, ActiveLeaderCardException {
        if (leaderCards.size() < chosenLeaderCard)
            throw new AlreadyDiscardLeaderCardException();
        if(leaderCards.get(chosenLeaderCard -1).isActive())
            throw new ActiveLeaderCardException();
        leaderCards.remove(chosenLeaderCard -1);
    }

    /**
     * @param chosenLeaderCard is player's choice about which leader card to discard
     * @return Resource.WHITE if the LeaderCard is not a WhiteConversionCard, otherwise @return a Resource
     */
    public Resource whiteConversion(int chosenLeaderCard){
        return leaderCards.get(chosenLeaderCard -1).whiteConversion();
    }

    /**
     * @return true if player has 7 DevelopmentCards, otherwise @return false
     */
    public boolean haveSevenDevelopmentCards(){
        int count = 0;
        for (int i = 0; i < 3; i++)
            count += slotDevelopmentCards[i].getNumOfCards();
        return (count >= 7);
    }

    /**
     * @return the sum of all victoryPoints of player
     */
    public int sumVictoryPoints(){
        return victoryPoints.sumVictoryPoints();
    }

    /**
     * @return the total amount of resource owned by player
     */
    public int sumTotalResource(){
        return (warehouse.sumWarehouseResource() + strongbox.sumStrongboxResource());
    }
}
