package it.polimi.ingsw.model.games;

import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.DiscardAction;
import it.polimi.ingsw.model.actions.LorenzoMoveAndShuffleAction;
import it.polimi.ingsw.model.actions.LorenzoTwoMoveAction;
import it.polimi.ingsw.model.cards.developmentCards.Deck;
import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;
import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.player.SimplePlayerBoard;

import java.util.ArrayList;

/**
 * SinglePlayerGame is main class which handle all different phases of a match if player has chosen to play alone.
 */
public class SinglePlayerGame extends Game implements LightSinglePlayerGame {

    private ArrayList<Action> actions = new ArrayList<>();
    private final SimplePlayerBoard LorenzoIlMagnifico = new SimplePlayerBoard();


    /**
     * the constructor calls the super method with @param numOfPlayers = 1. Then create and shuffle the 7 actions.
     */
    public SinglePlayerGame() {
        super(1);
        prepareActions();
    }

    public SimplePlayerBoard getLorenzoIlMagnifico() {
        return LorenzoIlMagnifico;
    }

    /**
     * this method creates the 7 actions of SinglePlayerGame, add to actions and shuffle them.
     */
    private void prepareActions(){
        Action action1 = new DiscardAction(Color.BLUE);
        Action action2 = new DiscardAction(Color.GREEN);
        Action action3 = new DiscardAction(Color.YELLOW);
        Action action4 = new DiscardAction(Color.PURPLE);
        Action action5 = new LorenzoMoveAndShuffleAction();
        Action action6 = new LorenzoTwoMoveAction();
        Action action7 = new LorenzoTwoMoveAction();
        actions.add(action1);
        actions.add(action2);
        actions.add(action3);
        actions.add(action4);
        actions.add(action5);
        actions.add(action6);
        actions.add(action7);
        shuffleActions();
    }

    @Override
    public void nextPlayer() {
        triggerFirstAction();
    }

    /**
     * this method trigger the first action.
     */
    public void triggerFirstAction() {
        actions.get(0).actionTrigger(this);
    }

    /**
     * this method move the first action to last position.
     */
    @Override
    public void moveToLastAction(){
        Action firstToLastAction = actions.remove(0);
        actions.add(firstToLastAction);
    }

    /**
     * this method shuffle actions, creating a new ArrayList</Action>.
     * for each action remove a random one from actions and add to newActions, then set actions as newActions.
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
     * @param faithPoints is the amount of faith points given by action.
     * this method increase ludovico faith points and then looks if ludovico reached the pope space and if true
     * check it out if player is in the vatican section and increase his victory points.
     */
    @Override
    public void LorenzoFaithTrackMovement(int faithPoints){
        LorenzoIlMagnifico.increaseFaithPoints(faithPoints);
        if (getFaithTrack().reachPope(LorenzoIlMagnifico.getFaithPoints())) {
            getFaithTrack().victoryPointsVaticanReport(getPlayer(0).getVictoryPoints(),getPlayer(0).getFaithPoints());
            getFaithTrack().DecreaseRemainingPope();
        }
    }

    /**
     * @param color stands for the kind of DevelopmentCards to discard from his deck.
     * this method receives the deck and remove the card if possible otherwise it asks a new deck and remove the card.
     * If during discarding there are not anymore DevelopmentCards, method do nothing.
     */
    @Override
    public void discardDeckDevelopmentCards(Color color){
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

    /**
     * @return true if SinglePlayerGame is ended. It could be ended if player has 7 DevelopmentCards, or one between
     * player and Ludovico has reached the end of the FaithTrack, or there are zero remaining DevelopmentCards of one color.
     */
    @Override
    public boolean isEndGame(){
        return getCurrentPlayer().haveSevenDevelopmentCards() || getFaithTrack().zeroRemainingPope() || zeroRemainingColorCards();
    }

    /**
     * @return player if he win the SinglePlayerGame
     */
    @Override
    public int endGame() {
        if(getCurrentPlayer().haveSevenDevelopmentCards() || getCurrentPlayer().getFaithPoints() >= 20)
            return 0;
        else
            return 1;
    }
}
