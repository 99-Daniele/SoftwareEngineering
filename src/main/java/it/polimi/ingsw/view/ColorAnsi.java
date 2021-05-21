package it.polimi.ingsw.view;

public enum ColorAnsi {
    ANSI_RED("\u001B[31m"),
    ANSI_CYAN ( "\u001B[36m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_WHITE ("\u001B[37m"),
    ANSI_PURPLE ("\u001B[35m"),
    ANSI_GREEN("\u001B[32m");

    public static final String RESET = "\u001B[0m";

    private String escape;

    ColorAnsi(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }
}


/*
public static final String ANSI_BLACK = "\u001B[30m";

    ANSI_BLUE("\u001B[34m");

    */
