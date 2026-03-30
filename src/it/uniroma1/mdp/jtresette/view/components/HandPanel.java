package it.uniroma1.mdp.jtresette.view.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.render.CardImageLoader;

/**
 * Pannello che mostra la mano del giocatore umano.
 * Le carte sono disposte a ventaglio e cliccabili.
 */
public class HandPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<Carta> carte = new ArrayList<>();
    private List<Carta> carteValide = new ArrayList<>();
    private int cartaHover = -1;
    private boolean inputAbilitato = false;
    private Consumer<Carta> onCartaCliccata;

    public HandPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(Constants.WINDOW_WIDTH, Constants.CARD_HEIGHT + 40));

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int nuovaHover = getCartaAt(e.getPoint());
                if (nuovaHover != cartaHover) {
                    cartaHover = nuovaHover;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!inputAbilitato || onCartaCliccata == null) return;
                int idx = getCartaAt(e.getPoint());
                if (idx >= 0 && idx < carte.size()) {
                    Carta carta = carte.get(idx);
                    if (carteValide.contains(carta)) {
                        onCartaCliccata.accept(carta);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cartaHover = -1;
                repaint();
            }
        });
    }

    /** Aggiorna le carte nella mano. */
    public void setCarte(List<Carta> carte) {
        this.carte = new ArrayList<>(carte);
        this.cartaHover = -1;
        repaint();
    }

    /** Imposta quali carte sono giocabili (le altre saranno oscurate). */
    public void setCarteValide(List<Carta> valide) {
        this.carteValide = new ArrayList<>(valide);
        repaint();
    }

    /** Abilita/disabilita l'input del giocatore. */
    public void setInputAbilitato(boolean abilitato) {
        this.inputAbilitato = abilitato;
        repaint();
    }

    /** Registra il callback per quando una carta viene cliccata. */
    public void setOnCartaCliccata(Consumer<Carta> callback) {
        this.onCartaCliccata = callback;
    }

    /** Determina quale carta e' sotto il punto dato. */
    private int getCartaAt(Point p) {
        if (carte.isEmpty()) return -1;
        int spacing = calcolaSpacing();
        int startX = calcolaStartX(spacing);

        // Controlla dall'ultima (sopra) alla prima
        for (int i = carte.size() - 1; i >= 0; i--) {
            int x = startX + i * spacing;
            int y = getYForCard(i);
            if (p.x >= x && p.x <= x + Constants.CARD_WIDTH &&
                p.y >= y && p.y <= y + Constants.CARD_HEIGHT) {
                return i;
            }
        }
        return -1;
    }

    private int calcolaSpacing() {
        if (carte.size() <= 1) return 0;
        int maxWidth = getWidth() - Constants.CARD_WIDTH - 40;
        return Math.min(Constants.CARD_WIDTH - 20, maxWidth / (carte.size() - 1));
    }

    private int calcolaStartX(int spacing) {
        int totalWidth = (carte.size() - 1) * spacing + Constants.CARD_WIDTH;
        return (getWidth() - totalWidth) / 2;
    }

    private int getYForCard(int index) {
        int baseY = 20;
        if (index == cartaHover && inputAbilitato) {
            Carta c = carte.get(index);
            if (carteValide.contains(c)) {
                return baseY - 20; // Alza la carta in hover
            }
        }
        return baseY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (carte.isEmpty()) return;

        CardImageLoader loader = CardImageLoader.getInstance();
        int spacing = calcolaSpacing();
        int startX = calcolaStartX(spacing);

        for (int i = 0; i < carte.size(); i++) {
            Carta carta = carte.get(i);
            int x = startX + i * spacing;
            int y = getYForCard(i);

            BufferedImage img = loader.getImmagine(carta);
            if (img != null) {
                // Oscura carte non valide
                if (inputAbilitato && !carteValide.contains(carta)) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
                g2d.drawImage(img, x, y, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                // Bordo highlight su hover
                if (i == cartaHover && inputAbilitato && carteValide.contains(carta)) {
                    g2d.setColor(Constants.CARD_HIGHLIGHT);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(x - 1, y - 1, Constants.CARD_WIDTH + 1, Constants.CARD_HEIGHT + 1, 8, 8);
                }
            }
        }
    }
}
