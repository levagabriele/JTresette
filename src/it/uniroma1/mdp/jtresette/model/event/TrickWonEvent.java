package it.uniroma1.mdp.jtresette.model.event;

import java.util.List;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.player.Giocatore;

/**
 * Evento emesso quando un turno (trick) viene vinto.
 */
public record TrickWonEvent(Giocatore vincitore, int indiceVincitore,
                            List<Carta> carteVinte, int puntiTerzi) implements GameEvent {
}
