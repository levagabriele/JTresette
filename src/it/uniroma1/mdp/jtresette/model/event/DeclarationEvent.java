package it.uniroma1.mdp.jtresette.model.event;

import java.util.List;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.game.Dichiarazione;
import it.uniroma1.mdp.jtresette.model.player.Giocatore;

/**
 * Evento emesso quando un giocatore fa una dichiarazione.
 */
public record DeclarationEvent(Giocatore giocatore, Dichiarazione dichiarazione,
                               List<Carta> carteCoinvolte) implements GameEvent {
}
