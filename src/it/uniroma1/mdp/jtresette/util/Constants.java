package it.uniroma1.mdp.jtresette.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Costanti condivise dell'applicazione.
 */
public final class Constants {

    private Constants() {}

    // Dimensioni finestra
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 800;
    public static final Dimension WINDOW_SIZE = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

    // Dimensioni carte
    public static final int CARD_WIDTH = 100;
    public static final int CARD_HEIGHT = 160;

    // Colori
    public static final Color TABLE_GREEN = new Color(0, 100, 0);
    public static final Color TABLE_GREEN_DARK = new Color(0, 80, 0);
    public static final Color GOLD = new Color(218, 165, 32);
    public static final Color MENU_BG = new Color(25, 25, 60);
    public static final Color CARD_HIGHLIGHT = new Color(255, 255, 100, 120);
    public static final Color TEXT_WHITE = new Color(240, 240, 240);
    public static final Color PANEL_BG = new Color(0, 0, 0, 120);

    // Font
    public static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 48);
    public static final Font SUBTITLE_FONT = new Font("Serif", Font.BOLD, 24);
    public static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font SCORE_FONT = new Font("SansSerif", Font.BOLD, 14);

    // Animazioni
    public static final int ANIMATION_FPS = 60;
    public static final int ANIMATION_DELAY_MS = 1000 / ANIMATION_FPS;
    public static final int CARD_SLIDE_DURATION_MS = 400;
    public static final int DEAL_CARD_DELAY_MS = 100;
    public static final int TRICK_COLLECT_DELAY_MS = 800;
    public static final int AI_THINK_DELAY_MS = 600;

    // Schermate (nomi per CardLayout)
    public static final String SCREEN_MENU = "MENU";
    public static final String SCREEN_PROFILE = "PROFILE";
    public static final String SCREEN_SETUP = "SETUP";
    public static final String SCREEN_GAME = "GAME";
    public static final String SCREEN_GAME_OVER = "GAME_OVER";

    // Risorse
    public static final String RESOURCES_PATH = "resources/";
    public static final String CARDS_PATH = RESOURCES_PATH + "images/cards/";
    public static final String AVATARS_PATH = RESOURCES_PATH + "images/avatars/";
    public static final String AUDIO_PATH = RESOURCES_PATH + "audio/";
    public static final String PROFILE_PATH = RESOURCES_PATH + "data/profile.json";
    public static final String CARD_BACK_PATH = CARDS_PATH + "dorso.png";

    // Gioco
    public static final int OBIETTIVO_PUNTI_DEFAULT = 21;
    public static final int OBIETTIVO_TERZI_DEFAULT = 63;
}
