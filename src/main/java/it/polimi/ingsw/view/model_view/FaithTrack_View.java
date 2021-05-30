package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.view.ColorAnsi;

import java.util.ArrayList;

public class FaithTrack_View {

    private ArrayList<String[][]> faithTracks;

    public FaithTrack_View(int numPlayers, ArrayList<String> nickNames) {
        faithTracks = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            faithTracks.add(new String[5][52]);
            createFaithTracks(i, nickNames.get(i));
        }
    }

    private void createFaithTracks(int player, String nickName){
        String[][] faithTrack = faithTracks.get(player);
        for(int y=0;y<51;y+=2) {
            if ((y>=10 && y<=14) || (y>=24 && y<=30 ) || (y>=38 && y<=46))
                faithTrack[2][y]=ColorAnsi.ANSI_RED.escape()+"║"+ColorAnsi.RESET;
            else if((y>=16 && y<=18) || (y>=32 && y<=34 ) || (y>=48 && y<=50))
                faithTrack[2][y]=ColorAnsi.ANSI_YELLOW.escape()+"║"+ColorAnsi.RESET;
            else
                faithTrack[2][y]="║";
        }
        for (int x=1;x<5;x+=2) {
            for (int y = 1; y < 50; y+=2) {
                if ((y >= 10 && y < 16) || (y >= 24 && y < 32) || (y >= 38 && y < 48))
                    faithTrack[x][y] = ColorAnsi.ANSI_RED.escape() + "═" + ColorAnsi.RESET;
                else if((y >= 16 && y <= 18) || (y >= 32 && y <= 34) || y>= 48)
                    faithTrack[x][y] = ColorAnsi.ANSI_YELLOW.escape() + "═" + ColorAnsi.RESET;
                else
                    faithTrack[x][y] = "═";
            }
        }
        for(int y = 2; y  < 49; y +=2){
            if ((y >= 10 && y < 16) || (y >= 24 && y < 32) || (y >= 38 && y < 48))
                faithTrack[1][y] = ColorAnsi.ANSI_RED.escape() + "╦" + ColorAnsi.RESET;
            else if((y >= 16 && y <= 18) || (y >= 32 && y <= 34) || y>= 48)
                faithTrack[1][y] = ColorAnsi.ANSI_YELLOW.escape() + "╦" + ColorAnsi.RESET;
            else
                faithTrack[1][y] = "╦";
        }
        for(int y = 2; y  < 49; y +=2){
            if ((y >= 10 && y < 16) || (y >= 24 && y < 32) || (y >= 38 && y < 48))
                faithTrack[3][y] = ColorAnsi.ANSI_RED.escape() + "╩" + ColorAnsi.RESET;
            else if((y >= 16 && y <= 18) || (y >= 32 && y <= 34) || y>= 48)
                faithTrack[3][y] = ColorAnsi.ANSI_YELLOW.escape() + "╩" + ColorAnsi.RESET;
            else
                faithTrack[3][y] = "╩";
        }
        faithTrack[1][0]="╔";
        faithTrack[3][0]="╚";
        faithTrack[1][50]=ColorAnsi.ANSI_YELLOW.escape()+"╗"+ColorAnsi.RESET;
        faithTrack[3][50]=ColorAnsi.ANSI_YELLOW.escape()+"╝"+ColorAnsi.RESET;
        for (int y=0;y<51;y++) {
            if(y!=7&&y!=13&&y!=19&&y!=25&&y!=31&&y!=36&&y!=37&&y!=42&&y!=43&&y!=48&&y!=49)
                faithTrack[0][y]=" ";
        }
        for (int y=1;y<51;y+=2) {
            faithTrack[2][y] = " ";
            faithTrack[4][y] = " ";
            faithTrack[4][y + 1] = " ";
        }
        faithTrack[0][7]=ColorAnsi.ANSI_CYAN.escape() + "1" + ColorAnsi.RESET;
        faithTrack[0][13]=ColorAnsi.ANSI_CYAN.escape() + "2" + ColorAnsi.RESET;
        faithTrack[0][19]=ColorAnsi.ANSI_CYAN.escape() + "4" + ColorAnsi.RESET;
        faithTrack[0][25]=ColorAnsi.ANSI_CYAN.escape() + "6" + ColorAnsi.RESET;
        faithTrack[0][31]=ColorAnsi.ANSI_CYAN.escape() + "9" + ColorAnsi.RESET;
        faithTrack[0][36]=ColorAnsi.ANSI_CYAN.escape() + "1" + ColorAnsi.RESET;
        faithTrack[0][37]=ColorAnsi.ANSI_CYAN.escape() + "2" + ColorAnsi.RESET;
        faithTrack[0][42]=ColorAnsi.ANSI_CYAN.escape() + "1" + ColorAnsi.RESET;
        faithTrack[0][43]=ColorAnsi.ANSI_CYAN.escape() + "6" + ColorAnsi.RESET;
        faithTrack[0][48]=ColorAnsi.ANSI_CYAN.escape() + "2" + ColorAnsi.RESET;
        faithTrack[0][49]=ColorAnsi.ANSI_CYAN.escape() + "0" + ColorAnsi.RESET;
        faithTrack[2][1] = playerColor(player) + "●"+ColorAnsi.RESET;
        faithTrack[2][51] = nickName;
        faithTrack[4][0] = " ";
    }

    private String playerColor(int player){
        switch (player){
            case 0:
                return ColorAnsi.ANSI_CYAN.escape();
            case 1:
                return ColorAnsi.ANSI_GREEN.escape();
            case 2:
                return ColorAnsi.ANSI_RED.escape();
            case 3:
                return ColorAnsi.ANSI_YELLOW.escape();
        }
        return null;
    }

    public void increaseFaithPoints(int player, int newFaithPoints){
        String[][] faithTrack = faithTracks.get(player);
        for (int y=1;y<51;y+=2) {
            faithTrack[2][y] = " ";
        }
        faithTrack[2][newFaithPoints*2 + 1] = playerColor(player) + "●" + ColorAnsi.RESET;
    }

    public void increaseVictoryPoints(int player, int victoryPoints){
        String[][] faithTrack = faithTracks.get(player);
        if(!faithTrack[4][33].equals(" ")){
            int newPoints = victoryPoints - Integer.parseInt(faithTrack[4][33]) - Integer.parseInt(faithTrack[4][17]);
            faithTrack[4][49] = "" + newPoints;
        }
        else if(!faithTrack[4][17].equals(" ")){
            int newPoints = victoryPoints - Integer.parseInt(faithTrack[4][17]);
            faithTrack[4][33] = "" + newPoints;
        }
        else {
            faithTrack[4][17] = "" + victoryPoints;
        }
    }

    public void printCli(){
        System.out.println("\nFAITH TRACK:\n");
        for(int i = 0; i < faithTracks.size(); i++)
            printFaithTrack(i);
    }

    private void printFaithTrack(int player){
        String[][] faithTrack = faithTracks.get(player);
        for (int x=0;x<5;x++) {
            for (int y = 0; y < 51; y++) {
                if(x == 4 && y == 17){
                    System.out.println(ColorAnsi.ANSI_YELLOW.escape() + faithTrack[x][y] + ColorAnsi.RESET);
                }
                else if(x == 4 && y == 33){
                    System.out.println(ColorAnsi.ANSI_YELLOW.escape() + faithTrack[x][y] + ColorAnsi.RESET);
                }
                else if(x == 4 && y == 49){
                    System.out.println(ColorAnsi.ANSI_YELLOW.escape() + faithTrack[x][y] + ColorAnsi.RESET);
                }
                else
                    System.out.print(faithTrack[x][y]);
            }
            if(x == 2)
                System.out.println("    " + faithTrack[x][51]);
            else
                System.out.println();
        }
    }
}
