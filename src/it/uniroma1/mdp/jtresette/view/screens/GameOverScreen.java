package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.StyledButton;

/**
 * Schermata di fine partita con risultati, punteggi e barra XP.
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

    public GameOverScreen() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);

        lblRisultato = new JLabel("", SwingConstants.CENTER);
        lblRisultato.setFont(new Font("SansSerif", Font.BOLD, 48));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        add(lblRisultato, gbc);

        lblSottotitolo = new JLabel("", SwingConstants.CENTER);
        lblSottotitolo.setFont(new Font("SansSerif", Font.PLAIN, 18));
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
        xpPanel.setPreferredSize(new Dimension(380, 60));
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
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setForeground(Constants.TEXT_WHITE);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        punteggiPanel.add(header);
        punteggiPanel.add(Box.createVerticalStrut(10));

        for (int i = 0; i < punteggi.length; i++) {
            JLabel lbl = new JLabel("  " + nomiGiocatori[i] + ":  " + RegoleTresette.formattaPunti(punteggi[i]) + " punti");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setForeground(i == 0 ? Constants.TEXT_WHITE : Constants.TEXT_MUTED);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            punteggiPanel.add(lbl);
            punteggiPanel.add(Box.createVerticalStrut(4));
        }
        punteggiPanel.revalidate();
        punteggiPanel.repaint();
        xpPanel.repaint();
    }

    /** Disegna la barra XP con livelli. */
    private void disegnaXpBar(Graphics2D g2d, int w, int h) {
        int barH = 16;
        int barY = h / 2 - barH / 2 + 8;
        int starSize = 20;
        int leftPad = 30;
        int rightPad = 30;
        int barW = w - leftPad - rightPad;

        // Stella livello corrente (sinistra)
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2d.setColor(Constants.GOLD);
        g2d.drawString("\u2605", 6, barY + barH / 2 + 4);
        g2d.setColor(Constants.TEXT_WHITE);
        g2d.drawString(String.valueOf(livello), 20, barY + barH / 2 + 4);

        // Sfondo barra
        g2d.setColor(new Color(50, 50, 55));
        g2d.fillRoundRect(leftPad, barY, barW, barH, 8, 8);

        // Progresso barra
        float progresso = Math.min(1f, (float) xpNelLivello / xpPerLivello);
        g2d.setColor(Constants.BTN_GREEN);
        g2d.fillRoundRect(leftPad, barY, (int) (barW * progresso), barH, 8, 8);

        // Stella livello successivo (destra)
        g2d.setColor(Constants.TEXT_MUTED);
        g2d.drawString("\u2605", w - rightPad + 6, barY + barH / 2 + 4);
        g2d.drawString(String.valueOf(livello + 1), w - rightPad + 20, barY + barH / 2 + 4);

        // Testo XP sopra la barra
        String xpText = xpNelLivello + "/" + xpPerLivello;
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Constants.TEXT_MUTED);
        g2d.drawString(xpText, leftPad + (barW - fm.stringWidth(xpText)) / 2, barY - 6);

        // +XP guadagnati
        String plusXp = "+" + xpGuadagnati;
        g2d.setColor(Constants.BTN_GREEN);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        fm = g2d.getFontMetrics();
        g2d.drawString(plusXp, leftPad + (int)(barW * progresso) - fm.stringWidth(plusXp) / 2, barY - 6);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint bg = new GradientPaint(0, 0, Constants.BG_DARK,
                0, getHeight(), Constants.BG_LIGHTER);
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addNuovaPartitaListener(ActionListener l) { btnNuovaPartita.addActionListener(l); }
    public void addMenuListener(ActionListener l) { btnMenu.addActionListener(l); }
}
