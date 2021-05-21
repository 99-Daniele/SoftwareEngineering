package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class Game_View {

    private ArrayList<PlayerBoard_View> players = new ArrayList<>();
    private FaithTrack_View faithTrack;
    private Market_View market;
    private Decks_View decks;

    public void setPlayers(ArrayList<String> players){
        this.players = new ArrayList<>();
        for (String player: players)
            addPlayer(player);
        initFaithTrack();
    }

    public int getNumOfPlayers(){
        return players.size();
    }

    public String getNickname(int player){
        return players.get(player).getNickName();
    }

    public void addPlayer(String newPlayer){
        players.add(new PlayerBoard_View(newPlayer));
    }

    public void removePlayer(String deletedPlayer){
        players.removeIf(player -> player.getNickName().equals(deletedPlayer));
    }

    public void setMarket(Market_View market){
        this.market = market;
    }

    public void setFirstDeckCards(ArrayList<Integer> cards){
        decks = new Decks_View(cards);
    }

    public ArrayList<Integer> getFirstDeckCards(){
        return decks.getDevelopmentCards();
    }

    public void initFaithTrack(){
        faithTrack = new FaithTrack_View(players.size());
    }

    public void increaseFaithPoints(int viewID, int faithPoints){
        faithTrack.increaseFaithPoints(viewID, faithPoints);
    }

    public void slideRow(int selectedRow){
        market.slideRow(selectedRow);
    }

    public void slideColumn(int selectedColumn) {
        market.slideColumn(selectedColumn);
    }

    public void getRowMarbles(int row){market.getRowMarbles(row);}

    public void getColumnMarbles(int column){market.getColumnMarbles(column);}

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
        players.get(viewID).increaseWarehouse(resource, depot);
    }

    public void newAmount(int viewID, Resource resource, int warehouseAmount, int strongboxAmount){
        players.get(viewID).newAmount(resource, warehouseAmount, strongboxAmount);
    }

    public void switchDepot(int viewID, int depot1, int depot2) {
        players.get(viewID).switchDepot(depot1, depot2);
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

    public int getMyLeaderCard(int viedID, int leaderCard){
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

    public void setVictoryPoints(int viewID, int victoryPoints){
        players.get(viewID).setVictoryPoints(victoryPoints);
    }
}
