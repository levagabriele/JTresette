package it.uniroma1.mdp.jtresette.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingUtilities;

import it.uniroma1.mdp.jtresette.model.event.GameEvent;

/**
 * Classe base Observable per il pattern Observer/Observable.
 * Gestisce la lista di observer e la notifica degli eventi.
 * Le notifiche vengono inviate sull'Event Dispatch Thread di Swing
 * per garantire thread-safety nella GUI.
 */
public abstract class GameObservable {

    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();

    /** Registra un observer. */
    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /** Rimuove un observer. */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifica tutti gli observer di un evento.
     * La notifica avviene sull'EDT (Event Dispatch Thread) di Swing.
     *
     * @param event l'evento da notificare
     */
    protected void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            SwingUtilities.invokeLater(() -> observer.onGameEvent(event));
        }
    }

    /** Restituisce il numero di observer registrati. */
    public int getObserverCount() {
        return observers.size();
    }
}
