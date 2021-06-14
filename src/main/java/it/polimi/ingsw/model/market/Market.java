package it.polimi.ingsw.model.market;

import it.polimi.ingsw.exceptions.WrongParametersException;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.view.CLI.ColorAnsi;
import it.polimi.ingsw.view.modelView.MarketView;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Market is the market of Game.
 */
public class Market implements Serializable, MarketView {
    private static final int row = 3;
    private static final int column = 4;
    private final Marble[][] marketTray;
    private Marble externalMarble;

    /**
     * the constructor build a matrix 3X4 of marbles and a slot for the side slide.
     * the market is build randomly with the amount of marbles available.
     */
    public Market() {
        // this array indicate the amount of marble for each color when the market is still empty.
        int[] numOfMarbles = {1, 2, 2, 2, 2, 4};
        boolean enough;
        // this attribute indicate the number of the marble(0 = RED, 1=YELLOW, 2=BLUE, 3=GREY, 4=PURPLE, 5=WHITE).
        int random;
        marketTray = new Marble[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                do {
                    random = (int) (Math.random() * 6);
                    enough = setCell(i, j, random, numOfMarbles);
                } while (!enough);
            }
        }
        externalMarbleSet(numOfMarbles);
    }

    /**
     * this private method set the cell of the market to a specific Marble.
     *
     * @param i           row of the market.
     * @param j           column of the market.
     * @param random      number of the marble(0 = RED, 1=YELLOW, 2=BLUE, 3=GREY, 4=PURPLE, 5=WHITE).
     * @param numOfMarble vector of the amount of marble for each color.
     * @return true if there are still Marble of that color left, else false.
     */
    private boolean setCell(int i, int j, int random, int[] numOfMarble) {
        if (numOfMarble[random] > 0) {
            numOfMarble[random]--;
            if (random == 0)
                marketTray[i][j] = new RedMarble();
            if (random == 1)
                marketTray[i][j] = new ResourceMarble(Resource.COIN);
            if (random == 2)
                marketTray[i][j] = new ResourceMarble(Resource.SHIELD);
            if (random == 3)
                marketTray[i][j] = new ResourceMarble(Resource.STONE);
            if (random == 4)
                marketTray[i][j] = new ResourceMarble(Resource.SERVANT);
            if (random == 5)
                marketTray[i][j] = new WhiteMarble();
            return true;
        }
        return false;
    }

    /**
     * this private method set the marble that is situated in the side slide.
     *
     * @param numOfMarbles contains the amount of marble for each color.
     */
    private void externalMarbleSet(int[] numOfMarbles) {
        if (numOfMarbles[0] > 0)
            externalMarble = new RedMarble();
        if (numOfMarbles[5] > 0)
            externalMarble = new WhiteMarble();
        if (numOfMarbles[2] > 0)
            externalMarble = new ResourceMarble(Resource.SHIELD);
        if (numOfMarbles[1] > 0)
            externalMarble = new ResourceMarble(Resource.COIN);
        if (numOfMarbles[3] > 0)
            externalMarble = new ResourceMarble(Resource.STONE);
        if (numOfMarbles[4] > 0)
            externalMarble = new ResourceMarble(Resource.SERVANT);
    }

    /**
     * this method allows the caller to get all the marbles inside a selected column.
     *
     * @param selectedColumn this is the column where the caller wants all the marbles.
     * @return a vector of Marble.
     */
    public Marble[] getColumnMarbles(int selectedColumn) {
        Marble[] marketCopy = new Marble[row];
        for (int i = 0; i < row; i++) {
            marketCopy[i] = marketTray[i][selectedColumn - 1];
        }
        return marketCopy;
    }

    /**
     * this method allows the caller to get all the marbles inside a selected row.
     *
     * @param selectedRow this is the row where the caller wants all the marbles.
     * @return a vector of Marbles.
     */
    public Marble[] getRowMarbles(int selectedRow) {
        Marble[] marketCopy = new Marble[column];
        System.arraycopy(marketTray[selectedRow - 1], 0, marketCopy, 0, column);
        return marketCopy;
    }

    /**
     * @return the external marble.
     */
    public Marble getExternalMarble() {
        return externalMarble;
    }

    /**
     * this method allows the caller to slide the selected column(of the market)of one position upwards,
     * the last marble of the column gets out of the matrix and will placed to the side slide,
     * the current element on the side slide gets placed to the first slot of the column.
     *
     * @param selectedColumn number of column that will be slided.
     */
    public void slideColumn(int selectedColumn){
        Marble marble;
        marble=this.externalMarble;
        this.externalMarble= marketTray[0][selectedColumn-1];
        for(int i=0; i<row-1; i++) {
            this.marketTray[i][selectedColumn-1]=this.marketTray[i+1][selectedColumn-1];
        }
        marketTray[row-1][selectedColumn-1]=marble;
    }

    /**
     * this method allows the caller to slide the selected row of the market of one position to the left,
     * the first marble of the column gets out of the matrix and will placed to the side slide,
     * the current element in the side slide gets placed to the last slot of the row.
     *
     * @param selectedRow number of the row that will be slided.
     */
    @Override
    public void slideRow(int selectedRow) {
        Marble marble;
        marble = this.externalMarble;
        this.externalMarble = marketTray[selectedRow - 1][0];
        System.arraycopy(this.marketTray[selectedRow - 1], 1, this.marketTray[selectedRow - 1], 0, column - 1);
        marketTray[selectedRow - 1][column - 1] = marble;
    }

    @Override
    public void printMarketCli() {
        System.out.println("\nMARKET:");
        String[] s = new String[13];
        int i = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 4; y++) {
                Marble m = marketTray[x][y];
                s[i] = fromMarbleToString(m);
                i++;
            }
        }
        s[i] = fromMarbleToString(externalMarble);
        creationTable(s);
    }

    private void creationTable(String[] s) {
        String[][] tabella = new String[7][17];
        for (int x = 0; x < 7; x += 2) {
            tabella[x][0] = "╠";
            for (int y = 1; y < 16; y++) {
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "╬";
            }
            tabella[x][16] = "╣";
        }
        tabella[0][0] = "╔";
        for (int i = 1; i < 4; i++) {
            tabella[0][4 * i] = "╦";
        }
        tabella[0][16] = "╗";
        tabella[6][0] = "╚";
        for (int i = 1; i < 4; i++) {
            tabella[6][4 * i] = "╩";
        }
        tabella[6][16] = "╝";
        for (int x = 1; x < 7; x += 2) {
            tabella[x][0] = "║";
            for (int y = 1; y < 15; y++) {
                tabella[x][y] = "";
                y += 2;
                tabella[x][y] = "";
                y++;
                tabella[x][y] = "║";
            }
            tabella[x][16] = "║";
        }
        tabella[0][0] = "╔";
        tabella[6][0] = "╚";
        tabella[0][16] = "╗";
        tabella[6][16] = "╝";
        int count = 0;
        for (int x = 1; x < 6; x += 2)
            for (int y = 2; y < 15; y += 4) {
                tabella[x][y] = s[count] + " ● " + ColorAnsi.RESET;
                count++;
            }
        String[][] tabella2 = new String[3][5];
        tabella2[0][0] = "╔";
        tabella2[0][1] = "═";
        tabella2[0][2] = "═";
        tabella2[0][3] = "═";
        tabella2[0][4] = "╗";
        tabella2[1][0] = "║";
        tabella2[1][1] = "";
        tabella2[1][2] = s[count] + " ● " + ColorAnsi.RESET;
        tabella2[1][3] = "";
        tabella2[1][4] = "║";
        tabella2[2][0] = "╚";
        tabella2[2][1] = "═";
        tabella2[2][2] = "═";
        tabella2[2][3] = "═";
        tabella2[2][4] = "╝";
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 17; j++)
                System.out.print(tabella[i][j]);
            System.out.println();
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++)
                System.out.print(tabella2[i][j]);
            System.out.println();
        }
    }

    public void printRow(int row) {
        row--;
        String[] s = new String[4];
        int i = 0;
        for (int y = 0; y < 4; y++) {
            Marble m = marketTray[row][y];
            s[i] = fromMarbleToString(m);
            i++;
        }
        creationRow(s);
    }

    private void creationRow(String[] s) {
        String[][] tabella = new String[3][s.length * 4 + 1];
        for (int y = 0; y < s.length * 4 + 1; y += 4)
            tabella[1][y] = "║";
        tabella[0][0] = "╔";
        tabella[2][0] = "╚";
        tabella[0][s.length * 4] = "╗";
        tabella[2][s.length * 4] = "╝";
        for (int x = 0; x < 3; x += 2)
            for (int y = 1; y < s.length * 4; y++)
                tabella[x][y] = "═";
        for (int i = 1; i < s.length; i++) {
            tabella[0][4 * i] = "╦";
        }
        for (int i = 1; i < s.length; i++) {
            tabella[2][4 * i] = "╩";
        }
        int count = 0;
        for (int x = 1; x < 3; x += 2) {
            for (int y = 1; y < s.length * 4 + 1; y += 2) {
                tabella[x][y] = "";
                y++;
                tabella[x][y] = s[count] + " ● " + ColorAnsi.RESET;
                y++;
                tabella[x][y] = "";
                count++;
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < s.length * 4 + 1; j++)
                System.out.print(tabella[i][j]);
            System.out.println();
        }
    }

    public void printColumn(int column) {
        column--;
        String[] s = new String[3];
        int i = 0;
        for (int x = 0; x < 3; x++) {
            Marble m = marketTray[x][column];
            s[i] = fromMarbleToString(m);
            i++;
        }
        creationColumn(s);
    }

    private void creationColumn(String[] s) {
        String[][] tabella = new String[7][5];
        for (int x = 1; x < 7; x++)
            for (int y = 0; y < 5; y += 4)
                tabella[x][y] = "║";
        for (int x = 0; x < 7; x += 2) {
            tabella[x][0] = "╠";
            for (int y = 1; y < 4; y++) {
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "═";
                y++;
                tabella[x][y] = "╬";
            }
            tabella[x][4] = "╣";
        }
        tabella[0][0] = "╔";
        tabella[6][0] = "╚";
        tabella[0][4] = "╗";
        tabella[6][4] = "╝";
        int count = 0;
        for (int x = 1; x < 7; x += 2) {
            tabella[x][1] = "";
            tabella[x][2] = s[count] + " ● " + ColorAnsi.RESET;
            tabella[x][3] = "";
            count++;
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++)
                System.out.print(tabella[i][j]);
            System.out.println();
        }
    }

    @Override
    public void printMarbles(ArrayList<Marble> marbles) {
        String[] s = new String[marbles.size()];
        int i = 0;
        for (Marble m : marbles) {
            s[i] = fromMarbleToString(m);
            i++;
        }
        creationRow(s);
    }

    @Override
    public Marble getMarble(int row, int column) {
        return marketTray[row][column];
    }

    private String fromMarbleToString(Marble m) {
        String s1 = m.toStringAbb();
        switch (s1) {
            case "R":
                return ColorAnsi.ANSI_RED.escape();
            case "G":
                return ColorAnsi.ANSI_WHITE.escape();
            case "Y":
                return ColorAnsi.ANSI_YELLOW.escape();
            case "B":
                return ColorAnsi.ANSI_CYAN.escape();
            case "P":
                return ColorAnsi.ANSI_PURPLE.escape();
            default:
                return "";
        }
    }
}