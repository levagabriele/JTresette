package it.uniroma1.mdp.jtresette.model;

import it.uniroma1.mdp.jtresette.model.event.GameEvent;

/**
 * Interfaccia Observer per il pattern Observer/Observable.
 * Implementata dalla View per ricevere notifiche dal Model.
 */
public interface GameObserver {

    /**
     * Chiamato dal Model quando si verifica un evento di gioco.
     *
     * @param event l'evento che si e' verificato
     */
    void onGameEvent(GameEvent event);
}
