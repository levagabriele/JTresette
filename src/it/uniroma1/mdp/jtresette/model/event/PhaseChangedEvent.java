package it.uniroma1.mdp.jtresette.model.event;

import it.uniroma1.mdp.jtresette.model.game.GamePhase;

/**
 * Evento emesso quando la fase di gioco cambia.
 */
public record PhaseChangedEvent(GamePhase vecchiaFase, GamePhase nuovaFase) implements GameEvent {
}
