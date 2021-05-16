package it.polimi.ingsw.model.playersTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.developmentCards.*;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerBoardTest {

    Resource r1 = Resource.COIN;
    Resource r2 = Resource.SHIELD;
    Resource r3 = Resource.STONE;

    /**
     * this test tries to buy an expensive development card if there isn't any active DiscountCard
     */
    @Test
    void incorrectBuyDevelopmentCardNotEnoughResourceNoDiscountCard(){

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c1 = new Cost();
        c1.addResource(r1, 2);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2, cardID);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.buyDevelopmentCard(developmentCard, 0, 1));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to insert a DevelopmentCard where it can't be inserted
     */
    @Test
    void incorrectBuyDevelopmentCard() throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2, cardID);
        p.buyDevelopmentCard(developmentCard1, 1, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2, cardID);

        ImpossibleDevelopmentCardAdditionException thrown =
                assertThrows(ImpossibleDevelopmentCardAdditionException.class, () -> p.buyDevelopmentCard(developmentCard2, 1, 1));

        String expectedMessage = "You have not available slots to buy this card";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct buying of DevelopmentCard if there isn't any active DiscountCard
     */
    @Test
    void correctBuyDevelopmentCardNoDiscountCard() throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2, cardID);

        assertEquals(0, p.getVictoryPoints().getVictoryPointsByCards());
        assertEquals(3, p.findAvailableSlot(developmentCard).size());
        /*
         p has no cards so there 3 available slot.
         */

        p.buyDevelopmentCard(developmentCard, 1, 1);
        assertEquals(3, p.getVictoryPoints().getVictoryPointsByCards());
        assertEquals(2, p.findAvailableSlot(developmentCard).size());
        /*
         player has 1 card, so there are now 2 available slot.
         */
    }

    /**
     * this test tries to buy an expensive development card if there are two active DiscountCards
     */
    @Test
    void incorrectBuyDevelopmentCardNotEnoughResourceWithDiscountCard()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c1 = new Cost();
        c1.addResource(r1, 3);
        c1.addResource(r2, 3);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2, cardID);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new DiscountCard(r1, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new DiscountCard(r2, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);
        p.activateLeaderCard(1);
        p.activateLeaderCard(2);

        assertEquals(3, c1.getNumOfResource(r1));
        assertEquals(3, c1.getNumOfResource(r2));

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.buyDevelopmentCard(developmentCard, 0, 1));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(3, c1.getNumOfResource(r1));
        assertEquals(3, c1.getNumOfResource(r2));
        /*
         DevelopmentCard has been discounted but since it wasn't bought by player, because he hasn't enough resource,
         it return to his original cost.
         */
    }

    /**
     * this test verifies the correct buying of DevelopmentCard if there are two active DiscountCards
     */
    @Test
    void correctBuyDevelopmentCardWithDiscountCards()
            throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);

        Cost c1 = new Cost();
        c1.addResource(r1, 2);
        c1.addResource(r2, 2);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2, cardID);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new DiscountCard(r1, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new DiscountCard(r2, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);
        p.activateLeaderCard(1);
        p.activateLeaderCard(2);

        assertEquals(2, c1.getNumOfResource(r1));
        assertEquals(2, c1.getNumOfResource(r2));
        /*
         p has 1 r1 and 1 r2, and developmentCard costs 2 r1 and 2 r2, but there are two active DiscountCard,
         so p can buy developmentCard
         */

        p.buyDevelopmentCard(developmentCard, 1, 1);
        assertEquals(1, c1.getNumOfResource(r1));
        assertEquals(1, c1.getNumOfResource(r2));
        /*
         developmentCard resourceCost has been decreased by 1 during the buying
         */
    }

    /**
     * this test verifies the correct finding of available slots to insert DevelopmentCard
     */
    @Test
    void correctFindAvailableSlots() throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);

        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        c1.addResource(r2, 1);
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2, cardID);
        assertEquals(3, p.findAvailableSlot(card1).size());
        p.buyDevelopmentCard(card1, 1, 1);

        DevelopmentCard card2 = new DevelopmentCard(Color.YELLOW, 1, c2, 3, c1, c3, 2, cardID);
        assertEquals(2, p.findAvailableSlot(card2).size());
        assertEquals(2, p.findAvailableSlot(card2).get(0));
        assertEquals(3, p.findAvailableSlot(card2).get(1));
        /*
         available slots to insert card2 are first slot (0) and third (2)
         */

        p.buyDevelopmentCard(card2, 2, 1);
        DevelopmentCard card3 = new DevelopmentCard(Color.PURPLE, 1, c1, 3, c2, c3, 2, cardID);
        assertEquals(1, p.findAvailableSlot(card3).size());
        assertEquals(3, p.findAvailableSlot(card3).get(0));
        /*
         there is one available slot: the third one
         */

        DevelopmentCard card4 = new DevelopmentCard(Color.PURPLE, 2, c1, 3, c2, c3, 2, cardID);
        assertEquals(2, p.findAvailableSlot(card4).size());
        assertEquals(1, p.findAvailableSlot(card4).get(0));
        assertEquals(2, p.findAvailableSlot(card4).get(1));
        /*
         available slots to insert card4 are first and second slot, where there are level 1 cards
         */

        DevelopmentCard card5 = new DevelopmentCard(Color.GREEN, 3, c1, 3, c2, c3, 2, cardID);
        assertEquals(0, p.findAvailableSlot(card5).size());
        /*
         there are not available slots for card5 because there isn't any level 2 card
         */
    }

    /**
     *
     */
    @Test
    void correctStrongboxIncrease(){

        PlayerBoard p = new PlayerBoard("p");
        Strongbox s = new Strongbox();
        s.increaseResourceType(Resource.COIN, 2);

        assertEquals(0, p.sumTotalResource());
        p.increaseStrongbox(s);
        assertEquals(2, p.sumTotalResource());
    }

    /**
     * this test tries to activate DevelopmentCard production power without having enough resources
     */
    @Test
    void notEnoughResourceStandardProductionPower() throws
            InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 2);
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);
        p.buyDevelopmentCard(developmentCard, 1, 1);

        PowerProductionPlayerChoice playerChoice = new PowerProductionPlayerChoice();
        playerChoice.setFirstPower();
        assertTrue(playerChoice.isFirstPower());
        /*
         p has 1 r1 and 1 r2. He chose to activate firstSlotCard which requires 2 r1
         */

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.activateProduction(playerChoice));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate DevelopmentCard production power and basic power without having enough resources
     */
    @Test
    void notEnoughResourceStandardAndBasic() throws
            InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r2);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);
        p.buyDevelopmentCard(developmentCard, 1, 1);

        PowerProductionPlayerChoice playerChoice = new PowerProductionPlayerChoice();
        playerChoice.setFirstPower();
        assertTrue(playerChoice.isFirstPower());

        playerChoice.setBasicPower(r2, r3, r1);
        assertTrue(playerChoice.isBasicPower());
        /*
         p has 1 r1 and 1 r2. He chose to activate firstSlotCard which requires 1 r1 and basicPower which requires 1 r2
         and 1 r3. p has not enough resources
         */

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.activateProduction(playerChoice));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate DevelopmentCard production power, basic power and AdditionalProductionPowerLeaderCard
     * without having enough resources
     */
    @Test
    void notEnoughResourceStandardBasicAndAdditional() throws
            InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r3);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);
        p.buyDevelopmentCard(developmentCard, 1, 1);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);
        p.activateLeaderCard(1);

        PowerProductionPlayerChoice playerChoice = new PowerProductionPlayerChoice();
        playerChoice.setFirstPower();
        assertTrue(playerChoice.isFirstPower());

        playerChoice.setBasicPower(r2, r3, r1);
        assertTrue(playerChoice.isBasicPower());

        playerChoice.setFirstAdditionalPower(r1);
        assertTrue(playerChoice.isFirstAdditionalPower());
        /*
         p has 1 r1, 1 r2 and 1 r3. He chose to activate firstSlotCard which requires 1 r1, basicPower which
         requires 1 r2 and 1 r3, leaderCardPower which requires 1 r2. p has not enough resources
         */

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.activateProduction(playerChoice));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of DevelopmentCard production power, basic power and AdditionalProductionPowerLeaderCard
     */
    @Test
    void correctStandardBasicAndAdditionalProductionPower() throws
            InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException, ImpossibleDevelopmentCardAdditionException, NoSuchProductionPowerException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r3);
        p.increaseWarehouse(r3);
        p.increaseWarehouse(r3);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        int cardID = 0;
        c3.addResource(r1, 1);
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 2, cardID);
        p.buyDevelopmentCard(developmentCard, 1, 1);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new AdditionalProductionPowerCard(r3, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);
        p.activateLeaderCard(1);
        p.activateLeaderCard(2);

        PowerProductionPlayerChoice playerChoice = new PowerProductionPlayerChoice();

        playerChoice.setFirstPower();
        assertTrue(playerChoice.isFirstPower());

        playerChoice.setBasicPower(r2, r3, r2);
        assertTrue(playerChoice.isBasicPower());

        playerChoice.setFirstAdditionalPower(r2);
        assertTrue(playerChoice.isFirstAdditionalPower());

        playerChoice.setSecondAdditionalPower(r2);
        assertTrue(playerChoice.isSecondAdditionalPower());

        playerChoice.setChoice(1);

        assertTrue(p.activateProduction(playerChoice));
        assertEquals(4, p.getFaithPoints());
    }

    /**
     * this test tries to activate the production of an empty SlotDevelopmentCards
     */
    @Test
    void incorrectEmptySlotDevelopmentCard(){

        PlayerBoard p = new PlayerBoard("p1");
        Strongbox s = new Strongbox();

        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> p.activateDevelopmentCardProductionPower(1, s, 1));

        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate the production of a not existing DevelopmentCard
     */
    @Test
    void incorrectNotExistingDevelopmentCard(){

        PlayerBoard p = new PlayerBoard("p1");
        PowerProductionPlayerChoice choice = new PowerProductionPlayerChoice();
        choice.setFirstPower();

        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> p.activateProduction(choice));

        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate the production of an inactive AdditionalProductionPowerCard
     */
    @Test
    void incorrectNotActiveAdditionalCard(){

        PlayerBoard p = new PlayerBoard("p1");
        PowerProductionPlayerChoice choice = new PowerProductionPlayerChoice();
        Cost c = new Cost();
        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 1, 0);
        p.addLeaderCard(card, card);
        choice.setFirstAdditionalPower(r1);

        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> p.activateProduction(choice));

        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate the production of a not existing AdditionalProductionPowerCard
     */
    @Test
    void incorrectNotExistingAdditionalCard() throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        PlayerBoard p = new PlayerBoard("p1");
        PowerProductionPlayerChoice choice = new PowerProductionPlayerChoice();
        Cost c = new Cost();
        LeaderCard card = new DiscountCard(r1, c, 1, 0);
        p.addLeaderCard(card, card);
        p.activateLeaderCard(1);
        choice.setFirstAdditionalPower(r1);

        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> p.activateProduction(choice));

        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate production power if there aren't enough resources
     */
    @Test
    void incorrectNotEnoughResourceStandardProductionPower()
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        c3.addResource(r1, 1);
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 2, cardID);
        p.buyDevelopmentCard(developmentCard, 1, 1);
        Strongbox s = new Strongbox();

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.activateDevelopmentCardProductionPower(1, s, 1));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of a DevelopmentCard production power
     */
    @Test
    void correctStandardProductionPower() throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, NoSuchProductionPowerException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        c3.addResource(r1, 1);
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 2, cardID);
        p.buyDevelopmentCard(developmentCard, 2, 1);

        Strongbox s = new Strongbox();
        assertEquals(1, p.sumTotalResource());

        p.activateDevelopmentCardProductionPower(2, s, 1);
        assertEquals(0, p.sumTotalResource());
        assertEquals(1, s.getNumOfResource(r1));
    }

    /**
     * this test tries to activate basic production power of DevelopmentCard if there aren't enough resources
     */
    @Test
    void incorrectNotEnoughResourceBasicProductionPower() {

        PlayerBoard p = new PlayerBoard("p1");

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.activateBasicProduction(r1, r2, 1));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of basic production power
     */
    @Test
    void correctBasicProductionPower() throws InsufficientResourceException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);
        assertEquals(2, p.sumTotalResource());

        p.activateBasicProduction(r1, r2, 2);
        assertEquals(0, p.sumTotalResource());
    }

    /**
     * this test tries to activate the production of a not existing AdditionalProductionPowerCard
     */
    @Test
    void incorrectNotExistingAdditionalProductionPowerCard(){

        PlayerBoard p = new PlayerBoard("p1");

        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> p.activateAdditionalProductionPower(2,  1));

        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate production power of AdditionalProductionPowerCard if there aren't enough resources
     */
    @Test
    void incorrectNotEnoughResourceAdditionalProductionPower()
            throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");
        Cost c = new Cost();
        LeaderRequirements l = new LeaderRequirements();
        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 1, 0);
        p.addLeaderCard(card, card);
        p.activateLeaderCard(1);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.activateAdditionalProductionPower(1,1));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of a AdditionalProductionPowerCard production power
     */
    @Test
    void correctAdditionalProductionPower() throws InsufficientResourceException, NoSuchProductionPowerException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c = new Cost();
        LeaderRequirements l = new LeaderRequirements();
        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 1, 0);
        p.addLeaderCard(card, card);
        p.activateLeaderCard(1);
        assertEquals(1, p.sumTotalResource());

        p.activateAdditionalProductionPower(1, 2);
        assertEquals(0, p.sumTotalResource());
    }


    /**
     * this test trie to activate a previously discarded LeaderCard
     */
    @Test
    void discardedLeaderCardActivation() throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        p.discardLeaderCard(1);

        AlreadyDiscardLeaderCardException thrown =
                assertThrows(AlreadyDiscardLeaderCardException.class, () -> p.activateLeaderCard(2));

        String expectedMessage = "This card has already been discarded";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of a LeaderCard
     */
    @Test
    void correctLeaderCardActivation()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        assertEquals(0, p.getVictoryPoints().getVictoryPointsByCards());
        p.activateLeaderCard(1);
        assertEquals(1, p.getVictoryPoints().getVictoryPointsByCards());
    }

    /**
     * this test tries to discard an active LeaderCard
     */
    @Test
    void discardActiveLeaderCard()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        p.activateLeaderCard(1);

        ActiveLeaderCardException thrown =
                assertThrows(ActiveLeaderCardException.class, () -> p.discardLeaderCard(1));

        String expectedMessage = "This card has already been activate";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to discard an already discarded LeaderCard
     */
    @Test
    void alreadyDiscardedLeaderCard() throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2= new DiscountCard(r1, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        p.discardLeaderCard(2);

        AlreadyDiscardLeaderCardException thrown =
                assertThrows(AlreadyDiscardLeaderCardException.class, () -> p.discardLeaderCard(2));

        String expectedMessage = "This card has already been discarded";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct discard of LeaderCard
     */
    @Test
    void correctDiscardLeaderCard() throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        assertEquals(0, p.getFaithPoints());
        p.discardLeaderCard(1);
        assertEquals(1, p.getFaithPoints());
        p.discardLeaderCard(1);
        assertEquals(2, p.getFaithPoints());
    }

    /**
     * this test verifies the correct return of isWhiteConver
     */
    @Test
    void correctWhiteConversion()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        PlayerBoard p = new PlayerBoard("p1");

        assertFalse(p.isWhiteConversionLeaderCardActive(1));
        assertFalse(p.isWhiteConversionLeaderCardActive(2));
        /*
         there are any WhiteConversionCard so @return false
         */

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r1, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new WhiteConversionCard(r1, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        assertFalse(p.isWhiteConversionLeaderCardActive(1));
        assertFalse(p.isWhiteConversionLeaderCardActive(2));

        p.activateLeaderCard(1);
        p.activateLeaderCard(2);
        assertFalse(p.isWhiteConversionLeaderCardActive(1));
        assertTrue(p.isWhiteConversionLeaderCardActive(2));
    }

    /**
     * this test tries to get an already discarded LeaderCard
     */
    @Test
    void alreadyDiscardedGetLeaderCard() throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2= new DiscountCard(r1, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        p.discardLeaderCard(2);

        AlreadyDiscardLeaderCardException thrown =
                assertThrows(AlreadyDiscardLeaderCardException.class, () -> p.getLeaderCard(2));

        String expectedMessage = "This card has already been discarded";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct getting of LeaderCard
     */
    @Test
    void correctGetLeaderCard() throws AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        LeaderCard leaderCard2 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1, 0);
        p.addLeaderCard(leaderCard1, leaderCard2);

        assertSame(leaderCard1, p.getLeaderCard(1));
        assertSame(leaderCard2, p.getLeaderCard(2));
    }

    /**
     * this test verify if player has 7 DevelopmentCards
     */
    @Test
    void sevenDevelopmentCards() throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        p.buyDevelopmentCard(developmentCard1, 1, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        p.buyDevelopmentCard(developmentCard2, 2, 1);

        DevelopmentCard developmentCard3= new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        p.buyDevelopmentCard(developmentCard3, 3, 1);

        DevelopmentCard developmentCard4 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        p.buyDevelopmentCard(developmentCard4, 1, 1);

        DevelopmentCard developmentCard5 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        p.buyDevelopmentCard(developmentCard5, 2, 1);

        DevelopmentCard developmentCard6 = new DevelopmentCard(Color.BLUE, 3, c1, 1, c2, c3, 0, cardID);
        p.buyDevelopmentCard(developmentCard6, 1, 1);

        assertFalse(p.haveSevenDevelopmentCards());

        DevelopmentCard developmentCard7 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        p.buyDevelopmentCard(developmentCard7, 3, 1);
        assertTrue(p.haveSevenDevelopmentCards());
    }

    /**
     * this test calculates the sum of total victory points
     */
    @Test
    void sumTotalVictoryPoints()
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        Cost c = new Cost();
        int cardID = 0;
        assertEquals(0, p.sumVictoryPoints());

        DevelopmentCard card = new DevelopmentCard(Color.GREEN, 1, c, 2, c, c, 0, cardID);
        p.buyDevelopmentCard(card, 1, 1);
        assertEquals(2, p.sumVictoryPoints());

        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r3);
        p.increaseWarehouse(r3);
        p.increaseWarehouse(r3);
        assertEquals(6, p.sumTotalResource());

        assertEquals(3, p.sumVictoryPoints());
    }

    /**
     * this test calculates the sum of resources amount in PlayerBoard
     */
    @Test
    void sumTotalResource() throws InsufficientResourceException, NoSuchProductionPowerException {

        PlayerBoard p = new PlayerBoard("p1");
        assertEquals(0, p.sumTotalResource());

        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r2);

        assertEquals(3, p.sumTotalResource());

        PowerProductionPlayerChoice powerChoice = new PowerProductionPlayerChoice();
        powerChoice.setBasicPower(r1, r2, r3);
        p.activateProduction(powerChoice);

        assertEquals(2, p.sumTotalResource());
    }
}
