package it.polimi.ingsw.model.market;

import it.polimi.ingsw.exceptions.WrongParametersException;

import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.view.model_view.Market_View;

import java.io.Serializable;

/**
 * Market is the market of Game.
 */
public class Market implements Serializable, Market_View {

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
       int[] numOfMarbles={1,2,2,2,2,4};
       boolean enough;
       // this attribute indicate the number of the marble(0 = RED, 1=YELLOW, 2=BLUE, 3=GREY, 4=PURPLE, 5=WHITE).
       int random;
       marketTray = new Marble[row][column];
       for (int i = 0; i < row; i++) {
           for (int j = 0; j < column; j++) {
               do {
                   random = (int) (Math.random() * 6);
                   enough = setCell(i,j,random,numOfMarbles);
               }while(!enough);
           }
       }
       externalMarbleSet(numOfMarbles);
   }

    /**
     * this private method set the cell of the market to a specific Marble.
     * @param i row of the market.
     * @param j column of the market.
     * @param random number of the marble(0 = RED, 1=YELLOW, 2=BLUE, 3=GREY, 4=PURPLE, 5=WHITE).
     * @param numOfMarble vector of the amount of marble for each color.
     * @return true if there are still Marble of that color left, else false.
     */
    private boolean setCell(int i, int j, int random,int[] numOfMarble) {
        if (numOfMarble[random]>0) {
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
     * @param numOfMarbles contains the amount of marble for each color.
     */
    private void externalMarbleSet(int[] numOfMarbles) {
        if(numOfMarbles[0]>0)
            externalMarble= new RedMarble();
        if(numOfMarbles[5]>0)
            externalMarble=new WhiteMarble();
        if(numOfMarbles[2]>0)
            externalMarble=new ResourceMarble(Resource.SHIELD);
        if(numOfMarbles[1]>0)
            externalMarble=new ResourceMarble(Resource.COIN);
        if(numOfMarbles[3]>0)
            externalMarble=new ResourceMarble(Resource.STONE);
        if(numOfMarbles[4]>0)
            externalMarble=new ResourceMarble(Resource.SERVANT);
    }

    /**
     * this method allows the caller to get all the marbles inside a selected column.
     * @param selectedColumn this is the column where the caller wants all the marbles.
     * @return a vector of Marble.
     * @throws WrongParametersException activated when the selectedColumn doesn't exist in the market.
     */
    public Marble[] getColumnMarbles(int selectedColumn) throws WrongParametersException {
        if (selectedColumn<1 || selectedColumn>4)
        throw new WrongParametersException();
        Marble[] marketCopy= new Marble[row];
        for(int i=0; i<row; i++)
        {
            marketCopy[i]=marketTray[i][selectedColumn-1];
        }
        return marketCopy;
    }

    /**
     * this method allows the caller to get all the marbles inside a selected row.
     * @param selectedRow this is the row where the caller wants all the marbles.
     * @return a vector of Marbles.
     * @throws WrongParametersException activated when the selectedRow doesn't exist in the market.
     */
    public Marble[] getRowMarbles(int selectedRow) throws WrongParametersException {
        if (selectedRow<1 || selectedRow>3)
        throw new WrongParametersException();
        Marble[] marketCopy= new Marble[column];
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
     * @param selectedColumn number of column that will be slided.
     * @throws WrongParametersException activated when the selectedColumn doesn't exist in the market.
     */
    @Override
    public void slideColumn(int selectedColumn) throws WrongParametersException {
        if (selectedColumn<1 || selectedColumn>4)
            throw new WrongParametersException();
        Marble marble;
        marble=this.externalMarble;
        this.externalMarble= marketTray[row-1][selectedColumn-1];
        for(int i=row-1; i>0; i--) {
            this.marketTray[i][selectedColumn-1]=this.marketTray[i-1][selectedColumn-1];
        }
        marketTray[0][selectedColumn-1]=marble;
    }

    /**
     * this method allows the caller to slide the selected row of the market of one position to the left,
     * the first marble of the column gets out of the matrix and will placed to the side slide,
     * the current element in the side slide gets placed to the last slot of the row.
     * @param selectedRow number of the row that will be slided.
     * @throws WrongParametersException activated when the selectedRow doesn't exist in the market.
     */
    @Override
    public void slideRow(int selectedRow) throws WrongParametersException {
        if(selectedRow < 1 || selectedRow > 3)
            throw new WrongParametersException();
        Marble marble;
        marble = this.externalMarble;
        this.externalMarble = marketTray[selectedRow-1][0];
        System.arraycopy(this.marketTray[selectedRow - 1], 1, this.marketTray[selectedRow - 1], 0, column - 1);
        marketTray[selectedRow-1][column-1] = marble;
    }

    public void printMarket(){
        System.out.println("MARKET");
    }
}
