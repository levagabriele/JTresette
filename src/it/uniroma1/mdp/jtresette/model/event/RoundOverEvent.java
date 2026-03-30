package it.uniroma1.mdp.jtresette.model.event;

/**
 * Evento emesso alla fine di una mano (round).
 */
public record RoundOverEvent(int[] puntiMano, int[] punteggiTotali, int numeroMano) implements GameEvent {
}
