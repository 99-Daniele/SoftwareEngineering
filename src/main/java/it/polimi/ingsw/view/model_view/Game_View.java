package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class Game_View {

    private ArrayList<PlayerBoard_View> players = new ArrayList<>();
    private PlayerBoard_View Ludovico;
    private FaithTrack_View faithTrack;
    private Market_View market;
    private Decks_View decks;
    private ArrayList<Marble> chosenMarbles;
    private boolean startGame;

    public void setPlayers(ArrayList<String> players){
        this.players = new ArrayList<>();
        for (String player: players)
            addPlayer(player);
        if(players.size() == 1)
            Ludovico = new PlayerBoard_View("LorenzoIlMagnifico");
        initFaithTrack();
        startGame = false;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void startGame() {
        startGame = true;
    }

    public ArrayList<String> getNickNames(){
        ArrayList<String> nickNames = new ArrayList<>();
        for(PlayerBoard_View player: players)
            nickNames.add(player.getNickName());
        if(players.size() == 1)
            nickNames.add("LorenzoIlMagnifico");
        return nickNames;
    }

    public int getNumOfPlayers(){
        return players.size();
    }

    public PlayerBoard_View getPlayer(int player){
        return players.get(player);
    }

    public String getNickname(int player){
        return players.get(player).getNickName();
    }

    public void addPlayer(String newPlayer){
        players.add(new PlayerBoard_View(newPlayer));
    }

    public FaithTrack_View getFaithTrack() {
        return faithTrack;
    }

    public Market_View getMarket() {
        return market;
    }

    public Decks_View getDecks() {
        return decks;
    }

    public Warehouse_Strongbox_View getWarehouseStrongbox(int viewID){
        return players.get(viewID).getWarehouse_strongbox();
    }

    public Cards_Slots_View getSlotCards(int viewID){
        return players.get(viewID).getCards_slots();
    }

    public void setMarket(Market_View market){
        this.market = market;
    }

    public void setFirstDeckCards(ArrayList<Integer> cards){
        decks = new Decks_View(cards);
    }

    public void initFaithTrack(){
        if(players.size() > 1)
            faithTrack = new FaithTrack_View(players.size(), getNickNames());
        else
            faithTrack = new FaithTrack_View(2, getNickNames());
    }

    public void increaseFaithPoints(int viewID, int faithPoints){
        faithTrack.increaseFaithPoints(viewID, faithPoints);
    }

    public void increaseVictoryPoints(int viewID, int victoryPoints){
        faithTrack.increaseVictoryPoints(viewID, victoryPoints);
    }

    public void setChosenMarbles(ArrayList<Marble> marbles){
        chosenMarbles = marbles;
    }

    public ArrayList<Marble> getChosenMarbles(){
        return chosenMarbles;
    }

    public void slideRow(int selectedRow){
        market.slideRow(selectedRow);
    }

    public void slideColumn(int selectedColumn) {
        market.slideColumn(selectedColumn);
    }

    public void replaceCard(int row, int column, int cardID){
        decks.replaceCard(row, column, cardID);
    }

    public int[] get_Row_Column(int cardID){
        return decks.get_Row_Column(cardID);
    }

    public String getNickName(int viewID) {
        return players.get(viewID).getNickName();
    }

    public void increaseWarehouse(int viewID, Resource resource, int depot){
        players.get(viewID).increaseWarehouse(resource, depot-1);
    }

    public void newAmount(int viewID, Resource resource, int warehouseAmount, int strongboxAmount){
        players.get(viewID).newAmount(resource, warehouseAmount, strongboxAmount);
    }

    public void switchDepot(int viewID, int depot1, int depot2) {
        players.get(viewID).switchDepot(depot1-1, depot2-1);
    }

    public void addExtraDepot(int viewID, Resource r){
        players.get(viewID).addExtraDepot(r);
    }

    public void setMyLeaderCards(int viewID, int firstLeaderCard, int secondLeaderCard){
        players.get(viewID).setLeaderCards(firstLeaderCard, secondLeaderCard);
    }

    public ArrayList<Integer> getDevelopmentCards(int viewID){
        return players.get(viewID).getDevelopmentCards();
    }

    public int getLeaderCard(int viedID, int leaderCard){
        return players.get(viedID).getLeaderCard(leaderCard);
    }

    public void addLeaderCard(int viewID, int leaderCard){
        players.get(viewID).addLeaderCard(leaderCard);
    }

    public void addDevelopmentCard(int viewID, int cardID, int slot){
        players.get(viewID).addDevelopmentCard(cardID, slot);
    }

    public void discardLeaderCard(int viewID, int chosenLeaderCard){
        players.get(viewID).discardLeaderCard(chosenLeaderCard);
    }
}
