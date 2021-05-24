package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

import static java.util.Collections.swap;

public class PlayerBoard_View {

    private final String nickName;
    private Warehouse_Strongbox_View warehouse_strongbox;
    private Cards_Slots_View cards_slots;
    private int victoryPoints;

    public PlayerBoard_View(String nickName) {
        this.nickName = nickName;
        this.warehouse_strongbox = new Warehouse_Strongbox_View();
        this.cards_slots = new Cards_Slots_View();
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

    public int getLeaderCard(int leaderCard){
        return cards_slots.getLeaderCard(leaderCard);
    }

    public Warehouse_Strongbox_View getWarehouse_strongbox() {
        return warehouse_strongbox;
    }

    public Cards_Slots_View getCards_slots() {
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

    public void setVictoryPoints(int victoryPoints){
        this.victoryPoints = victoryPoints;
    }

    public void printSlots(){
        cards_slots.printCliSlot();
    }

    public void printLeaderCards(){
        cards_slots.printCliLeaderCard();
    }

    public void printVictoryPointsPope(){
        System.out.println("i punti vittoria legati legati alle sezioni papali sono: " + victoryPoints);
    }

    public void printCliAllPlayerBoard(){
        System.out.println(nickName);
        warehouse_strongbox.printCliWarehouse();
        warehouse_strongbox.printCliExtraDepot();
        warehouse_strongbox.printCliStrongbox();
        printSlots();
        printLeaderCards();
    }
}
