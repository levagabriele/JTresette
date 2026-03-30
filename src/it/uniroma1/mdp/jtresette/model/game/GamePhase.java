package it.uniroma1.mdp.jtresette.model.game;

/**
 * Fasi di gioco del Tresette (State pattern).
 * Controlla quali azioni sono lecite in ogni momento.
 */
public enum GamePhase {
    SETUP("Configurazione"),
    DEALING("Distribuzione carte"),
    DECLARING("Dichiarazioni"),
    PLAYING_TURN("Gioco in corso"),
    TRICK_COMPLETE("Turno completato"),
    ROUND_COMPLETE("Mano completata"),
    GAME_OVER("Fine partita");

    private final String descrizione;

    GamePhase(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String toString() {
        return descrizione;
    }
}
