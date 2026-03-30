package it.uniroma1.mdp.jtresette.view.animation;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Gestore delle animazioni. Usa un Timer Swing per aggiornare
 * e disegnare tutte le animazioni attive a ~60 FPS.
 */
public class AnimationManager {

    private static AnimationManager instance;

    private final List<CardAnimation> animazioni = new ArrayList<>();
    private final Timer timer;
    private JPanel targetPanel;

    private AnimationManager() {
        timer = new Timer(Constants.ANIMATION_DELAY_MS, e -> tick());
        timer.start();
    }

    public static synchronized AnimationManager getInstance() {
        if (instance == null) {
            instance = new AnimationManager();
        }
        return instance;
    }

    /** Imposta il pannello su cui disegnare le animazioni. */
    public void setTargetPanel(JPanel panel) {
        this.targetPanel = panel;
    }

    /** Aggiunge un'animazione. */
    public void aggiungi(CardAnimation animazione) {
        synchronized (animazioni) {
            animazioni.add(animazione);
        }
    }

    /** Aggiornamento periodico. */
    private void tick() {
        synchronized (animazioni) {
            Iterator<CardAnimation> it = animazioni.iterator();
            while (it.hasNext()) {
                CardAnimation anim = it.next();
                if (!anim.update()) {
                    it.remove();
                }
            }
        }
        if (targetPanel != null && !animazioni.isEmpty()) {
            targetPanel.repaint();
        }
    }

    /** Disegna tutte le animazioni attive. Chiamato dal paintComponent del pannello. */
    public void disegna(Graphics2D g2d) {
        synchronized (animazioni) {
            for (CardAnimation anim : animazioni) {
                Point pos = anim.getPosizioneCorrente();
                if (anim.getImmagine() != null) {
                    g2d.drawImage(anim.getImmagine(), pos.x, pos.y, null);
                }
            }
        }
    }

    /** Verifica se ci sono animazioni in corso. */
    public boolean isAnimating() {
        synchronized (animazioni) {
            return !animazioni.isEmpty();
        }
    }

    /** Rimuove tutte le animazioni. */
    public void pulisci() {
        synchronized (animazioni) {
            animazioni.clear();
        }
    }
}
