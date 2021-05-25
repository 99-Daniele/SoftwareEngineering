package it.polimi.ingsw.modelTests.marketTests;

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
        Marble external;
        external=market.getExternalMarble();

        //checking if a column change after being slided.
        market.slideColumn(2);
        change1 = market.getColumnMarbles(2);
        for (int i = 0; i < row; i++) {
            assertNotEquals(marble1[i], change1[i]);
        }
        assertEquals(marble1[1],change1[0]);
        assertEquals(marble1[2],change1[1]);
        assertEquals(external,change1[2]);
        external=market.getExternalMarble();
        assertEquals(external,marble1[0]);
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
            assertNotEquals(marble[i], change[i]);
        }
        assertEquals(marble[1],change[0]);
        assertEquals(marble[2],change[1]);
        assertEquals(marble[3],change[2]);
        assertEquals(marble[0],market.getExternalMarble());
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
            selectedRow=market.getRowMarbles(3);
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
    }
}



