package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Schermata di fine partita con risultati e punteggi.
 */
public class GameOverScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel lblRisultato;
    private final JLabel lblPunteggi;
    private final JButton btnNuovaPartita;
    private final JButton btnMenu;

    public GameOverScreen() {
        setLayout(new GridBagLayout());
        setBackground(Constants.MENU_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);

        lblRisultato = new JLabel("", SwingConstants.CENTER);
        lblRisultato.setFont(Constants.TITLE_FONT);
        lblRisultato.setForeground(Constants.GOLD);
        gbc.gridy = 0;
        add(lblRisultato, gbc);

        lblPunteggi = new JLabel("", SwingConstants.CENTER);
        lblPunteggi.setFont(Constants.SUBTITLE_FONT);
        lblPunteggi.setForeground(Constants.TEXT_WHITE);
        gbc.gridy = 1;
        add(lblPunteggi, gbc);

        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBottoni.setOpaque(false);

        btnNuovaPartita = creaBottone("Nuova Partita");
        btnNuovaPartita.setBackground(new Color(0, 100, 0));
        btnMenu = creaBottone("Menu Principale");

        panelBottoni.add(btnNuovaPartita);
        panelBottoni.add(btnMenu);

        gbc.gridy = 2;
        gbc.insets = new Insets(40, 0, 0, 0);
        add(panelBottoni, gbc);
    }

    /** Aggiorna la schermata con i risultati della partita. */
    public void mostraRisultati(String nomeVincitore, boolean haVintoUmano, int[] punteggi, String[] nomiGiocatori) {
        if (haVintoUmano) {
            lblRisultato.setText("HAI VINTO!");
            lblRisultato.setForeground(Constants.GOLD);
        } else {
            lblRisultato.setText("Hai Perso...");
            lblRisultato.setForeground(new Color(200, 50, 50));
        }

        StringBuilder sb = new StringBuilder("<html><center>");
        for (int i = 0; i < punteggi.length; i++) {
            sb.append(nomiGiocatori[i]).append(": ").append(RegoleTresette.formattaPunti(punteggi[i]));
            if (i < punteggi.length - 1) sb.append("<br>");
        }
        sb.append("</center></html>");
        lblPunteggi.setText(sb.toString());
    }

    private JButton creaBottone(String testo) {
        JButton btn = new JButton(testo);
        btn.setFont(Constants.BUTTON_FONT);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 60, 120));
        btn.setForeground(Constants.TEXT_WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.GOLD, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, new Color(15, 15, 50),
                0, getHeight(), new Color(35, 35, 80));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addNuovaPartitaListener(ActionListener l) { btnNuovaPartita.addActionListener(l); }
    public void addMenuListener(ActionListener l) { btnMenu.addActionListener(l); }
}
