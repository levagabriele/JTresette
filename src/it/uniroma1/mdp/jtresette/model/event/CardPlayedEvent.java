package it.uniroma1.mdp.jtresette.model.event;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.player.Giocatore;

/**
 * Evento emesso quando un giocatore gioca una carta.
 */
public record CardPlayedEvent(Giocatore giocatore, Carta carta, int indiceGiocatore) implements GameEvent {
}
