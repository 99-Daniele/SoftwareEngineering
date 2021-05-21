package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.view.ColorAnsi;

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

    public void printCli(){
        int count=1;
        String s=new String();
        System.out.println("TRACCIATO FEDE GIOCATORI");
        System.out.println("disposti in ordine di posizione dei giocatori");
        for (Integer i:playersFaithPoints)
        {
            int val=i.intValue();
            switch (count){
                case 1:
                    s=new String(ColorAnsi.ANSI_CYAN.escape());
                    break;
                case 2:
                    s=new String(ColorAnsi.ANSI_GREEN.escape());
                    break;
                case 3:
                    s=new String(ColorAnsi.ANSI_PURPLE.escape());
                    break;
                case 4:
                    s=new String(ColorAnsi.ANSI_YELLOW.escape());
                    break;
            }
            representationFaithTrack(val,s);
            count++;
        }
    }

    private void representationFaithTrack(int val, String color){
        String tabella[][]=new String[5][51];
        for(int y=0;y<51;y+=2)
        {
            if (y==10||y==18||y==24||y==34||y==38||y==50)
                tabella[2][y]=ColorAnsi.ANSI_RED.escape()+"║"+ColorAnsi.RESET;
            else
                tabella[2][y]="║";
        }
        for (int x=1;x<5;x+=2)
            for (int y=1;y<50;y++)
                if ((y>=10&&y<19)||(y>=24&&y<=34)||(y>=38&&y<=50))
                    tabella[x][y]=ColorAnsi.ANSI_RED.escape()+"═"+ColorAnsi.RESET;
                else
                    tabella[x][y]="═";
        tabella[1][0]="╔";
        tabella[3][0]="╚";
        tabella[1][50]=ColorAnsi.ANSI_RED.escape()+"╗"+ColorAnsi.RESET;
        tabella[3][50]=ColorAnsi.ANSI_RED.escape()+"╝"+ColorAnsi.RESET;
        for (int y=0;y<51;y++)
        {
            if(y!=7&&y!=13&&y!=19&&y!=25&&y!=31&&y!=36&&y!=37&&y!=42&&y!=43&&y!=48&&y!=49)
                tabella[0][y]=" ";
            if(y!=17&&y!=33&&y!=49)
                tabella[4][y]=" ";
        }
        tabella[0][7]="1";
        tabella[0][13]="2";
        tabella[0][19]="4";
        tabella[0][25]="6";
        tabella[0][31]="9";
        tabella[0][36]="1";
        tabella[0][37]="2";
        tabella[0][42]="1";
        tabella[0][43]="6";
        tabella[0][48]="2";
        tabella[0][49]="0";
        tabella[4][17]="P";
        tabella[4][33]="P";
        tabella[4][49]="P";
        int i=0;
        for (int y=1;y<51;y+=2)
        {
            if(i==val)
                tabella[2][y]=color+"●"+ColorAnsi.RESET;
            else
                tabella[2][y]=" ";
            i++;
        }


        for (int x=0;x<5;x++)
        {
            for (int y = 0; y < 51; y++)
                System.out.print(tabella[x][y]);
            System.out.println();
        }
    }
}
