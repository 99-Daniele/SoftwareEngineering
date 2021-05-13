package it.polimi.ingsw.model.games;

import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.exceptions.*;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import it.polimi.ingsw.model.developmentCards.*;
import it.polimi.ingsw.model.faithTrack.*;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.market.*;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.*;
import it.polimi.ingsw.network.messages.*;

/**
 * Game is main class which handle all different phases of a match.
 */
public class Game extends Observable implements LightGame{

    private final ArrayList<PlayerBoard> players = new ArrayList<>();
    private final Deck[][] deck = new Deck[3][4];
    private final Market market;
    private final FaithTrack faithTrack;
    private final int numOfPlayers;
    private int currentPlayer;
    private final ArrayList<LeaderCard> leaderCards = new ArrayList<>(16);

    /**
     * @param numOfPlayers is the chosen number of players.
     */
    public Game(int numOfPlayers){
        createDecks();
        market = new Market();
        faithTrack = new FaithTrack();
        this.numOfPlayers = numOfPlayers;
        this.currentPlayer = 0;
        createLeaderCards();
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
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/DevelopmentCards.json"));
            DevelopmentCard[] developmentCards = gson.fromJson(reader, DevelopmentCard[].class);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method creates the list of 16 LeaderCards by parsing Json Files.
     */
    private void createLeaderCards(){
        try {
            Gson gson = new Gson();
            JsonReader reader1 = new JsonReader(new FileReader("src/main/resources/DiscountCards.json"));
            LeaderCard[] discountCards = gson.fromJson(reader1, DiscountCard[].class);
            JsonReader reader2 = new JsonReader(new FileReader("src/main/resources/ExtraDepotCards.json"));
            LeaderCard[] extraDepotCards = gson.fromJson(reader2, ExtraDepotCard[].class);
            JsonReader reader3 = new JsonReader(new FileReader("src/main/resources/WhiteConversionCards.json"));
            LeaderCard[] whiteConversionCards = gson.fromJson(reader3, WhiteConversionCard[].class);
            JsonReader reader4 = new JsonReader(new FileReader("src/main/resources/AdditionalProductionPowerCards.json"));
            LeaderCard[] additionalProductionPowerCards = gson.fromJson(reader4, AdditionalProductionPowerCard[].class);
            leaderCards.addAll(Arrays.asList(discountCards).subList(0, 4));
            leaderCards.addAll(Arrays.asList(extraDepotCards).subList(0, 4));
            leaderCards.addAll(Arrays.asList(whiteConversionCards).subList(0, 4));
            leaderCards.addAll(Arrays.asList(additionalProductionPowerCards).subList(0, 4));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * @return FaithTrack of game.
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
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
     * @param card1 is the LeaderCard chosen to be added to current PlayerBoard.
     * @param card2 is the LeaderCard chosen to be added to current PlayerBoard.
     */
    public void selectCurrentPlayerLeaderCards(LeaderCard card1, LeaderCard card2){
        players.get(currentPlayer).addLeaderCard(card1, card2);
    }

    /**
     * shift current player to his subsequent or return to first player.
     */
    public void nextPlayer() {
        if(currentPlayer < numOfPlayers -1)
            currentPlayer++;
        else
            currentPlayer = 0;
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
        Message_Two_Parameter_Int message_two_parameter_int;
        if(isRow)
            message_two_parameter_int=new Message_Two_Parameter_Int(MessageType.MARKET_CHANGE,currentPlayer+1,1,choice);
        else
            message_two_parameter_int = new Message_Two_Parameter_Int(MessageType.MARKET_CHANGE,currentPlayer+1,0,choice);
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
        Message_One_Int_One_Resource message_one_int_one_resource=new Message_One_Int_One_Resource(MessageType.INCREASE_WAREHOUSE,currentPlayer+1,resource,);
        setChanged();
        notifyObservers(message_one_int_one_resource);
        return players.get(currentPlayer).increaseWarehouse(resource);
    }

    /**
     * this method increase all players except current faith points by 1.
     */
    @Override
    public void increaseOneFaithPointOtherPlayers() {
        for(int i = 0; i < numOfPlayers; i++) {
            if (i != currentPlayer)
            {
                players.get(i).increaseFaithPoints(1);
                Message_One_Parameter_Int message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,i+1, players.get(i).getFaithPoints());
                setChanged();
                notifyObservers(message_one_parameter_int);
            }
        }
    }

    /**
     * method that sets the current player's victory points related to the position in FaithTrack and controls if
     * the player is in or beyond the pope space.
     * if it's true it increases the victory points of all the players in the vatican section.
     */
    @Override
    public void faithTrackMovement(){
        faithTrack.victoryPointsFaithTrack(players.get(currentPlayer).getVictoryPoints(), players.get(currentPlayer).getFaithPoints());
        if(faithTrack.reachPope(players.get(currentPlayer).getFaithPoints()))
        {
            for(PlayerBoard player:players)
            {
                faithTrack.victoryPointsVaticanReport(player.getVictoryPoints(), player.getFaithPoints());
            }
            faithTrack.DecreaseRemainingPope();
        }
    }

    /**
     * this method increase current player faith points by 1.
     */
    @Override
    public void increaseOneFaithPointCurrentPlayer() {
        players.get(currentPlayer).increaseFaithPoints(1);
        Message_One_Parameter_Int message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1, players.get(currentPlayer).getFaithPoints());
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
    public void faithTrackMovementAllPlayers(){
        int flag=0;
        for(PlayerBoard player: players) {
            faithTrack.victoryPointsFaithTrack(player.getVictoryPoints(), player.getFaithPoints());
            if (faithTrack.reachPope(player.getFaithPoints()))
                flag=1;
        }
        if (flag==1) {
            for (PlayerBoard player: players)
                faithTrack.victoryPointsVaticanReport(player.getVictoryPoints(), player.getFaithPoints());
            faithTrack.DecreaseRemainingPope();
        }
    }

    /**
     * @return a list of available switches for current player.
     */
    public ArrayList<Integer[]> availableSwitches(){
        return players.get(currentPlayer).availableSwitches();
    }

    /**
     * @param depot1 stands for position of the first Depot in current player Warehouse to switch.
     * @param depot2 stands for position of the second Depot in current player Warehouse to switch.
     * @throws ImpossibleSwitchDepotException if the switch is not possible.
     */
    public void switchDepots(int depot1, int depot2) throws ImpossibleSwitchDepotException {
        players.get(currentPlayer).switchDepots(depot1, depot2);
        Message_Two_Parameter_Int message_two_parameter_int=new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT,currentPlayer+1,depot1,depot2);
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
     * @param card is a DevelopmentCard bought by current player.
     * @return a list of available slots to insert @param card.
     */
    public ArrayList<Integer> findAvailableSlots(DevelopmentCard card){
        return players.get(currentPlayer).findAvailableSlot(card);
    }

    /**
     * @return a list of DevelopmentCard buyable by current player.
     */
    public ArrayList<DevelopmentCard> buyableDevelopmentCards(){
        ArrayList<DevelopmentCard> buyableCards = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                try {
                    DevelopmentCard card = deck[i][j].getFirstCard();
                    if (players.get(currentPlayer).isBuyable(card))
                        buyableCards.add(card);
                } catch (EmptyDevelopmentCardDeckException e) { }
            }
        }
        return buyableCards;
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


        //modificare id carta
        Message_Two_Parameter_Int message_two_parameter_int=new Message_Two_Parameter_Int(MessageType.BUY_CARD,currentPlayer+1,5,slot);
        setChanged();
        notifyObservers(message_two_parameter_int);
        int isEmpty;
        int idCard;
        if(deck[deckRow][deckColumn].isEmpty()){
            isEmpty=1;
            idCard=-1;
        }
        else{
            isEmpty=0;
            idCard=5;//modificare
        }
        Message_Four_Parameter_Int message_four_parameter_int=new Message_Four_Parameter_Int(MessageType.CARD_REMOVE,currentPlayer+1,deckRow,deckColumn,isEmpty,idCard);
        setChanged();
        notifyObservers(message_four_parameter_int);
        for(Resource resource: Resource.values())
        {
            if(card.getResourceCost().getNumOfResource(resource)!=0)
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
     * @param card is a DevelopmentCard bought by current player.
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased.
     * @param slot is player's choice about in which slot want to insert the chosen card.
     */
    public void buyDevelopmentCard(DevelopmentCard card, int choice, int slot){
        try {
            players.get(currentPlayer).buyDevelopmentCard(card, slot, choice);
        } catch (InsufficientResourceException | ImpossibleDevelopmentCardAdditionException e) {  }



        int [] pos=removeDevelopmentCard(card);
        //modificare id carta
        Message_Two_Parameter_Int message_two_parameter_int=new Message_Two_Parameter_Int(MessageType.BUY_CARD,currentPlayer+1,5,slot);
        setChanged();
        notifyObservers(message_two_parameter_int);
        int correct;
        int idCard;
        if(deck[pos[0]][pos[1]].isEmpty()){
            correct=1;
            idCard=-1;
        }
        else{
            correct=0;
            idCard=5;//modificare
        }
        Message_Four_Parameter_Int message_four_parameter_int=new Message_Four_Parameter_Int(MessageType.CARD_REMOVE,currentPlayer+1,pos[0],pos[1],correct,idCard);
        setChanged();
        notifyObservers(message_four_parameter_int);
        for(Resource resource: Resource.values())
        {
            if(card.getResourceCost().getNumOfResource(resource)!=0)
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
     * @param card is DevelopmentCard to be removed by his deck.
     */
    private int[] removeDevelopmentCard(DevelopmentCard card){
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 4; j++){
                if(deck[i][j].getColor() == card.getColor() && i == card.getLevel()-1) {
                    try {
                        deck[i][j].removeDevelopmentCard();
                        int [] pos={i,j};
                        return pos;
                    } catch (EmptyDevelopmentCardDeckException e) { }
                }
            }
        }
        return new int[2];
    }

    /**
     * @param choice summarize all player's choices about which production powers activate.
     * @throws InsufficientResourceException if player has not enough resources to activate all production powers together.
     * this method activate all production powers together and if player has increased his faith points, calls faithTrackMovement().
     */
    public void activateProduction(PowerProductionPlayerChoice choice)
            throws InsufficientResourceException, NoSuchProductionPowerException {
        if(players.get(currentPlayer).activateProduction(choice))
        {
            faithTrackMovement();
            Message_One_Parameter_Int message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1,players.get(currentPlayer).getFaithPoints());
            setChanged();
            notifyObservers(message_one_parameter_int);
        }
        for(Resource resource: Resource.values())
        {
            int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
            int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
            Message_One_Resource_Two_Int message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
            setChanged();
            notifyObservers(message_one_resource_two_int);
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
        Message_One_Parameter_Int message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1,players.get(currentPlayer).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int);
        for(Resource resource: Resource.values())
        {
            if(players.get(currentPlayer).getSlotDevelopmentCards(chosenSlot-1).getLastCard().getProductionPowerResourceRequired().getNumOfResource(resource)!=0)
            {
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
            if(r1==resource|| r2==resource|| r3==resource)
            {
                int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
                int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
                Message_One_Resource_Two_Int message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
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
        players.get(currentPlayer).activateAdditionalProductionPower(chosenAdditionalPowerCard, choice);
        s.increaseResourceType(r, 1);
        faithTrackMovement();
        Message_One_Parameter_Int message_one_parameter_int=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1,players.get(currentPlayer).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int);
        LeaderCard leaderCard=null;
        try {
            leaderCard=getCurrentPlayerLeaderCard(chosenAdditionalPowerCard);
        } catch (AlreadyDiscardLeaderCardException e) {
            e.printStackTrace();
        }
        if(r!=leaderCard.getResource())
        {
            int par=players.get(currentPlayer).getWarehouse().getNumOfResource(r);
            int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(r);
            Message_One_Resource_Two_Int message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,r,par,par1);
            setChanged();
            notifyObservers(message_one_resource_two_int);
        }
        int par=players.get(currentPlayer).getWarehouse().getNumOfResource(leaderCard.getResource());
        int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(leaderCard.getResource());
        Message_One_Resource_Two_Int message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,leaderCard.getResource(),par,par1);
        setChanged();
        notifyObservers(message_one_resource_two_int);
    }

    /**
     * @param s is a strongbox.
     * this method increase current player strongbox by the amount contained in @param s.
     */
    public void increaseCurrentPlayerStrongbox(Strongbox s){
        players.get(currentPlayer).increaseStrongbox(s);
        for(Resource resource: Resource.values())
        {
            int par=players.get(currentPlayer).getWarehouse().getNumOfResource(resource);
            int par1=players.get(currentPlayer).getStrongbox().getNumOfResource(resource);
            Message_One_Resource_Two_Int message_one_resource_two_int = new Message_One_Resource_Two_Int(MessageType.RESOURCE_AMOUNT, currentPlayer + 1,resource,par,par1);
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
        players.get(currentPlayer).activateLeaderCard(chosenLeaderCard );
        LeaderCard leaderCard=null;
        try {
            leaderCard=getCurrentPlayerLeaderCard(chosenLeaderCard);
        } catch (AlreadyDiscardLeaderCardException e) {
            e.printStackTrace();
        }
        Message_One_Parameter_Int message_one_parameter_int=new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION,currentPlayer+1, idcard);
        setChanged();
        notifyObservers(message_one_parameter_int);
    }

    /**
     * @param chosenLeaderCard is current player's choice about which leader card to discard.
     * @throws AlreadyDiscardLeaderCardException if current player already discard this LeaderCard previously.
     * @throws ActiveLeaderCardException if chosen LeaderCard is already active.
     */
    public void discardLeaderCard(int chosenLeaderCard)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {
        players.get(currentPlayer).discardLeaderCard(chosenLeaderCard);
        faithTrackMovement();
        LeaderCard leaderCard=null;
        try {
            leaderCard=getCurrentPlayerLeaderCard(chosenLeaderCard);
        } catch (AlreadyDiscardLeaderCardException e) {
            e.printStackTrace();
        }
        Message_One_Parameter_Int message_one_parameter_int=new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, currentPlayer+1, idcard);
        setChanged();
        notifyObservers(message_one_parameter_int);
        Message_One_Parameter_Int message_one_parameter_int1=new Message_One_Parameter_Int(MessageType.FAITH_POINTS_INCREASE,currentPlayer+1,players.get(currentPlayer).getFaithPoints());
        setChanged();
        notifyObservers(message_one_parameter_int1);
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
        Message endGame = new Message_Two_Parameter_Int(MessageType.END_GAME, winner +1, maxVictoryPoints, maxNumOfResources);
        setChanged();
        notifyObservers(endGame);
        return winner;
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

    public void triggerFirstAction() {return;}

    public void moveToLastAction(){return;}

    public void shuffleActions(){return;}

    public void LorenzoFaithTrackMovement(int faithPoints){return;}

    public void discardDeckDevelopmentCards(Color color){return;}

}
