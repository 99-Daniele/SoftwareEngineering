package it.polimi.ingsw.model.marketTests;

import it.polimi.ingsw.exceptions.WrongParametersException;

import it.polimi.ingsw.model.market.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MarketTest {
    static final int row = 3;
    static final int column = 4;

    /**
     * checking if a column remain unchanged after a different column has been slided.
     * checking if a column change after being slided.
     * @throws ArrayIndexOutOfBoundsException activated when the selectedColumn or selectedRow doesn't exist in the market.
     */
    @Test
    void slideColumnMarketTest() throws WrongParametersException {
        Market market = new Market();
        assertNotNull(market);

        //checking if a column remain unchanged after a different column has been slided.
        Marble[] marble1 = market.getColumnMarbles(2);
        market.slideColumn(4);
        Marble[] change1 = market.getColumnMarbles(2);
        for (int i = 0; i < row; i++) {
            assertEquals(marble1[i], change1[i]);
        }

        //checking if a column change after being slided.
        market.slideColumn(2);
        change1 = market.getColumnMarbles(2);
        for (int i = 0; i < row; i++) {
            assertNotEquals(marble1[i], change1[i]);
        }
    }

    /**
     * this test tries to slide a not existing row in Market
     */
    @Test
    void WrongParametersSlide(){

        Market market = new Market();
        assertNotNull(market);

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> market.slideRow(0));

        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    /**
     * checking if a row remain unchanged after a different row has been slided.
     * checking if a row change after being slided.
     * @throws ArrayIndexOutOfBoundsException activated when the selectedColumn or selectedRow doesn't exist in the market.
     */
    @Test
    public void slideRowMarketTest() throws WrongParametersException {
        Market market = new Market();
        assertNotNull(market);

        //checking if a row remain unchanged after a different row has been slided.
        Marble[] marble = market.getRowMarbles(2);
        market.slideRow(3);
        Marble[] change = market.getRowMarbles(2);
        for (int i = 0; i < column; i++) {
            assertEquals(marble[i], change[i]);
        }

        //checking if a row change after being slided.
        market.slideRow(2);
        change = market.getRowMarbles(2);
        for (int i = 0; i < column; i++) {
            if (marble[i] != change[i])
                assertFalse(false);
        }
    }

    /**
     * checking if the market has been prepared with the correct amount of Marbles and color of Marbles.
     * @throws ArrayIndexOutOfBoundsException activated when the selectedColumn or selectedRow doesn't exist in the market.
     */
    @Test
    void marketCreationTest() throws WrongParametersException {
        Market market = new Market();
        Marble[] selectedRow;
        int red = 0;
        int white = 0;
        int resource=0;
        for (int i=1;i<=row;i++) {
            selectedRow = market.getRowMarbles(i);
            for (int j = 0; j < column; j++) {
                if (selectedRow[j] instanceof RedMarble) {
                    red++;
                }
                if (selectedRow[j] instanceof WhiteMarble) {
                    white++;
                }
                if (selectedRow[j] instanceof ResourceMarble) {
                    resource++;
                }
            }
        }
            //checking the color of the marble in the side slide
            market.slideColumn(1);
            selectedRow=market.getRowMarbles(1);
            if (selectedRow[0] instanceof RedMarble) {
                red++;
            }
            if (selectedRow[0] instanceof WhiteMarble) {
                white++;
            }
            if (selectedRow[0] instanceof ResourceMarble) {
                resource++;
            }
            //check if the number of marble are the same as the rules says.
            assertEquals(1,red);
            assertEquals(4,white);
            assertEquals(8,resource);

        }


    /**
     * checking if the licit action are being executed correctly,
     * then checking if an exception is being thrown after a prohibited action has been made.
     * @throws ArrayIndexOutOfBoundsException activated when the selectedColumn or selectedRow doesn't exist in the market.
     */
    @Test
    void wrongIndexSelectedTest() throws WrongParametersException {
        Market market = new Market();
        assertNotNull(market);


        //checking if a row remain unchanged after a different row has been slided.
        Marble[] marble = market.getRowMarbles(3);
        market.slideRow(2);
        Marble[] change = market.getRowMarbles(3);
        for (int i = 0; i < column; i++) {
            assertEquals(marble[i], change[i]);
        }

        //checking if a row change after being slided.
        market.slideRow(2);
        change = market.getRowMarbles(2);
        for (int i = 0; i < column; i++) {
            if (marble[i] != change[i])
                assertFalse(false);
        }

        //trying to select out of bound index
        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> market.slideColumn(5));
        assertTrue(thrown.getMessage().contains("You have inserted wrong parameters"));

        WrongParametersException thrown1 =
                assertThrows(WrongParametersException.class, () -> market.getRowMarbles(0));
        String expectedMessage1 = "You have inserted wrong parameters";
        String actualMessage1 = thrown1.getMessage();
        assertTrue(actualMessage1.contains(expectedMessage1));

        WrongParametersException thrown2 =
                assertThrows(WrongParametersException.class, () -> market.slideRow(5));
        String expectedMessage2 = "You have inserted wrong parameters";
        String actualMessage2 = thrown2.getMessage();
        assertTrue(actualMessage2.contains(expectedMessage2));

    }
}



