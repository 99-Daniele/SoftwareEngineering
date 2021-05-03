package it.polimi.ingsw.model.player;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

/**
 * PlayerBoard is the board of the player. Contains his nickname, his warehouse, his strongbox, his owned DevelopmentCards,
 * his owned LeaderCards and summarize of his VictoryPoints.
 */
public class PlayerBoard extends SimplePlayerBoard {

    private final String nickname;
    private final Warehouse warehouse;
    private final Strongbox strongbox;
    private final SlotDevelopmentCards[] slotDevelopmentCards = new SlotDevelopmentCards[3];
    private final ArrayList<LeaderCard> leaderCards;
    private final VictoryPoints victoryPoints;

    /**
     * @param nickname is player chosen nickname which was previously verified to be unique among other players.
     * the constructor call SimplePlayerBoard constructor, and then initialized nickname, Warehouse, Strongbox, the 3
     * SlotDevelopmentCards, the list of LeaderCards and the VictoryPoints.
     */
    public PlayerBoard(String nickname) {
        super();
        this.nickname = nickname;
        warehouse = new Warehouse();
        strongbox = new Strongbox();
        slotDevelopmentCards[0] = new SlotDevelopmentCards();
        slotDevelopmentCards[1] = new SlotDevelopmentCards();
        slotDevelopmentCards[2] = new SlotDevelopmentCards();
        leaderCards = new ArrayList<>();
        victoryPoints = new VictoryPoints();
    }

    public String getNickname() {
        return nickname;
    }

    public VictoryPoints getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * @param card1 is the first LeaderCard chosen by player at the beginning.
     * @param card2 is the second LeaderCard chosen by player at the beginning.
     */
    public void addLeaderCard(LeaderCard card1, LeaderCard card2){
        leaderCards.add(card1);
        leaderCards.add(card2);
    }

    /**
     * @param resource stands for the type of resource to increase by 1 in Warehouse.
     * @return false if @param resource has to be discarded.
     */
    public boolean increaseWarehouse(Resource resource){
        return warehouse.increaseResource(resource);
    }

    /**
     *
     */
    public ArrayList<Integer[]> availableSwitches(){
        return warehouse.availableSwitches();
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
     * @param card is DevelopmentCard to buy.
     * @param slot is player's choice about in which slot @param card will be inserted.
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased.
     * @throws InsufficientResourceException if player has not enough resources to buy @param card.
     * if exists active DiscountCard @param card resourceCost could be discounted. In case player has still not enough.
     * resources, @param card resourceCost return to original and card is not bought by player.
     */
    public void buyDevelopmentCard(DevelopmentCard card, int slot, int choice)
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {
        boolean discount1 = false;
        boolean discount2 = false;
        if(leaderCards.size() > 0)
            discount1 = leaderCards.get(0).discount(card);
        if(leaderCards.size() > 1)
            discount2 = leaderCards.get(1).discount(card);
        if(isBuyable(card)){
            if(!(slotDevelopmentCards[slot -1].addDevelopmentCard(card)))
                throw new ImpossibleDevelopmentCardAdditionException();
            card.buyCard(warehouse, strongbox, choice);
            victoryPoints.increaseVictoryPointsByCards(card.getVictoryPoints());
        }
        else {
            if (discount1)
                leaderCards.get(0).recount(card);
            if (discount2)
                leaderCards.get(1).recount(card);
            throw new InsufficientResourceException();
        }
    }

    /**
     * @param card is DevelopmentCard to buy.
     * @return true if player has enough resources.
     */
    public boolean isBuyable(DevelopmentCard card){
        return card.isBuyable(warehouse, strongbox);
    }

    /**
     * @param card is DevelopmentCard to buy.
     * @return an ArrayList </Integer> of slots where @card can be inserted.
     * for each slot verifies if @param card has the required level and in case add to @param slots.
     */
    public ArrayList<Integer> findAvailableSlot(DevelopmentCard card){
        ArrayList<Integer> slots = new ArrayList<>();
        for(int i = 1; i < 4; i++){
            if(slotDevelopmentCards[i -1].haveRequiredLevel(card))
                slots.add(i);
        }
        return slots;
    }

    /**
     * @param choice summarize all player's choices about which production powers activate.
     * @throws InsufficientResourceException if player has not enough resources to activate all production powers together.
     * this method firstly creates copies of player's warehouse and strongbox, and then, for each chosen production power,
     * decrease those copy's resources by the amount required.
     * if no InsufficientResourceException is thrown during controls then player has enough resource to activate all
     * chosen production power together.
     * if player chose to activate a production power which not exist (empty SlotDevelopmentCards, or AdditionalPowerCard
     * which not exist or is not active) the method do nothing without throwing any exception like player has never chose them.
     */
    private void enoughTotalProductionPowerResource(PowerProductionPlayerChoice choice)
            throws InsufficientResourceException {
        Warehouse w;
        w = warehouse.copyThisWarehouse();
        Strongbox s;
        s = strongbox.copyThisStrongbox();
        if(choice.isFirstPower())
            slotDevelopmentCards[0].removeProductionPowerResource(w, s);
        if(choice.isSecondPower())
            slotDevelopmentCards[1].removeProductionPowerResource(w, s);
        if(choice.isThirdPower())
            slotDevelopmentCards[2].removeProductionPowerResource(w, s);
        if(choice.isBasicPower()) {
            decreaseWarehouseResource(choice.getResources()[0], choice.getResources()[1], w, s);
        }
        if(choice.isFirstAdditionalPower())
            if(leaderCards.size() > 0)
                leaderCards.get(0).decreaseProductionPowerResources(w, s, 1);
        if(choice.isSecondPower())
            if(leaderCards.size() > 1)
                leaderCards.get(1).decreaseProductionPowerResources(w, s, 1);
    }

    /**
     *
     */
    public void activateDevelopmentCardProductionPower(int chosenSlot, Strongbox s, int choice)
            throws InsufficientResourceException, NoSuchProductionPowerException {
        slotDevelopmentCards[chosenSlot - 1].removeProductionPowerResource(warehouse, strongbox, choice);
        int faithPoints = slotDevelopmentCards[chosenSlot - 1].increaseProductionPowerResource(s);
        increaseFaithPoints(faithPoints);
    }

    /**
     *
     */
    public void increaseStrongbox(Strongbox s){
        for(Resource resource: Resource.values()) {
            strongbox.increaseResourceType(resource, s.getNumOfResource(resource));
        }
    }

    /**
     * @param r1 is player's choice to a resource to be removed.
     * @param r2 is player's choice to a resource to be removed.
     * @return true if has at least 1 @param r1 and @param r2.
     */
    private boolean enoughBasicProductionResource(Resource r1, Resource r2){
        return (((warehouse.getNumOfResource(r1) > 0) || (strongbox.getNumOfResource(r1) > 0))
                 &&((warehouse.getNumOfResource(r2) > 0) || (strongbox.getNumOfResource(r2) > 0)));
    }

    /**
     * @param choice summarize all player's choices about which production powers activate.
     * @return true if player has increased his faith points.
     * @throws InsufficientResourceException by the signature of activateProductionCard().
     * this method firstly verifies if player has enough resources. if player has no enough resources throw an exception
     * without doing anything.
     * for each chosen production power activate the production.
     * if a chosen production power not exist the method do nothing.
     * if player has increased his faith points, calls increaseFaithPoints() method.
     */
    public boolean activateProduction(PowerProductionPlayerChoice choice) throws
            InsufficientResourceException, NoSuchProductionPowerException {
        enoughTotalProductionPowerResource(choice);
        int faithPoints = 0;
        if(choice.isFirstPower())
            faithPoints += slotDevelopmentCards[0].activateProductionActiveCard(warehouse, strongbox, choice.getChoice());
        if(choice.isSecondPower())
            faithPoints += slotDevelopmentCards[1].activateProductionActiveCard(warehouse, strongbox, choice.getChoice());
        if(choice.isThirdPower())
            faithPoints += slotDevelopmentCards[2].activateProductionActiveCard(warehouse, strongbox, choice.getChoice());
        if(choice.isBasicPower())
            activateBasicProduction(choice.getResources()[0], choice.getResources()[1], choice.getResources()[2],
                    warehouse, strongbox, choice.getChoice());
        if(choice.isFirstAdditionalPower())
            if(leaderCards.size() > 0) {
                if(leaderCards.get(0).additionalProductionPower(warehouse, strongbox,
                        choice.getChoice(), choice.getAdditionalResource1()) == 0)
                    throw new NoSuchProductionPowerException();
                else
                    faithPoints++;
            }
        if(choice.isSecondAdditionalPower())
            if(leaderCards.size() > 1) {
                if(leaderCards.get(1).additionalProductionPower(warehouse, strongbox,
                        choice.getChoice(), choice.getAdditionalResource2()) == 0)
                    throw new NoSuchProductionPowerException();
                else
                    faithPoints++;
            }
        if(faithPoints > 0) {
            increaseFaithPoints(faithPoints);
            return true;
        }
        return false;
    }

    /**
     * @param r1 is player's choice to a resource to be removed.
     * @param r2 is player's choice to a resource to be removed.
     * @param r3 is player's choice to a resource to be added.
     * @param w is a warehouse.
     * @param s is a strongbox.
     * @param choice is player's choice about which between @param w and @param s has the priority to be decreased.
     * @throws InsufficientResourceException if @param w and @param s has not enough resources.
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
     *
     */
    public void activateBasicProduction(Resource r1, Resource r2, int choice) throws InsufficientResourceException {
        if(!enoughBasicProductionResource(r1, r2))
            throw new  InsufficientResourceException();
        if(choice == 1){
            decreaseWarehouseResource(r1, r2, warehouse, strongbox);
        }
        else{
            decreaseStrongboxResource(r1, r2, warehouse, strongbox);
        }
    }

    /**
     *
     */
    public void activateAdditionalProductionPower(int chosenAdditionalCard, int choice)
            throws NoSuchProductionPowerException, InsufficientResourceException {
        if (leaderCards.size() < chosenAdditionalCard)
            throw new NoSuchProductionPowerException();
        leaderCards.get(chosenAdditionalCard).decreaseProductionPowerResources(warehouse, strongbox, choice);
        increaseFaithPoints(1);
    }

    /**
     * @param r1 is player's choice to a resource to be removed.
     * @param r2 is player's choice to a resource to be removed.
     * @param w is a warehouse.
     * @param s is a strongbox.
     * this method decrease firstly @param w and then, if necessary, @param s.
     */
    private void decreaseWarehouseResource(Resource r1, Resource r2, Warehouse w, Strongbox s){
        if (w.decreaseResource(r1, 1) > 0)
            s.decreaseResourceType(r1, 1);
        if (w.decreaseResource(r2, 1) > 0)
            s.decreaseResourceType(r2, 1);
    }

    /**
     * @param r1 is player's choice to a resource to be removed.
     * @param r2 is player's choice to a resource to be removed.
     * @param w is a warehouse.
     * @param s is a strongbox.
     * this method decrease firstly @param s and then, if necessary, @param w.
     */
    private void decreaseStrongboxResource(Resource r1, Resource r2, Warehouse w, Strongbox s){
        if(s.decreaseResourceType(r1, 1) > 0)
            w.decreaseResource(r1, 1);
        if(s.decreaseResourceType(r2, 1) > 0)
            w.decreaseResource(r2, 1);
    }

    /**
     * @param chosenLeaderCard is player's choice about which leader card to activate.
     * @throws InsufficientResourceException if player has not enough resources.
     * @throws InsufficientCardsException if player has not enough cards.
     * @throws AlreadyDiscardLeaderCardException if player already discard this LeaderCard previously.
     * @throws ActiveLeaderCardException if this LeaderCard is already active.
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
     * @return a summary of player owned DevelopmentCards.
     */
    private LeaderRequirements createMyLeaderRequirements(){
        LeaderRequirements leaderRequirements = new LeaderRequirements();
        for (int i = 0; i < 3; i++)
            slotDevelopmentCards[i].updateLeaderRequirements(leaderRequirements);
        return leaderRequirements;
    }

    /**
     * @param chosenLeaderCard is player's choice about which leader card to discard.
     * @throws AlreadyDiscardLeaderCardException if player already discard this LeaderCard previously.
     * @throws ActiveLeaderCardException if this LeaderCard is already active.
     */
    public void discardLeaderCard(int chosenLeaderCard)
            throws AlreadyDiscardLeaderCardException, ActiveLeaderCardException {
        if (leaderCards.size() < chosenLeaderCard)
            throw new AlreadyDiscardLeaderCardException();
        if(leaderCards.get(chosenLeaderCard -1).isActive())
            throw new ActiveLeaderCardException();
        leaderCards.remove(chosenLeaderCard -1);
        increaseFaithPoints(1);
    }

    /**
     * @param chosenLeaderCard is the position of one player's LeaderCard.
     * @return true if the chosen LeaderCard is an active WhiteConversionCard.
     * if it isn't an active WhiteConversionCard or even not exist a LeaderCard, @return false.
     */
    public boolean isWhiteConversionLeaderCardActive(int chosenLeaderCard){
       if(leaderCards.size() < chosenLeaderCard)
           return false;
       return leaderCards.get(chosenLeaderCard -1).whiteConversion();
    }

    /**
     * @param chosenLeaderCard is the position of the chosen LeaderCard.
     * @return the chosen LeaderCard.
     */
    public LeaderCard getLeaderCard(int chosenLeaderCard) throws AlreadyDiscardLeaderCardException {
        if (leaderCards.size() < chosenLeaderCard)
            throw new AlreadyDiscardLeaderCardException();
        else
            return leaderCards.get(chosenLeaderCard -1);
    }

    /**
     * @return true if player has 7 DevelopmentCards, otherwise @return false.
     */
    public boolean haveSevenDevelopmentCards(){
        int count = 0;
        for (int i = 0; i < 3; i++)
            count += slotDevelopmentCards[i].getNumOfCards();
        return (count >= 7);
    }

    /**
     * @return the sum of all victoryPoints of player.
     */
    public int sumVictoryPoints(){
        return victoryPointsByAmountOfResource() + victoryPoints.sumVictoryPoints();
    }

    /**
     * @return the victory points earned by the amount of resources.
     * this method calculate the sum of total
     */
    private int victoryPointsByAmountOfResource(){
        return sumTotalResource()/5;
    }

    /**
     * @return the total amount of resource owned by player.
     */
    public int sumTotalResource(){
        return (warehouse.sumWarehouseResource() + strongbox.sumStrongboxResource());
    }

}