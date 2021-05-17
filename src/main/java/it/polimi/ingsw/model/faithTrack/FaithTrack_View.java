package it.polimi.ingsw.model.faithTrack;

import java.util.ArrayList;

public class FaithTrack_View {

    private final int[] sections;
    private final int[] vaticanSections;
    private ArrayList<Integer> playersFaithPoints;

    public FaithTrack_View(int numPlayers) {
        sections = new int[]{3, 6, 9, 12, 15, 18, 21, 24};
        vaticanSections = new int[]{8, 16, 24};
        playersFaithPoints = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++)
            playersFaithPoints.add(0);
    }
}
