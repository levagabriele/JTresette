package it.uniroma1.mdp.jtresette.model.event;

/**
 * Evento emesso quando le carte vengono distribuite.
 */
public record DealEvent(int cartePerGiocatore, int numeroMano) implements GameEvent {
}
