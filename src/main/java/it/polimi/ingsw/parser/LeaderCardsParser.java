package it.polimi.ingsw.parser;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.leaderCards.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LeaderCardsParser {

    private final ArrayList<LeaderCard> leaderCards = new ArrayList<>(16);

    public LeaderCardsParser() {
        Gson gson = new Gson();
        Reader reader1 = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/DiscountCards.json")), StandardCharsets.UTF_8);
        LeaderCard[] discountCards = gson.fromJson(reader1, DiscountCard[].class);
        Reader reader2 = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/ExtraDepotCards.json")), StandardCharsets.UTF_8);
        LeaderCard[] extraDepotCards = gson.fromJson(reader2, ExtraDepotCard[].class);
        Reader reader3 = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/WhiteConversionCards.json")), StandardCharsets.UTF_8);
        LeaderCard[] whiteConversionCards = gson.fromJson(reader3, WhiteConversionCard[].class);
        Reader reader4 = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/AdditionalProductionPowerCards.json")), StandardCharsets.UTF_8);
        LeaderCard[] additionalProductionPowerCards = gson.fromJson(reader4, AdditionalProductionPowerCard[].class);
        leaderCards.addAll(Arrays.asList(discountCards).subList(0, 4));
        leaderCards.addAll(Arrays.asList(extraDepotCards).subList(0, 4));
        leaderCards.addAll(Arrays.asList(whiteConversionCards).subList(0, 4));
        leaderCards.addAll(Arrays.asList(additionalProductionPowerCards).subList(0, 4));
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

}
