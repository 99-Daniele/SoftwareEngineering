package it.polimi.ingsw.parser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class DevelopmentCardsParser {

    private final DevelopmentCard[] developmentCards;

    public DevelopmentCardsParser() {
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/DevelopmentCards.json")), StandardCharsets.UTF_8);
        developmentCards = gson.fromJson(reader, DevelopmentCard[].class);
    }

    public DevelopmentCard[] getDevelopmentCards(){
        return developmentCards;
    }
}
