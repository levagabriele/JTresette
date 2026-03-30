package it.uniroma1.mdp.jtresette.audio;

/**
 * Enum che rappresenta gli effetti sonori del gioco.
 * Ogni effetto ha un path alla risorsa audio.
 */
public enum SoundEffect {
    CARD_PLAY("resources/audio/card_play.wav"),
    CARD_DEAL("resources/audio/card_deal.wav"),
    TRICK_WIN("resources/audio/trick_win.wav"),
    GAME_WIN("resources/audio/game_win.wav"),
    GAME_LOSE("resources/audio/game_lose.wav"),
    BUTTON_CLICK("resources/audio/button_click.wav"),
    DECLARATION("resources/audio/declaration.wav");

    private final String path;

    SoundEffect(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
