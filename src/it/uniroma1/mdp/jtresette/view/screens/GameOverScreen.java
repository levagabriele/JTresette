package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.StyledButton;

/**
 * Schermata di fine partita con risultati e punteggi.
 */
public class GameOverScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel lblRisultato;
    private final JLabel lblSottotitolo;
    private final JPanel punteggiPanel;
    private final StyledButton btnNuovaPartita;
    private final StyledButton btnMenu;

    public GameOverScreen() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        lblRisultato = new JLabel("", SwingConstants.CENTER);
        lblRisultato.setFont(new Font("Serif", Font.BOLD, 56));
        lblRisultato.setForeground(Constants.GOLD);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        add(lblRisultato, gbc);

        lblSottotitolo = new JLabel("", SwingConstants.CENTER);
        lblSottotitolo.setFont(new Font("Serif", Font.ITALIC, 20));
        lblSottotitolo.setForeground(new Color(180, 180, 210));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        add(lblSottotitolo, gbc);

        // Pannello punteggi con sfondo
        punteggiPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.setColor(new Color(218, 165, 32, 60));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };
        punteggiPanel.setOpaque(false);
        punteggiPanel.setLayout(new BoxLayout(punteggiPanel, BoxLayout.Y_AXIS));
        punteggiPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        punteggiPanel.setPreferredSize(new Dimension(350, 150));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(punteggiPanel, gbc);

        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBottoni.setOpaque(false);

        btnNuovaPartita = StyledButton.verde("Nuova Partita");
        btnMenu = new StyledButton("Menu Principale");

        panelBottoni.add(btnNuovaPartita);
        panelBottoni.add(btnMenu);

        gbc.gridy = 3;
        add(panelBottoni, gbc);
    }

    public void mostraRisultati(String nomeVincitore, boolean haVintoUmano, int[] punteggi, String[] nomiGiocatori) {
        if (haVintoUmano) {
            lblRisultato.setText("VITTORIA!");
            lblRisultato.setForeground(Constants.GOLD);
            lblSottotitolo.setText("Complimenti, hai vinto la partita!");
        } else {
            lblRisultato.setText("SCONFITTA");
            lblRisultato.setForeground(new Color(200, 60, 60));
            lblSottotitolo.setText("Ha vinto " + nomeVincitore);
        }

        punteggiPanel.removeAll();
        JLabel header = new JLabel("  PUNTEGGI FINALI");
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setForeground(Constants.GOLD);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        punteggiPanel.add(header);
        punteggiPanel.add(Box.createVerticalStrut(10));

        for (int i = 0; i < punteggi.length; i++) {
            JLabel lbl = new JLabel("  " + nomiGiocatori[i] + ":  " + RegoleTresette.formattaPunti(punteggi[i]) + " punti");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setForeground(i == 0 ? Constants.GOLD : Constants.TEXT_WHITE);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            punteggiPanel.add(lbl);
            punteggiPanel.add(Box.createVerticalStrut(4));
        }
        punteggiPanel.revalidate();
        punteggiPanel.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint bg = new GradientPaint(0, 0, new Color(10, 10, 40),
                0, getHeight(), new Color(30, 30, 70));
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addNuovaPartitaListener(ActionListener l) { btnNuovaPartita.addActionListener(l); }
    public void addMenuListener(ActionListener l) { btnMenu.addActionListener(l); }
}
