package it.uniroma1.mdp.jtresette.model.event;

import it.uniroma1.mdp.jtresette.model.player.Giocatore;

/**
 * Evento emesso alla fine della partita.
 */
public record GameOverEvent(Giocatore vincitore, int indiceVincitore, int[] punteggiFinali) implements GameEvent {
}
