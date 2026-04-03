package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.StyledButton;

/**
 * Schermata di fine partita con risultati, punteggi e barra XP.
 * Include animazioni d'ingresso, barra XP animata e coriandoli sulla vittoria.
 */
public class GameOverScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel lblRisultato;
    private final JLabel lblSottotitolo;
    private final JPanel punteggiPanel;
    private final JPanel xpPanel;
    private final StyledButton btnNuovaPartita;
    private final StyledButton btnMenu;

    // Dati XP (aggiornati da mostraRisultati)
    private int xpGuadagnati;
    private int xpNelLivello;
    private int xpPerLivello;
    private int livello;

    // Animazione barra XP
    private float xpProgressoAnimato = 0f;
    private float xpProgressoTarget = 0f;
    private Timer xpAnimTimer;

    // Animazioni d'ingresso
    private float titoloScale = 0f;
    private float sottotitoloAlpha = 0f;
    private float punteggiAlpha = 0f;
    private float xpAlpha = 0f;
    private float bottoniAlpha = 0f;
    private Timer entrataTimer;
    private long entrataInizio;

    // Coriandoli
    private boolean mostraCoriandoli = false;
    private final List<Coriandolo> coriandoli = new ArrayList<>();
    private Timer coriandoliTimer;
    private static final int NUM_CORIANDOLI = 80;
    private static final Random RANDOM = new Random();
    private static final Color[] COLORI_CORIANDOLI = {
        new Color(255, 215, 0),   // oro
        new Color(40, 160, 70),   // verde
        new Color(65, 135, 245),  // blu
        new Color(255, 85, 85),   // rosso
        new Color(200, 130, 255), // viola
        new Color(255, 180, 50),  // arancione
        Color.WHITE
    };

    public GameOverScreen() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);

        lblRisultato = new JLabel("", SwingConstants.CENTER);
        lblRisultato.setFont(new Font("Segoe UI", Font.BOLD, 48));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        add(lblRisultato, gbc);

        lblSottotitolo = new JLabel("", SwingConstants.CENTER);
        lblSottotitolo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSottotitolo.setForeground(Constants.TEXT_MUTED);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(lblSottotitolo, gbc);

        // Pannello punteggi
        punteggiPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(new Color(80, 80, 90, 60));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        punteggiPanel.setOpaque(false);
        punteggiPanel.setLayout(new BoxLayout(punteggiPanel, BoxLayout.Y_AXIS));
        punteggiPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        punteggiPanel.setPreferredSize(new Dimension(350, 150));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        add(punteggiPanel, gbc);

        // Pannello XP
        xpPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                disegnaXpBar(g2d, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        xpPanel.setOpaque(false);
        xpPanel.setPreferredSize(new Dimension(380, 75));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 25, 0);
        add(xpPanel, gbc);

        // Bottoni
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBottoni.setOpaque(false);

        btnMenu = new StyledButton("Menu");
        btnMenu.setPreferredSize(new Dimension(160, 45));
        btnNuovaPartita = StyledButton.verde("Nuova Partita");
        btnNuovaPartita.setPreferredSize(new Dimension(200, 45));

        panelBottoni.add(btnMenu);
        panelBottoni.add(btnNuovaPartita);

        gbc.gridy = 4;
        add(panelBottoni, gbc);
    }

    public void mostraRisultati(String nomeVincitore, boolean haVintoUmano,
                                int[] punteggi, String[] nomiGiocatori,
                                int xpGuadagnati, int xpNelLivello, int xpPerLivello, int livello) {
        this.xpGuadagnati = xpGuadagnati;
        this.xpNelLivello = xpNelLivello;
        this.xpPerLivello = xpPerLivello;
        this.livello = livello;

        if (haVintoUmano) {
            lblRisultato.setText("VITTORIA!");
            lblRisultato.setForeground(Constants.BTN_GREEN);
            lblSottotitolo.setText("Complimenti, hai vinto la partita!");
        } else {
            lblRisultato.setText("SCONFITTA");
            lblRisultato.setForeground(Constants.BTN_RED);
            lblSottotitolo.setText("Ha vinto " + nomeVincitore);
        }

        punteggiPanel.removeAll();
        JLabel header = new JLabel("  PUNTEGGI FINALI");
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Constants.TEXT_WHITE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        punteggiPanel.add(header);
        punteggiPanel.add(Box.createVerticalStrut(10));

        for (int i = 0; i < punteggi.length; i++) {
            JLabel lbl = new JLabel("  " + nomiGiocatori[i] + ":  " + RegoleTresette.formattaPunti(punteggi[i]) + " punti");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lbl.setForeground(i == 0 ? Constants.TEXT_WHITE : Constants.TEXT_MUTED);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            punteggiPanel.add(lbl);
            punteggiPanel.add(Box.createVerticalStrut(4));
        }
        punteggiPanel.revalidate();
        punteggiPanel.repaint();

        // Avvia animazioni
        avviaAnimazioniIngresso();
        avviaAnimazioneXp();
        if (haVintoUmano) {
            avviaCoriandoli();
        } else {
            fermaCoriandoli();
        }
    }

    // ==================== Animazione barra XP ====================

    private void avviaAnimazioneXp() {
        if (xpAnimTimer != null) xpAnimTimer.stop();
        xpProgressoAnimato = 0f;
        xpProgressoTarget = Math.min(1f, (float) xpNelLivello / xpPerLivello);

        // Ritardo iniziale: la barra parte dopo che il pannello XP e' visibile
        Timer delay = new Timer(1200, e -> {
            xpAnimTimer = new Timer(Constants.ANIMATION_DELAY_MS, ev -> {
                float velocita = 0.015f;
                xpProgressoAnimato += velocita;
                if (xpProgressoAnimato >= xpProgressoTarget) {
                    xpProgressoAnimato = xpProgressoTarget;
                    xpAnimTimer.stop();
                }
                xpPanel.repaint();
            });
            xpAnimTimer.start();
        });
        delay.setRepeats(false);
        delay.start();
    }

    // ==================== Animazioni d'ingresso ====================

    private void avviaAnimazioniIngresso() {
        if (entrataTimer != null) entrataTimer.stop();

        // Reset stato
        titoloScale = 0f;
        sottotitoloAlpha = 0f;
        punteggiAlpha = 0f;
        xpAlpha = 0f;
        bottoniAlpha = 0f;
        entrataInizio = System.currentTimeMillis();

        entrataTimer = new Timer(Constants.ANIMATION_DELAY_MS, e -> {
            long elapsed = System.currentTimeMillis() - entrataInizio;

            // Titolo: 0-400ms (scale da 0 a 1 con overshoot)
            titoloScale = animaValore(elapsed, 0, 400);

            // Sottotitolo: 200-500ms (fade in)
            sottotitoloAlpha = animaValore(elapsed, 200, 300);

            // Punteggi: 400-700ms (fade in)
            punteggiAlpha = animaValore(elapsed, 400, 300);

            // XP: 700-1000ms (fade in)
            xpAlpha = animaValore(elapsed, 700, 300);

            // Bottoni: 900-1200ms (fade in)
            bottoniAlpha = animaValore(elapsed, 900, 300);

            if (elapsed > 1200) {
                titoloScale = 1f;
                sottotitoloAlpha = 1f;
                punteggiAlpha = 1f;
                xpAlpha = 1f;
                bottoniAlpha = 1f;
                entrataTimer.stop();
            }

            repaint();
        });
        entrataTimer.start();
    }

    /** Calcola un valore 0..1 con easing per un'animazione che inizia a startMs e dura durationMs. */
    private float animaValore(long elapsed, int startMs, int durationMs) {
        if (elapsed < startMs) return 0f;
        float t = Math.min(1f, (float)(elapsed - startMs) / durationMs);
        // Ease-out cubic
        return 1f - (1f - t) * (1f - t) * (1f - t);
    }

    // ==================== Coriandoli ====================

    private void avviaCoriandoli() {
        fermaCoriandoli();
        mostraCoriandoli = true;
        coriandoli.clear();

        int w = Math.max(getWidth(), 800);
        for (int i = 0; i < NUM_CORIANDOLI; i++) {
            coriandoli.add(new Coriandolo(w));
        }

        coriandoliTimer = new Timer(Constants.ANIMATION_DELAY_MS, e -> {
            for (Coriandolo c : coriandoli) {
                c.aggiorna();
            }
            repaint();
        });
        coriandoliTimer.start();
    }

    private void fermaCoriandoli() {
        mostraCoriandoli = false;
        coriandoli.clear();
        if (coriandoliTimer != null) {
            coriandoliTimer.stop();
            coriandoliTimer = null;
        }
    }

    // ==================== Rendering ====================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sfondo gradient
        GradientPaint bg = new GradientPaint(0, 0, Constants.BG_DARK,
                0, getHeight(), Constants.BG_LIGHTER);
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Coriandoli (sotto i componenti Swing)
        if (mostraCoriandoli) {
            for (Coriandolo c : coriandoli) {
                c.disegna(g2d);
            }
        }

        g2d.dispose();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Applica alpha ai componenti figli tramite paint override
        Graphics2D g2d = (Graphics2D) g.create();

        // Disegna coriandoli sopra tutto
        if (mostraCoriandoli) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Coriandolo c : coriandoli) {
                c.disegna(g2d);
            }
        }

        g2d.dispose();
    }

    /** Disegna la barra XP con livelli. */
    private void disegnaXpBar(Graphics2D g2d, int w, int h) {
        int barH = 16;
        int barY = 22;
        int leftPad = 30;
        int rightPad = 30;
        int barW = w - leftPad - rightPad;

        // Stella livello corrente (sinistra)
        g2d.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
        g2d.setColor(Constants.GOLD);
        g2d.drawString("\u2605", 6, barY + barH / 2 + 4);
        g2d.setColor(Constants.TEXT_WHITE);
        g2d.drawString(String.valueOf(livello), 20, barY + barH / 2 + 4);

        // Sfondo barra
        g2d.setColor(new Color(50, 50, 55));
        g2d.fillRoundRect(leftPad, barY, barW, barH, 8, 8);

        // Progresso barra (animato)
        int barFill = (int) (barW * xpProgressoAnimato);
        if (barFill > 0) {
            // Gradiente sulla barra per effetto lucido
            GradientPaint barGrad = new GradientPaint(
                    leftPad, barY, Constants.BTN_GREEN,
                    leftPad, barY + barH, Constants.BTN_GREEN.darker());
            g2d.setPaint(barGrad);
            g2d.fillRoundRect(leftPad, barY, barFill, barH, 8, 8);
        }

        // Stella livello successivo (destra)
        g2d.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
        g2d.setColor(Constants.TEXT_MUTED);
        g2d.drawString("\u2605", w - rightPad + 6, barY + barH / 2 + 4);
        g2d.drawString(String.valueOf(livello + 1), w - rightPad + 20, barY + barH / 2 + 4);

        // Testo XP sopra la barra
        String xpText = xpNelLivello + "/" + xpPerLivello;
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Constants.TEXT_MUTED);
        g2d.drawString(xpText, leftPad + (barW - fm.stringWidth(xpText)) / 2, barY - 6);

        // +XP guadagnati (appare sotto la barra, centrato)
        if (xpProgressoAnimato >= xpProgressoTarget && xpGuadagnati > 0) {
            String plusXp = "+" + xpGuadagnati + " XP";
            g2d.setColor(Constants.BTN_GREEN);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            fm = g2d.getFontMetrics();
            g2d.drawString(plusXp, leftPad + (barW - fm.stringWidth(plusXp)) / 2, barY + barH + fm.getHeight());
        }
    }

    public void addNuovaPartitaListener(ActionListener l) { btnNuovaPartita.addActionListener(l); }
    public void addMenuListener(ActionListener l) { btnMenu.addActionListener(l); }

    // ==================== Classe interna Coriandolo ====================

    /** Singola particella di coriandolo animata. */
    private static class Coriandolo {
        float x, y;
        float velocitaY;
        float velocitaX;
        float rotazione;
        float velocitaRotazione;
        float dimensione;
        Color colore;
        int larghezzaSchermo;

        Coriandolo(int larghezzaSchermo) {
            this.larghezzaSchermo = larghezzaSchermo;
            reset(true);
        }

        void reset(boolean iniziale) {
            x = RANDOM.nextFloat() * larghezzaSchermo;
            y = iniziale ? -RANDOM.nextFloat() * 600 : -RANDOM.nextFloat() * 40 - 10;
            velocitaY = 1.5f + RANDOM.nextFloat() * 2.5f;
            velocitaX = (RANDOM.nextFloat() - 0.5f) * 2f;
            rotazione = RANDOM.nextFloat() * 360;
            velocitaRotazione = (RANDOM.nextFloat() - 0.5f) * 8f;
            dimensione = 4 + RANDOM.nextFloat() * 6;
            colore = COLORI_CORIANDOLI[RANDOM.nextInt(COLORI_CORIANDOLI.length)];
        }

        void aggiorna() {
            y += velocitaY;
            x += velocitaX;
            // Ondulazione laterale
            x += Math.sin(y * 0.02) * 0.5;
            rotazione += velocitaRotazione;

            if (y > 850) {
                reset(false);
            }
        }

        void disegna(Graphics2D g2d) {
            Graphics2D g = (Graphics2D) g2d.create();
            g.translate(x, y);
            g.rotate(Math.toRadians(rotazione));

            // Rettangolino con alpha
            g.setColor(new Color(colore.getRed(), colore.getGreen(), colore.getBlue(), 200));
            g.fillRoundRect((int)(-dimensione / 2), (int)(-dimensione / 4),
                    (int) dimensione, (int)(dimensione / 2), 2, 2);
            g.dispose();
        }
    }
}
