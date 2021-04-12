package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyFinishedGameException;
import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

public abstract class Action {

    public abstract void actionTrigger(LightSinglePlayerGame game) throws EmptyDevelopmentCardDeckException;
}
