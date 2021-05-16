package it.polimi.ingsw.parser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.cards.developmentCards.DevelopmentCard;

import java.io.FileReader;
import java.io.IOException;

public class DevelopmentCardsParser {

    private DevelopmentCard[] developmentCards;

    public DevelopmentCardsParser() {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/DevelopmentCards.json"));
            developmentCards = gson.fromJson(reader, DevelopmentCard[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DevelopmentCard[] getDevelopmentCards(){
        return developmentCards;
    }
}
