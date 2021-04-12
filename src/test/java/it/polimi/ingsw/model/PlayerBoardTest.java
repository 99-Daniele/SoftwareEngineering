package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Test;

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
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.buyDevelopmentCard(developmentCard, 0, 1));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
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

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2);
        p.buyDevelopmentCard(developmentCard1, 0, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2);

        ImpossibleDevelopmentCardAdditionException thrown =
                assertThrows(ImpossibleDevelopmentCardAdditionException.class, () -> p.buyDevelopmentCard(developmentCard2, 0, 1));

        String expectedMessage = "Non puoi comprare questa carta";
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
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2);

        assertEquals(0, p.getVictoryPoints().getVictoryPointsByCards());
        assertEquals(3, p.findAvailableSlot(developmentCard).size());
        /*
         p has no cards so there 3 available slot.
         */

        p.buyDevelopmentCard(developmentCard, 0, 1);
        assertEquals(3, p.getVictoryPoints().getVictoryPointsByCards());
        assertEquals(2, p.findAvailableSlot(developmentCard).size());
        /*
         player has 1 card, so there are now 2 available slot.
         */
    }

    /**
     * this test tries to buy an expensive development card if there is an active DiscountCard
     */
    @Test
    void incorrectBuyDevelopmentCardNowEnoughResourceWithDiscountCard()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c1 = new Cost();
        c1.addResource(r1, 3);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new DiscountCard(r1, leaderRequirements, 1);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);
        p.activateLeaderCard(1);

        assertEquals(3, c1.getNumOfResource(r1));

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.buyDevelopmentCard(developmentCard, 0, 1));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(3, c1.getNumOfResource(r1));
    }

    /**
     * this test verifies the correct buying of DevelopmentCard if there is an active DiscountCard
     */
    @Test
    void correctBuyDevelopmentCardWithDiscountCard()
            throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);

        Cost c1 = new Cost();
        c1.addResource(r1, 2);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new DiscountCard(r1, leaderRequirements, 1);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);
        p.activateLeaderCard(1);

        assertEquals(2, c1.getNumOfResource(r1));
        /*
         p has 1 r1 and developmentCard costs 2 r1, but there is an active DiscountCard, so p can buy developmentCard
         */

        p.buyDevelopmentCard(developmentCard, 0, 1);
        assertEquals(1, c1.getNumOfResource(r1));
        /*
         developmentCard resourceCost has been decreased by 1
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
        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 1, c1, 3, c2, c3, 2);
        assertEquals(3, p.findAvailableSlot(card1).size());
        p.buyDevelopmentCard(card1, 1, 1);

        DevelopmentCard card2 = new DevelopmentCard(Color.YELLOW, 1, c2, 3, c1, c3, 2);
        assertEquals(2, p.findAvailableSlot(card2).size());
        assertEquals(0, p.findAvailableSlot(card2).get(0));
        assertEquals(2, p.findAvailableSlot(card2).get(1));
        /*
         available slots to insert card2 are first slot (0) and third (2)
         */

        p.buyDevelopmentCard(card2, 0, 1);
        DevelopmentCard card3 = new DevelopmentCard(Color.PURPLE, 1, c1, 3, c2, c3, 2);
        assertEquals(1, p.findAvailableSlot(card3).size());
        assertEquals(2, p.findAvailableSlot(card3).get(0));
        /*
         there is one available slot: the third one
         */

        DevelopmentCard card4 = new DevelopmentCard(Color.PURPLE, 2, c1, 3, c2, c3, 2);
        assertEquals(2, p.findAvailableSlot(card4).size());
        assertEquals(0, p.findAvailableSlot(card4).get(0));
        assertEquals(1, p.findAvailableSlot(card4).get(1));
        /*
         available slots to insert card4 are first and second slot, where there are level 1 cards
         */

        DevelopmentCard card5 = new DevelopmentCard(Color.GREEN, 3, c1, 3, c2, c3, 2);
        assertEquals(0, p.findAvailableSlot(card5).size());
        /*
         there are not available slots for card5 because there isn't any level 2 card
         */
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
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        p.buyDevelopmentCard(developmentCard, 0, 1);

        PowerProductionPlayerChoice playerChoice = new PowerProductionPlayerChoice();
        playerChoice.setFirstPower();
        assertTrue(playerChoice.isFirstPower());
        /*
         p has 1 r1 and 1 r2. He chose to activate firstSlotCard which requires 2 r1
         */

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> p.activateProduction(playerChoice));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
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
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        p.buyDevelopmentCard(developmentCard, 0, 1);

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

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
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
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        p.buyDevelopmentCard(developmentCard, 0, 1);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1);
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

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of DevelopmentCard production power, basic power and AdditionalProductionPowerLeaderCard
     */
    @Test
    void correctStandardBasicAndAdditionalProductionPower() throws
            InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException, ImpossibleSwitchDepotException, ImpossibleDevelopmentCardAdditionException {

        PlayerBoard p = new PlayerBoard("p1");
        p.increaseWarehouse(r1);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r2);
        p.increaseWarehouse(r3);
        p.increaseWarehouse(r3);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        c3.addResource(r1, 1);
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard, 0, 1);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);
        p.activateLeaderCard(1);

        PowerProductionPlayerChoice playerChoice = new PowerProductionPlayerChoice();
        playerChoice.setFirstPower();
        assertTrue(playerChoice.isFirstPower());

        playerChoice.setBasicPower(r2, r3, r2);
        assertTrue(playerChoice.isBasicPower());

        assertFalse(p.activateProduction(playerChoice));
        /*
         neither DevelopmentCard nor basic power give player faith points, so @return true
         */

        playerChoice.setFirstAdditionalPower(r2);
        assertTrue(playerChoice.isFirstAdditionalPower());

        assertTrue(p.activateProduction(playerChoice));
        /*
         AdditionalProductionPower gives 1 faith points, so @return true
         */
    }

    /**
     * this test trie to activate a previously discarded LeaderCArd
     */
    @Test
    void discardedLeaderCardActivation() throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);

        p.discardLeaderCard(1);

        AlreadyDiscardLeaderCardException thrown =
                assertThrows(AlreadyDiscardLeaderCardException.class, () -> p.activateLeaderCard(2));

        String expectedMessage = "Questa carta è stata già scartata in precedenza";
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
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1);
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
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        LeaderCard leaderCard2 = new DiscountCard(r1, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);

        p.activateLeaderCard(1);

        ActiveLeaderCardException thrown =
                assertThrows(ActiveLeaderCardException.class, () -> p.discardLeaderCard(1));

        String expectedMessage = "Questa carta è stata già attivata in precedenza";
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
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        LeaderCard leaderCard2= new DiscountCard(r1, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);

        p.discardLeaderCard(2);

        AlreadyDiscardLeaderCardException thrown =
                assertThrows(AlreadyDiscardLeaderCardException.class, () -> p.discardLeaderCard(2));

        String expectedMessage = "Questa carta è stata già scartata in precedenza";
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
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        LeaderCard leaderCard2 = new AdditionalProductionPowerCard(r2, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);

        assertEquals(0, p.getFaithPoints());
        p.discardLeaderCard(1);
        assertEquals(1, p.getFaithPoints());
        p.discardLeaderCard(1);
        assertEquals(2, p.getFaithPoints());
    }

    /**
     * this test verifies the correct return of whiteConversion
     */
    @Test
    void correctWhiteConversion()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        PlayerBoard p = new PlayerBoard("p1");

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(r1, leaderRequirements, 1);
        LeaderCard leaderCard2 = new WhiteConversionCard(r1, leaderRequirements, 1);
        p.addLeaderCard(leaderCard1, leaderCard2);

        assertSame(Resource.WHITE, p.whiteConversion(1));
        assertSame(Resource.WHITE, p.whiteConversion(2));

        p.activateLeaderCard(1);
        p.activateLeaderCard(2);
        assertSame(Resource.WHITE, p.whiteConversion(1));
        assertSame(r1, p.whiteConversion(2));
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

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard1, 0, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard2, 1, 1);

        DevelopmentCard developmentCard3= new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard3, 2, 1);

        DevelopmentCard developmentCard4 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard4, 0, 1);

        DevelopmentCard developmentCard5 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard5, 1, 1);

        DevelopmentCard developmentCard6 = new DevelopmentCard(Color.BLUE, 3, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard6, 0, 1);

        assertFalse(p.haveSevenDevelopmentCards());

        DevelopmentCard developmentCard7 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        p.buyDevelopmentCard(developmentCard7, 2, 1);
        assertTrue(p.haveSevenDevelopmentCards());
    }

    /**
     * this test calculates the sum of resources amount in PlayerBoard
     */
    @Test
    void sumTotalResource() throws InsufficientResourceException, ImpossibleSwitchDepotException {

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
