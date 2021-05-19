package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.cards.developmentCards.*;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.model.market.*;
import it.polimi.ingsw.parser.*;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import it.polimi.ingsw.network.messages.*;

/**
 * Game is main class which handle all different phases of a match.
 */
public class Game extends Observable implements LightGame{

    private ArrayList<PlayerBoard> players = new ArrayList<>();
    private final Deck[][] deck = new Deck[3][4];
    private final Market market;
    private final FaithTrack faithTrack;
    private final int numOfPlayers;
    private int currentPlayer;
    private ArrayList<LeaderCard> leaderCards;

    /**
     * @param numOfPlayers is the chosen number of players.
     */
    public Game(int numOfPlayers){
        createDecks();
        createLeaderCards();
        market = new Market();
        faithTrack = new FaithTrack();
        this.numOfPlayers = numOfPlayers;
        this.currentPlayer = 0;
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    /**
     * this method method creates all 12 decks and all 48 DevelopmentCards parsing by Json file.
     * then add each card to is correct deck and prepare all decks.
     */
    private void createDecks(){
        deck[0][0]=new Deck(Color.GREEN,1);
        deck[0][1]=new Deck(Color.PURPLE,1);
        deck[0][2]=new Deck(Color.BLUE,1);
        deck[0][3]=new Deck(Color.YELLOW,1);
        deck[1][0]=new Deck(Color.GREEN,2);
        deck[1][1]=new Deck(Color.PURPLE,2);
        deck[1][2]=new Deck(Color.BLUE,2);
        deck[1][3]=new Deck(Color.YELLOW,2);
        deck[2][0]=new Deck(Color.GREEN,3);
        deck[2][1]=new Deck(Color.PURPLE,3);
        deck[2][2]=new Deck(Color.BLUE,3);
        deck[2][3]=new Deck(Color.YELLOW,3);
        DevelopmentCardsParser developmentCardsParser = new DevelopmentCardsParser();
        DevelopmentCard[] developmentCards = developmentCardsParser.getDevelopmentCards();
        int i = 0;
        for(int j = 0; j < 3; j++){
            for (int k = 0; k < 4; k++){
                for(int h = 0; h < 4; h++) {
                    deck[j][k].addDevelopmentCard(developmentCards[i]);
                    i++;
                }
                deck[j][k].prepareDeck();
            }
        }
    }

    private void createLeaderCards(){
        LeaderCardsParser leaderCardsParser = new LeaderCardsParser();
        leaderCards = leaderCardsParser.getLeaderCards();
    }

    /**
     * @param nickname is player chosen nickname.
     * @throws AlreadyTakenNicknameException if @param nickname is already taken by another player.
     */
    public void createPlayer(String nickname) throws AlreadyTakenNicknameException {
        if(isNicknameTaken(nickname))
            throw new AlreadyTakenNicknameException();
        PlayerBoard player = new PlayerBoard(nickname);
        players.add(player);
        Message m = new Message_One_Parameter_String(MessageType.NEW_PLAYER,players.size() -1, nickname);
        setChanged();
        notifyObservers(m);
        if(players.size() == numOfPlayers) {
            shufflePlayers();
            notifyMarket();
            notifyDeckCards();
        }
    }

    public void notifyMarket(){
        Message m = new Message_Market(MessageType.MARKET, currentPlayer, market);
        setChanged();
        notifyObservers(m);
    }

    public void notifyDeckCards(){
        ArrayList<Integer> currentDeckCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                DevelopmentCard card = null;
                try {
                    card = deck[i][j].getFirstCard();
                } catch (EmptyDevelopmentCardDeckException e) {
                    e.printStackTrace();
                }
                currentDeckCards.add(card.getCardID());
            }
        }
        Message m = new Message_ArrayList_Int(MessageType.DECKBOARD, currentPlayer, currentDeckCards);
        setChanged();
        notifyObservers(m);
    }

    public void deletePlayer(String nickName){
        PlayerBoard quitPlayer = null;
        for (PlayerBoard player: players) {
            if (player.getNickname().equals(nickName))
                quitPlayer = player;
        }
        if(quitPlayer == null)
            return;
        try {
            players.remove(quitPlayer);
        }catch (IndexOutOfBoundsException | NullPointerException e){
            e.printStackTrace();
        }
    }

    private void shufflePlayers(){
        ArrayList<PlayerBoard> newPlayersPosition = new ArrayList<>();
        for(int count = players.size(); count > 0; count --){
            int i = (int) (Math.random() * count);
            PlayerBoard player = players.remove(i);
            newPlayersPosition.add(player);
        }
        players = newPlayersPosition;
    }

    /**
     * @param nickname is player chosen nickname.
     * @return true if @param nickname is already taken by another player.
     */
    private boolean isNicknameTaken(String nickname){
        for(PlayerBoard player: players)
            if(player.getNickname().equals(nickname))
                return true;
        return false;
    }

    public String getPlayerPosition(int position){
        return players.get(position).getNickname();
    }

    /**
     * @return FaithTrack of game.
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ArrayList<String> getPlayersNickname(){
        ArrayList<String> nickNames = new ArrayList<>();
        for (PlayerBoard player: players)
            nickNames.add(player.getNickname());
        return nickNames;
    }

    /**
     * @return arraylist of 4 casual LeaderCards.
     */
    public synchronized ArrayList<LeaderCard> casualLeaderCards() {
        ArrayList<LeaderCard> NewLeaderCards=new ArrayList<>();
        for(int count=0; count<4; count++)
        {
            int i = (int) (Math.random() * leaderCards.size());
            NewLeaderCards.add(leaderCards.remove(i));
        }
        return NewLeaderCards;
    }

    /**
     * @param card1 is the LeaderCard chosen to be added to one player.
     * @param card2 is the LeaderCard chosen to be added to one player.
     * @param player is the player which has chosen his leader cards
     */
    public void selectPlayerLeaderCards(LeaderCard card1, LeaderCard card2, int player){
        players.get(player).addLeaderCard(card1, card2);
    }

    /**
     * shift current player to his subsequent or return to first player.
     */
    public void nextPlayer() {
        Message m = new Message(MessageType.END_TURN, currentPlayer);
        if(currentPlayer < numOfPlayers -1)
            currentPlayer++;
        else
            currentPlayer = 0;
        setChanged();
        notifyObservers(m);
    }

    /**
     * @param isRow is true if current player has selected a market row, is false if current player has selected a market column.
     * @param choice is current player's choice of market row or column.
     * @throws WrongParametersException if current player has inserted wrong parameters.
     * this method return marbles selected by players and ask the order to play them.
     * at the end if there are any discarded marbles, increase faith points of other players by the amount of them and
     * calls fatihTrackMovementExceptCurrentPlayer() method.
     */
    public Marble[] takeMarketMarble(boolean isRow, int choice) throws WrongParametersException {
        Marble [] marbles;
        if(isRow) {
            marbles = market.getRowMarbles(choice);
            market.slideRow(choice);
        }
        else {
            marbles = market.getColumnMarbles(choice);
            market.slideColumn(choice);
        }
        Message message_two_parameter_int;
        if(isRow)
            message_two_parameter_int=new Message_Two_Parameter_Int(MessageType.MARKET_CHANGE,currentPlayer+1,0,choice);
        else
            message_two_parameter_int = new Message_Two_Parameter_Int(MessageType.MARKET_CHANGE,currentPlayer+1,1,choice);
        setChanged();
        notifyObservers(message_two_parameter_int);
        return marbles;
    }

    /**
     * @param resource stands for the type of resource to increase by 1 in Warehouse.
     * @return false if @param resource has to be discarded.
     */
    @Override
    public boolean increaseWarehouse(Resource resource) {
        int depot = players.get(currentPlayer).increaseWarehouse(resource);
        Message message_one_int_one_resource;
        if(depot != -1)
            message_one_int_one_resource=new Message_One_Int_One_Resource(MessageType.INCREASE_WAREHOUSE,currentPlayer+1,resource, depot+1);
        else
            message_one_int_one_resource = new Message_One_Int_One_Resource(MessageType.INCREASE_WAREHOUSE, currentPlayer+1, resource, -1);
        setChanged();
        notifyObservers(message_one_int_one_resource);
        return depot != -1;
    }

    public void firstIncreaseWarehouse(Resource resource, int player){
        Message message_one_int_one_resource=new Message_One_Int_One_Resource(MessageType.INCREASE_WAREHOUSE,player+1,resource, 1);
        players.get(player).increaseWarehouse(resource);
        setChanged();
        notifyObservers(message_one_int_one_resource);
        if(player == 2) {
            players.get(player).increaseFaithPoints(1);
            Message message_one_parameter_int = new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE, player + 1, 1);
            setChanged();
            notifyObservers(message_one_parameter_int);
        }
    }

    public void firstDoubleIncreaseWarehouse(Resource resource1, Resource resource2){
        Message message_one_int_one_resource;
        if(resource1 == resource2)
         message_one_int_one_resource=new Message_One_Int_One_Resource(MessageType.INCREASE_WAREHOUSE,4, resource1, 2);
        else
            message_one_int_one_resource = new Message_One_Int_One_Resource(MessageType.INCREASE_WAREHOUSE, 4, resource1, 1);
        players.get(3).increaseWarehouse(resource1);
        setChanged();
        notifyObservers(message_one_int_one_resource);
        if(resource1 == resource2){
            try {
                players.get(3).switchDepots(0, 1);
            } catch (ImpossibleSwitchDepotException e) {
                e.printStackTrace();
            }
        }
        message_one_int_one_resource=new Message_One_Int_One_Resource(MessageType.INCREASE_WAREHOUSE,4, resource2, 2);
        players.get(3).increaseWarehouse(resource2);
        setChanged();
        notifyObservers(message_one_int_one_resource);
        players.get(3).increaseFaithPoints(1);
        Message message_one_parameter_int = new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE, 4, 1);
        setChanged();
        notifyObservers(message_one_parameter_int);
    }

    /**
     * this method increase all players except current faith points by 1.
     */
    @Override
    public void increaseOneFaithPointOtherPlayers() {
        for(int i = 0; i < numOfPlayers; i++) {
            if (i != currentPlayer) {
                players.get(i).increaseFaithPoints(1);
                faithPointsIncreaseNotify(i);
            }
        }
    }

    private void faithPointsIncreaseNotify(int i){
        Message message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,i+1, players.get(i).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int);
    }

    /**
     * method that sets the current player's victory points related to the position in FaithTrack and controls if
     * the player is in or beyond the pope space.
     * if it's true it increases the victory points of all the players in the vatican section.
     */
    @Override
    public void faithTrackMovement(){
        faithTrack.victoryPointsFaithTrack(players.get(currentPlayer).getVictoryPoints(), players.get(currentPlayer).getFaithPoints());
        if(faithTrack.reachPope(players.get(currentPlayer).getFaithPoints())){
            for(int i = 0; i < players.size(); i++){
                faithTrack.victoryPointsVaticanReport(players.get(i).getVictoryPoints(), players.get(i).getFaithPoints());
                faithTrackNotify(i);
            }
            faithTrack.DecreaseRemainingPope();
        }
    }

    public void faithTrackNotify(int i){
        int victoryPoints = players.get(i).getVictoryPoints().getVictoryPointsByVaticanReport();
        Message m = new Message_Two_Parameter_Int(MessageType.VATICAN_REPORT, i, currentPlayer +1, victoryPoints);
        setChanged();
        notifyObservers(m);
    }

    /**
     * this method increase current player faith points by 1.
     */
    @Override
    public void increaseOneFaithPointCurrentPlayer() {
        players.get(currentPlayer).increaseFaithPoints(1);
        Message message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1, players.get(currentPlayer).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int);
    }

    /**
     * @param chosenLeaderCard is one player's LeaderCard.
     * @return true if the chosen LeaderCard is an active WhiteConversionCard.
     */
    @Override
    public boolean isActiveWhiteConversionCard(int chosenLeaderCard) {
        return players.get(currentPlayer).isWhiteConversionLeaderCardActive(chosenLeaderCard);
    }

    /**
     * @param chosenLeaderCard is one player's LeaderCard.
     * @return current player chosen LeaderCard.
     * @throws AlreadyDiscardLeaderCardException if chosen LeaderCard was previously discarded.
     */
    @Override
    public LeaderCard getCurrentPlayerLeaderCard(int chosenLeaderCard) throws AlreadyDiscardLeaderCardException {
        return players.get(currentPlayer).getLeaderCard(chosenLeaderCard);
    }

    /**
     * @return the two actives LeaderCards of current player.
     * @throws AlreadyDiscardLeaderCardException if one or both chosen LeaderCards were previously discarded.
     */
    public LeaderCard[] getCurrentPlayerActiveLeaderCards() throws AlreadyDiscardLeaderCardException {
        LeaderCard[] currentPlayerLeaderCards = new LeaderCard[2];
        currentPlayerLeaderCards[0] = players.get(currentPlayer).getLeaderCard(1);
        currentPlayerLeaderCards[1] = players.get(currentPlayer).getLeaderCard(2);
        return currentPlayerLeaderCards;
    }

    /**
     * @param leaderCard is the chosen WhiteConversionCard to convert white marble.
     * this method try to increase current player warehouse by 1 LeaderCard resource.
     * if it isn't possible increase other players faith points by 1.
     */
    @Override
    public void whiteMarbleConversion(LeaderCard leaderCard){
        if(!(increaseWarehouse(leaderCard.getResource())))
            increaseOneFaithPointOtherPlayers();
    }

    /**
     * method that sets the victory points related to the position in the Faith Track of all the players and controls if
     * the players are in or beyond the pope space.
     * if it's true it increases the victory points of all the players in the vatican section.
     */
    @Override
    public void faithTrackMovementAllPlayers(){
        int flag=0;
        for(PlayerBoard player: players) {
            faithTrack.victoryPointsFaithTrack(player.getVictoryPoints(), player.getFaithPoints());
            if (faithTrack.reachPope(player.getFaithPoints()))
                flag=1;
        }
        if (flag==1) {
            for(int i = 0; i < players.size(); i++){
                faithTrack.victoryPointsVaticanReport(players.get(i).getVictoryPoints(), players.get(i).getFaithPoints());
                faithTrackNotify(i);
            }
            faithTrack.DecreaseRemainingPope();
        }
    }

    /**
     * @param depot1 stands for position of the first Depot in current player Warehouse to switch.
     * @param depot2 stands for position of the second Depot in current player Warehouse to switch.
     * @throws ImpossibleSwitchDepotException if the switch is not possible.
     */
    public void switchDepots(int depot1, int depot2) throws ImpossibleSwitchDepotException {
        players.get(currentPlayer).switchDepots(depot1 -1, depot2 -1);
        Message message_two_parameter_int=new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT,currentPlayer+1,depot1,depot2);
        setChanged();
        notifyObservers(message_two_parameter_int);
    }

    /**
     * @param deckRow is the row of the chosen deck.
     * @param deckColumn is the column of the chosen deck.
     * @return an ArrayList </Integer> of slots where @card can be inserted.
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck.
     */
    public ArrayList<Integer> findAvailableSlots(int deckRow, int deckColumn)
            throws EmptyDevelopmentCardDeckException{
        DevelopmentCard card = deck[deckRow][deckColumn].getFirstCard();
        return players.get(currentPlayer).findAvailableSlot(card);
    }

    /**
     * @param deckRow is the row of the chosen deck.
     * @param deckColumn is the column of the chosen deck.
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased.
     * @param slot is player's choice about in which slot want to insert the chosen card.
     * @throws InsufficientResourceException if player has not enough resources.
     * @throws ImpossibleDevelopmentCardAdditionException if is not possible to add the card in player's slots.
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck.
     * this method firstly verifies if buying is possible and then remove DevelopmentCard from the deck
     * where it was contained.
     */
    public void buyDevelopmentCardFromMarket(int deckRow, int deckColumn, int choice, int slot)
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, EmptyDevelopmentCardDeckException {
        DevelopmentCard card = deck[deckRow][deckColumn].getFirstCard();
        if(!(players.get(currentPlayer).isBuyable(card)))
            throw new InsufficientResourceException();
        players.get(currentPlayer).buyDevelopmentCard(card, slot, choice);
        deck[deckRow][deckColumn].removeDevelopmentCard();
        Message message_two_parameter_int = new Message_Two_Parameter_Int(MessageType.BUY_CARD,currentPlayer+1, card.getCardID(), slot);
        setChanged();
        notifyObservers(message_two_parameter_int);
        Message message;
        if(deck[deckRow][deckColumn].isEmpty()){
            message = new Message_Four_Parameter_Int(MessageType.CARD_REMOVE,currentPlayer+1,deckRow,deckColumn,1,-1);
        }
        else{
            int cardID = deck[deckRow][deckColumn].getFirstCard().getCardID();
            message = new Message_Four_Parameter_Int(MessageType.CARD_REMOVE,currentPlayer+1,deckRow,deckColumn,0, cardID);
        }
        setChanged();
        notifyObservers(message);
        for(Resource resource: Resource.values()) {
            if(card.getResourceCost().getNumOfResource(resource)!=0)
            {
                int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
                int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
                Message message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
                setChanged();
                notifyObservers(message_one_resource_two_int);
            }
        }
    }

    /**
     * @param chosenSlot is the chosen SlotDevelopmentCards to activate last card production power.
     * @param s is a strongbox.
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased.
     * @throws InsufficientResourceException if player has not enough resources to activate all production powers together.
     * @throws NoSuchProductionPowerException if player has chosen an empty SlotDevelopmentCards.
     * this method remove player resources by the amount required by the chosen card and increase @param s by the amount
     * given by card production power.
     */
    public void removeDevelopmentCardProductionResource(int chosenSlot, Strongbox s, int choice)
            throws InsufficientResourceException, NoSuchProductionPowerException {
        players.get(currentPlayer).activateDevelopmentCardProductionPower(chosenSlot, s, choice);
        faithTrackMovement();
        Message message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1,players.get(currentPlayer).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int);
        for(Resource resource: Resource.values()) {
            if(players.get(currentPlayer).getSlotDevelopmentCards(chosenSlot-1).getLastCard().getProductionPowerResourceRequired().getNumOfResource(resource)!=0) {
                int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
                int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
                Message_One_Resource_Two_Int message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
                setChanged();
                notifyObservers(message_one_resource_two_int);
            }
            else
                if (players.get(currentPlayer).getSlotDevelopmentCards(chosenSlot-1).getLastCard().getProductionPowerResourceGiven().getNumOfResource(resource)!=0)
                {
                    int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
                    int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
                    Message_One_Resource_Two_Int message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
                    setChanged();
                    notifyObservers(message_one_resource_two_int);
                }
        }
    }

    /**
     * @param r1 is a resource to be decreased by current player resources.
     * @param r2 is a resource to be decreased by current player resources.
     * @param r3 is a resource to be increased by current player resources.
     * @param s is a strongbox.
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased.
     * @throws InsufficientResourceException if player has not enough resources to activate all production powers together.
     * this method remove player resources by 1 @param r1 and 1 @param r2 and increase @param s by 1 @param r3.
     */
    public void basicProductionPower(Resource r1, Resource r2, Resource r3, Strongbox s, int choice)
            throws InsufficientResourceException {
        players.get(currentPlayer).activateBasicProduction(r1, r2, choice);
        s.increaseResourceType(r3, 1);
        for (Resource resource:Resource.values())
            if(r1==resource|| r2==resource|| r3==resource) {
                int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
                int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
                Message message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
                setChanged();
                notifyObservers(message_one_resource_two_int);
            }
    }

    /**
     * @param chosenAdditionalPowerCard is the chosen AdditionalProductionPowerCard to be activated.
     * @param r is a resource to be increased by current player resources.
     * @param s is a strongbox.
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased.
     * @throws InsufficientResourceException if player has not enough resources to activate all production powers together.
     * @throws NoSuchProductionPowerException if player has chosen a not active or not existing AdditionalProductionPower.
     * this method remove player resources by the 1 resource required by the chosen card and increase @param s by the 1
     * resource given by card production power.
     */
    public void removeAdditionalProductionPowerCardResource(int chosenAdditionalPowerCard, Resource r, Strongbox s, int choice)
            throws InsufficientResourceException, NoSuchProductionPowerException {
        players.get(currentPlayer).activateAdditionalProductionPower(chosenAdditionalPowerCard, choice -1);
        s.increaseResourceType(r, 1);
        faithTrackMovement();
        Message message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1,players.get(currentPlayer).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int);
        LeaderCard leaderCard=null;
        try {
            leaderCard=getCurrentPlayerLeaderCard(chosenAdditionalPowerCard);
        } catch (AlreadyDiscardLeaderCardException e) {
            e.printStackTrace();
        }
        if(r!=leaderCard.getResource()) {
            int par=players.get(currentPlayer).getWarehouse().getNumOfResource(r);
            int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(r);
            Message message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,r,par,par1);
            setChanged();
            notifyObservers(message_one_resource_two_int);
        }
        int par=players.get(currentPlayer).getWarehouse().getNumOfResource(leaderCard.getResource());
        int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(leaderCard.getResource());
        Message message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,leaderCard.getResource(),par,par1);
        setChanged();
        notifyObservers(message_one_resource_two_int);
    }

    /**
     * @param s is a strongbox.
     * this method increase current player strongbox by the amount contained in @param s.
     */
    public void increaseCurrentPlayerStrongbox(Strongbox s){
        players.get(currentPlayer).increaseStrongbox(s);
        for(Resource resource: Resource.values()) {
            int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
            int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
            Message message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
            setChanged();
            notifyObservers(message_one_resource_two_int);
        }
    }

    /**
     * @param chosenLeaderCard is current player's choice about which leader card to activate.
     * @throws InsufficientResourceException if current player has not enough resources.
     * @throws InsufficientCardsException if current player has not enough cards.
     * @throws AlreadyDiscardLeaderCardException if current player already discard this LeaderCard previously.
     * @throws ActiveLeaderCardException if chosen LeaderCard is already active.
     */
    public void activateLeaderCard(int chosenLeaderCard)
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {
        LeaderCard leaderCard = getCurrentPlayerLeaderCard(chosenLeaderCard);
        boolean extraDepot = players.get(currentPlayer).activateLeaderCard(chosenLeaderCard);
        int idcard = leaderCard.getCardID();
        Message message=new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION,currentPlayer+1, idcard);
        setChanged();
        notifyObservers(message);
        if(extraDepot) {
            message = new Message_One_Int_One_Resource(MessageType.EXTRA_DEPOT, currentPlayer + 1, leaderCard.getResource(), 1);
            setChanged();
            notifyObservers(message);
        }
    }

    /**
     * @param chosenLeaderCard is current player's choice about which leader card to discard.
     * @throws AlreadyDiscardLeaderCardException if current player already discard this LeaderCard previously.
     * @throws ActiveLeaderCardException if chosen LeaderCard is already active.
     */
    public void discardLeaderCard(int chosenLeaderCard)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {
        LeaderCard leaderCard = getCurrentPlayerLeaderCard(chosenLeaderCard);
        players.get(currentPlayer).discardLeaderCard(chosenLeaderCard);
        int idcard = leaderCard.getCardID();
        Message message_one_parameter_int=new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, currentPlayer+1, idcard);
        setChanged();
        notifyObservers(message_one_parameter_int);
        Message message_one_parameter_int1=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1,players.get(currentPlayer).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int1);
        faithTrackMovement();
    }

    /**
     * @param color is the color of DevelopmentCards contained in deck.
     * @return the first not empty deck which contains DevelopmentCards of @param color, or the last deck of same kind of cards.
     */
    public Deck getColorDeck(Color color){
        int i = 0;
        while(deck[0][i].getColor() != color)
            i++;
        if (!deck[0][i].isEmpty())
            return deck[0][i];
        if(!deck[1][i].isEmpty())
            return deck[1][i];
        return deck[2][i];
    }

    /**
     * @return true if there are 0 remaining DevelopmentCards of one kind of color. In this case, SinglePlayerGame finishes.
     */
    public boolean zeroRemainingColorCards(){
        for(int i = 0; i < 4; i++) {
            if (deck[0][i].isEmpty() && deck[1][i].isEmpty() && deck[2][i].isEmpty())
                return true;
        }
        return false;

    }

    /**
     * @param row is the row of chosen deck
     * @param column is the column of the chosen deck
     * @return the chosen deck
     */
    public Deck getDeck(int row, int column) {
        return deck[row][column];
    }

    /**
     * @param position the position in the arraylist.
     * @return PlayerBoard in @param position.
     */
    public PlayerBoard getPlayer(int position){
        return players.get(position);
    }

    /**
     * @return PlayerBoard in current position.
     */
    public PlayerBoard getCurrentPlayer(){
        return players.get(currentPlayer);
    }

    /**
     * @return true if the game is ended. It could be ended if one player has 7 DevelopmentCards or has reached the end
     * of the FaithTrack
     */
    public boolean isEndGame(){
        return faithTrack.zeroRemainingPope() || players.get(currentPlayer).haveSevenDevelopmentCards();
    }

    /**
     * @return the position of the winner of the game.
     * this method find the player with more victory points.
     * in case more players have both max victory points, find which one has more amount of resources.
     */
    public int endGame() {
        int maxVictoryPoints = 0;
        int maxNumOfResources = 0;
        int winner = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).sumVictoryPoints() > maxVictoryPoints) {
                maxVictoryPoints = players.get(i).sumVictoryPoints();
                maxNumOfResources = players.get(i).sumTotalResource();
                winner = i;
            }
            else if(players.get(i).sumVictoryPoints() == maxVictoryPoints){
                if(players.get(i).sumTotalResource() > maxNumOfResources){
                    maxNumOfResources = players.get(i).sumTotalResource();
                    winner = i;
                }
            }
        }
        endGameNotify(winner +1, maxVictoryPoints, maxNumOfResources);
        return winner;
    }

    public void endGameNotify(int winner, int maxVictoryPoints, int maxNumOfResources){
        Message endGame = new Message_Two_Parameter_Int(MessageType.END_GAME, winner, maxVictoryPoints, maxNumOfResources);
        setChanged();
        notifyObservers(endGame);
    }

    public void endGameNotify(int winner){
        int maxVictoryPoints = players.get(winner).sumVictoryPoints();
        int maxNumOfResources = players.get(winner).sumTotalResource();
        Message endGame = new Message_Two_Parameter_Int(MessageType.END_GAME, winner+1, maxVictoryPoints, maxNumOfResources);
        setChanged();
        notifyObservers(endGame);
    }

    /**
     * @return currentPlayer'position
     */
    public int getCurrentPosition(){
        return currentPlayer;
    }

    /**
     * @param view is the observer that is added to the observers of game
     */
    public void addObservers(VirtualView view){
        addObserver(view);
    }

    /**
     * @return number of players
     */
    public int getNumOfPlayers(){
        return numOfPlayers;
    }


    public SimplePlayerBoard getLorenzoIlMagnifico() {
        return new SimplePlayerBoard();
    }

    public void triggerFirstAction() {
    }

    public void LorenzoFaithTrackMovement(int faithPoints){
        Message message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,2, faithPoints);
        setChanged();
        notifyObservers(message_one_parameter_int);
    }

    public void discardNotify(Deck colorDeck){
        Message message;
        if(colorDeck.isEmpty()){
            message = new Message_Four_Parameter_Int(MessageType.CARD_REMOVE,2, colorDeck.getRow(),colorDeck.getColumn(),1,-1);
        }
        else{
            int cardID = -1;
            try {
                cardID = colorDeck.getFirstCard().getCardID();
            } catch (EmptyDevelopmentCardDeckException e) {
                e.printStackTrace();
            }
            message = new Message_Four_Parameter_Int(MessageType.CARD_REMOVE,2,colorDeck.getRow(),colorDeck.getColumn(),0, cardID);
        }
        setChanged();
        notifyObservers(message);
    }

}
