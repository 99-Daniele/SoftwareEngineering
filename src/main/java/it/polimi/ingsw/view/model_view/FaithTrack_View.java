package it.polimi.ingsw.view.model_view;

import java.util.ArrayList;

public class FaithTrack_View {

    private final int[] sections;
    private final int[][] vaticanSections;
    private final int[] popeSpaces;
    private ArrayList<Integer> playersFaithPoints;

    public FaithTrack_View(int numPlayers) {
        sections = new int[]{3, 6, 9, 12, 15, 18, 21, 24};
        vaticanSections = new int[][]{{5, 8}, {12, 16}, {19, 24}};
        popeSpaces = new int[]{8, 16, 24};
        playersFaithPoints = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++)
            playersFaithPoints.add(0);
    }

    public void increaseFaithPoints(int numPlayer, int faithPoints){
        playersFaithPoints.set(numPlayer, faithPoints);
    }

    public void print(){};
}
