package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class PlayerBoardView {

    private final String nickName;
    private WarehouseStrongboxView warehouse_strongbox;
    private CardsSlotsView cards_slots;

    public PlayerBoardView(String nickName) {
        this.nickName = nickName;
        this.warehouse_strongbox = new WarehouseStrongboxView();
        this.cards_slots = new CardsSlotsView();
    }

    public String getNickName() {
        return nickName;
    }

    public void increaseWarehouse(Resource resource, int depot){
        warehouse_strongbox.increaseWarehouse(resource, depot);
    }

    public void newAmount(Resource resource, int warehouseAmount, int strongboxAmount){
        warehouse_strongbox.newAmount(resource, warehouseAmount, strongboxAmount);
    }

    public void switchDepot(int depot1, int depot2) {
        warehouse_strongbox.switchDepot(depot1, depot2);
    }

    public void addExtraDepot(Resource r){
        warehouse_strongbox.addExtraDepot(r);
    }

    public void setLeaderCards(int firstLeaderCard, int secondLeaderCard){
        cards_slots.setLeaderCards(firstLeaderCard, secondLeaderCard);
    }

    public ArrayList<Integer> getDevelopmentCards(){
        return cards_slots.getDevelopmentCards();
    }

    public ArrayList<Integer> getLeaderCards(){
        return cards_slots.getLeaderCards();
    }

    public boolean isLeaderCardActive(int leaderCard){
        return cards_slots.isLeaderCardActive(leaderCard);
    }

    public WarehouseStrongboxView getWarehouse_strongbox() {
        return warehouse_strongbox;
    }

    public CardsSlotsView getCards_slots() {
        return cards_slots;
    }

    public void addLeaderCard(int leaderCard){
        cards_slots.addLeaderCard(leaderCard);
    }

    public void addDevelopmentCard(int cardID, int slot){
        cards_slots.addDevelopmentCard(cardID, slot);
    }

    public void discardLeaderCard(int chosenLeaderCard){
        cards_slots.discardLeaderCard(chosenLeaderCard);
    }

    public void printSlots(){
        cards_slots.printCliSlot();
    }

    public void printLeaderCards(){
        cards_slots.printCliLeaderCard();
    }

    public void printCliAllPlayerBoard(){
        System.out.println(nickName);
        warehouse_strongbox.printCliWarehouse();
        warehouse_strongbox.printCliStrongbox();
        printSlots();
        printLeaderCards();
    }

    public ArrayList<ResourceContainerView> getWarehouse(){
        return warehouse_strongbox.getWarehouse();
    }

    public int coinAmount(){
        return warehouse_strongbox.coinAmount();
    }

    public int shieldAmount(){
        return warehouse_strongbox.shieldAmount();
    }

    public int stoneAmount(){
        return warehouse_strongbox.stoneAmount();
    }

    public int servantAmount(){
        return warehouse_strongbox.servantAmount();
    }
}
