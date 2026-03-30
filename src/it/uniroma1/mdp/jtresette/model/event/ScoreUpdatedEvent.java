package it.uniroma1.mdp.jtresette.model.event;

/**
 * Evento emesso quando i punteggi vengono aggiornati.
 */
public record ScoreUpdatedEvent(int[] punteggi) implements GameEvent {
}
