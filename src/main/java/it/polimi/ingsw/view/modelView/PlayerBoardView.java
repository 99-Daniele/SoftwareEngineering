package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class PlayerBoardView {

    private final String nickName;
    private final WarehouseStrongboxView warehouseStrongboxView;
    private final CardsSlotsView cardsSlotsView;

    public PlayerBoardView(String nickName) {
        this.nickName = nickName;
        this.warehouseStrongboxView = new WarehouseStrongboxView();
        this.cardsSlotsView = new CardsSlotsView();
    }

    public String getNickName() {
        return nickName;
    }

    public void increaseWarehouse(Resource resource, int depot){
        warehouseStrongboxView.increaseWarehouse(resource, depot);
    }

    public void newAmount(Resource resource, int warehouseAmount, int strongboxAmount){
        warehouseStrongboxView.newAmount(resource, warehouseAmount, strongboxAmount);
    }

    public void switchDepot(int depot1, int depot2) {
        warehouseStrongboxView.switchDepot(depot1, depot2);
    }

    public void addExtraDepot(Resource r){
        warehouseStrongboxView.addExtraDepot(r, cardsSlotsView.isLeaderCardActive(2) && !warehouseStrongboxView.existExtraDepot2());
    }

    public void setLeaderCards(int firstLeaderCard, int secondLeaderCard){
        cardsSlotsView.setLeaderCards(firstLeaderCard, secondLeaderCard);
    }

    public ArrayList<Integer> getDevelopmentCards(){
        return cardsSlotsView.getDevelopmentCards();
    }

    public ArrayList<Integer> getLeaderCards(){
        return cardsSlotsView.getLeaderCards();
    }

    public boolean isLeaderCardActive(int leaderCard){
        return cardsSlotsView.isLeaderCardActive(leaderCard);
    }

    public WarehouseStrongboxView getWarehouseStrongboxView() {
        return warehouseStrongboxView;
    }

    public CardsSlotsView getCardsSlotsView() {
        return cardsSlotsView;
    }

    public void addLeaderCard(int leaderCard){
        cardsSlotsView.addLeaderCard(leaderCard);
    }

    public void addDevelopmentCard(int cardID, int slot){
        cardsSlotsView.addDevelopmentCard(cardID, slot);
    }

    public void discardLeaderCard(int chosenLeaderCard){
        cardsSlotsView.discardLeaderCard(chosenLeaderCard);
    }

    public void printSlots(){
        cardsSlotsView.printCliSlot();
    }

    public void printLeaderCards(){
        cardsSlotsView.printCliLeaderCard();
    }

    public void printCliAllPlayerBoard(){
        System.out.println(nickName);
        warehouseStrongboxView.printCliWarehouse();
        warehouseStrongboxView.printCliStrongbox();
        printSlots();
        printLeaderCards();
    }

    public ArrayList<ResourceContainerView> getWarehouse(){
        return warehouseStrongboxView.getWarehouse();
    }

    public int coinAmount(){
        return warehouseStrongboxView.coinAmount();
    }

    public int shieldAmount(){
        return warehouseStrongboxView.shieldAmount();
    }

    public int stoneAmount(){
        return warehouseStrongboxView.stoneAmount();
    }

    public int servantAmount(){
        return warehouseStrongboxView.servantAmount();
    }

    public boolean isSecondDepot(){return warehouseStrongboxView.isSecondDepot();}
}
