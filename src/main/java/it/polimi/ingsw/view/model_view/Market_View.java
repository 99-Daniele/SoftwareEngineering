package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.exceptions.WrongParametersException;

public interface Market_View {

    void slideColumn(int selectedColumn) throws WrongParametersException;

    void slideRow(int selectedRow) throws WrongParametersException;

    void printMarket();
}
