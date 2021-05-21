package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.parser.CardMap;

import java.util.ArrayList;
import java.util.LinkedList;

public class Cards_Slots_View {

    private LinkedList<Integer> firstSlot;
    private LinkedList<Integer> secondSlot;
    private LinkedList<Integer> thirdSlot;
    private int firstLeaderCard;
    private int secondLeaderCard;

    public Cards_Slots_View(){
        firstSlot = new LinkedList<>();
        secondSlot = new LinkedList<>();
        thirdSlot = new LinkedList<>();
        firstLeaderCard = -1;
        secondLeaderCard = -1;
    }

    public void setLeaderCards(int firstLeaderCard, int secondLeaderCard){
        this.firstLeaderCard = firstLeaderCard;
        this.secondLeaderCard = secondLeaderCard;
    }

    public void addLeaderCard(int leaderCard){
        if(firstLeaderCard == -1)
            firstLeaderCard = leaderCard;
        else
            secondLeaderCard = leaderCard;
    }

    public ArrayList<Integer> getDevelopmentCards() {
        ArrayList<Integer> activeCards = new ArrayList<>();
        if(firstSlot.size() > 0)
            activeCards.add(firstSlot.getLast());
        if(secondSlot.size() > 0)
            activeCards.add(secondSlot.getLast());
        if(thirdSlot.size() > 0)
            activeCards.add(firstSlot.getLast());
        return activeCards;
    }

    public int getLeaderCard(int leaderCard){
       if(leaderCard == 1)
           return firstLeaderCard;
       else
           return secondLeaderCard;
    }

    public void addDevelopmentCard(int cardID, int slot){
        if(slot == 1)
            firstSlot.add(cardID);
        else if(slot == 2)
            secondSlot.add(cardID);
        else
            thirdSlot.add(cardID);
    }

    public void discardLeaderCard(int chosenLeaderCard){
        if(chosenLeaderCard == 1){
            firstLeaderCard = secondLeaderCard;
        }
        secondLeaderCard = -1;
    }

    public void printCliSlot(){
        System.out.println("SLOTS PLAYERBOARD");
        System.out.println("slot 1");
        int somma=0;
        int count=0;
        if (firstSlot.size()==0)
            System.out.println("vuoto");
        else
        {
            CardMap.getCard(firstSlot.getLast()).print();
            for(int i=0;i<firstSlot.size()-1;i++) {
                somma += CardMap.getCard(firstSlot.get(i)).getVictoryPoints();
                count++;
            }
            System.out.println("ho "+count+" carte coperte e la somma dei punti vittoria è: "+somma);
        }
        System.out.println("slot 2");
        somma=0;
        count=0;
        if (secondSlot.size()==0)
            System.out.println("vuoto");
        else
        {
            CardMap.getCard(secondSlot.getLast()).print();
            for(int i=0;i<secondSlot.size()-1;i++){
                somma+=CardMap.getCard(secondSlot.get(i)).getVictoryPoints();
                count++;
            }
            System.out.println("ho "+count+" carte coperte e la somma dei punti vittoria è: "+somma);
        }
        System.out.println("slot 3");
        somma=0;
        count=0;
        if (thirdSlot.size()==0)
            System.out.println("vuoto");
        else
        {
            CardMap.getCard(thirdSlot.getLast()).print();
            for(int i=0;i<thirdSlot.size()-1;i++) {
                somma += CardMap.getCard(thirdSlot.get(i)).getVictoryPoints();
                count++;
            }
            System.out.println("ho "+count+" carte coperte e la somma dei punti vittoria è: "+somma);
        }
    }

    public void printCliLeaderCard(){
        System.out.println("LEADER CARD");
        if(CardMap.getCard(firstLeaderCard).getVictoryPoints()!=0)
            CardMap.getCard(firstLeaderCard).print();
        if (CardMap.getCard(secondLeaderCard).getVictoryPoints()!=0)
            CardMap.getCard(secondLeaderCard).print();
    }

    public void print(){};
}
