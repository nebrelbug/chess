package ui;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

    private static final String UNICODE_ESCAPE = "\u001b";

    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";

    public static final String SET_TEXT_COLOR_BLACK = SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_COLOR_RED = SET_TEXT_COLOR + "160m";
    public static final String SET_TEXT_COLOR_GREEN = SET_TEXT_COLOR + "46m";
    public static final String SET_TEXT_COLOR_YELLOW = SET_TEXT_COLOR + "226m";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String SET_TEXT_COLOR_MAGENTA = SET_TEXT_COLOR + "5m";
    public static final String SET_TEXT_COLOR_WHITE = SET_TEXT_COLOR + "15m";
    public static final String RESET_TEXT_COLOR = SET_TEXT_COLOR + "0m";

    public static final String SET_BG_COLOR_LIGHT_GREY = SET_BG_COLOR + "242m";
    //    public static final String SET_BG_COLOR_OFF_WHITE = SET_BG_COLOR + "249m";
    //    public static final String SET_BG_COLOR_DARK_RED = SET_BG_COLOR + "52m";
    public static final String SET_BG_COLOR_GREEN = SET_BG_COLOR + "34m";
    public static final String SET_BG_COLOR_DARK_GREEN = SET_BG_COLOR + "22m";
    public static final String SET_BG_COLOR_BLUE = SET_BG_COLOR + "33m";
    public static final String SET_BG_COLOR_DARK_BLUE = SET_BG_COLOR + "20m";
    public static final String RESET_BG_COLOR = SET_BG_COLOR + "0m";

}