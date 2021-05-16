package it.polimi.ingsw.parser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.cards.leaderCards.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LeaderCardsParser {

    private ArrayList<LeaderCard> leaderCards = new ArrayList<>(16);

    public LeaderCardsParser() {
        try {
            Gson gson = new Gson();
            JsonReader reader1 = new JsonReader(new FileReader("src/main/resources/DiscountCards.json"));
            LeaderCard[] discountCards = gson.fromJson(reader1, DiscountCard[].class);
            JsonReader reader2 = new JsonReader(new FileReader("src/main/resources/ExtraDepotCards.json"));
            LeaderCard[] extraDepotCards = gson.fromJson(reader2, ExtraDepotCard[].class);
            JsonReader reader3 = new JsonReader(new FileReader("src/main/resources/WhiteConversionCards.json"));
            LeaderCard[] whiteConversionCards = gson.fromJson(reader3, WhiteConversionCard[].class);
            JsonReader reader4 = new JsonReader(new FileReader("src/main/resources/AdditionalProductionPowerCards.json"));
            LeaderCard[] additionalProductionPowerCards = gson.fromJson(reader4, AdditionalProductionPowerCard[].class);
            leaderCards.addAll(Arrays.asList(discountCards).subList(0, 4));
            leaderCards.addAll(Arrays.asList(extraDepotCards).subList(0, 4));
            leaderCards.addAll(Arrays.asList(whiteConversionCards).subList(0, 4));
            leaderCards.addAll(Arrays.asList(additionalProductionPowerCards).subList(0, 4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

}
