package it.polimi.ingsw.view.CLI;

public enum ColorAnsi {
    ANSI_RED("\033[0;31m"),
    ANSI_CYAN ( "\033[0;36m"),
    ANSI_YELLOW("\033[0;33m"),
    ANSI_WHITE ("\033[0;37m"),
    ANSI_PURPLE ("\033[0;35m"),
    ANSI_GREEN("\033[0;32m");

    public static final String RESET = "\u001B[0m";

    private String escape;

    ColorAnsi(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }
}
