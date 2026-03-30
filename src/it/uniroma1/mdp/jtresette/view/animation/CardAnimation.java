package it.uniroma1.mdp.jtresette.view.animation;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Animazione di una carta che scivola da un punto A a un punto B
 * con funzione di easing (ease-in-out).
 */
public class CardAnimation {

    private final BufferedImage immagine;
    private final Point partenza;
    private final Point arrivo;
    private final long durataMs;
    private final long tempoInizio;
    private final Runnable onComplete;
    private boolean completata = false;

    public CardAnimation(BufferedImage immagine, Point partenza, Point arrivo,
                         long durataMs, Runnable onComplete) {
        this.immagine = immagine;
        this.partenza = partenza;
        this.arrivo = arrivo;
        this.durataMs = durataMs;
        this.tempoInizio = System.currentTimeMillis();
        this.onComplete = onComplete;
    }

    /**
     * Aggiorna lo stato dell'animazione.
     *
     * @return true se l'animazione e' ancora in corso
     */
    public boolean update() {
        if (completata) return false;
        long elapsed = System.currentTimeMillis() - tempoInizio;
        if (elapsed >= durataMs) {
            completata = true;
            if (onComplete != null) {
                onComplete.run();
            }
            return false;
        }
        return true;
    }

    /** Posizione corrente della carta con easing ease-in-out. */
    public Point getPosizioneCorrente() {
        double t = Math.min(1.0, (double) (System.currentTimeMillis() - tempoInizio) / durataMs);
        double eased = easeInOut(t);
        int x = (int) (partenza.x + (arrivo.x - partenza.x) * eased);
        int y = (int) (partenza.y + (arrivo.y - partenza.y) * eased);
        return new Point(x, y);
    }

    /** Funzione ease-in-out cubica. */
    private double easeInOut(double t) {
        if (t < 0.5) {
            return 4 * t * t * t;
        } else {
            return 1 - Math.pow(-2 * t + 2, 3) / 2;
        }
    }

    public BufferedImage getImmagine() { return immagine; }
    public boolean isCompletata() { return completata; }
}
