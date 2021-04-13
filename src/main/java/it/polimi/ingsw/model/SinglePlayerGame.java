package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

import java.util.ArrayList;

public class SinglePlayerGame extends Game implements LightSinglePlayerGame{

    private ArrayList<Action> actions = new ArrayList<>();
    private SimplePlayerBoard ludovico;

    public SinglePlayerGame() {
        super(1);
    }

    @Override
    public void startGame() {
        prepareActions();
    }

    /**
     * this method creates the 7 actions of SinglePlayerGame, add to actions and shuffle them
     */
    public void prepareActions(){
        Action action1 = new DiscardAction(Color.BLUE);
        Action action2 = new DiscardAction(Color.GREEN);
        Action action3 = new DiscardAction(Color.YELLOW);
        Action action4 = new DiscardAction(Color.PURPLE);
        Action action5 = new LudovicoMoveAndShuffleAction();
        Action action6 = new LudovicoTwoMoveAction();
        Action action7 = new LudovicoTwoMoveAction();
        actions.add(action1);
        actions.add(action2);
        actions.add(action3);
        actions.add(action4);
        actions.add(action5);
        actions.add(action6);
        actions.add(action7);
        shuffleActions();
    }

    /**
     * method trigger the first action. If after the trigger, there zero remaining cards of one color, or player has seven cards,
     * or someone have reached the last pope space, calls endGame()
     * @throws EmptyDevelopmentCardDeckException by signature of actionTrigger()
     */
    public void triggerFirstAction() throws EmptyDevelopmentCardDeckException {
        actions.get(0).actionTrigger(this);
        if(zeroRemainingColorCards() || getFaithTrack().zeroRemainingPope() || getPlayer(0).haveSevenDevelopmentCards())
            endGame();
    }

    /**
     * this method move the first action to last position
     */
    @Override
    public void moveToLastAction(){
        Action firstToLastAction = actions.remove(0);
        actions.add(firstToLastAction);
    }

    /**
     * this method creates a new ArrayList</Action> and for each action remove a random one from actions and add to newActions.
     * then set actions as newActions
     */
    @Override
    public void shuffleActions(){
        ArrayList<Action> newActions = new ArrayList<>();
        for(int count = 7; count > 0; count--){
            int i = (int) (Math.random() * count);
            Action action = actions.remove(i);
            newActions.add(action);
        }
        actions = newActions;
    }

    /**
     * @param faithPoints is the amount of faith points given by action
     * this method increase ludovico faith points and then looks if ludovico reached the pope space and if true
     * check it out if player is in the vatican section and increase his victory points
     */
    @Override
    public void LudovicoFaithTrackMovement(int faithPoints){
        ludovico.increaseFaithPoints(faithPoints);
        if (getFaithTrack().reachPope(ludovico.getFaithPoints()))
        {
            getFaithTrack().victoryPointsVaticanReport(getPlayer(0).getVictoryPoints(),getPlayer(0).getFaithPoints());
        }
    }

    /**
     * @param color stands for the kind of DevelopmentCards to discard from his deck
     * this method receives the deck and remove the card if possible otherwise it asks a new deck and remove the card.
     * If during discarding there are not anymore DevelopmentCards, method do nothing.
     */
    @Override
    public void discardDeckDevelopmentCards(Color color) throws EmptyDevelopmentCardDeckException {
        Deck colorDeck = getColorDeck(color);
        int count=0;
        int eccCount=0;

        while (count<2 && eccCount<2)
        {
            try{
                colorDeck.removeDevelopmentCard();
                count++;
            }catch (EmptyDevelopmentCardDeckException e){
                colorDeck=getColorDeck(color);
                eccCount++;
            }
        }
    }

    @Override
    public SimplePlayerBoard endGame() {
        if(getCurrentPlayer().haveSevenDevelopmentCards() || getCurrentPlayer().getFaithPoints() >= 20)
            return getCurrentPlayer();
        else
            return ludovico;
    }
}
