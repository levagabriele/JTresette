package it.uniroma1.mdp.jtresette.model.event;

/**
 * Interfaccia sealed per tutti gli eventi di gioco.
 * Usa il pattern matching di JDK 21 per il dispatch negli observer.
 */
public sealed interface GameEvent
        permits CardPlayedEvent, TrickWonEvent, RoundOverEvent, GameOverEvent,
                DealEvent, DeclarationEvent, PhaseChangedEvent, ScoreUpdatedEvent {
}
