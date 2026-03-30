package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Schermata del menu principale con pulsanti per navigazione.
 */
public class MenuScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JButton btnNuovaPartita;
    private final JButton btnProfilo;
    private final JButton btnEsci;

    public MenuScreen() {
        setLayout(new GridBagLayout());
        setBackground(Constants.MENU_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel titolo = new JLabel("JTresette", SwingConstants.CENTER);
        titolo.setFont(Constants.TITLE_FONT);
        titolo.setForeground(Constants.GOLD);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        add(titolo, gbc);

        // Sottotitolo
        JLabel sottotitolo = new JLabel("Il classico gioco di carte italiano", SwingConstants.CENTER);
        sottotitolo.setFont(Constants.LABEL_FONT);
        sottotitolo.setForeground(Constants.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(sottotitolo, gbc);

        // Pulsanti
        gbc.insets = new Insets(8, 0, 8, 0);

        btnNuovaPartita = creaBottone("Nuova Partita");
        gbc.gridy = 2;
        add(btnNuovaPartita, gbc);

        btnProfilo = creaBottone("Profilo");
        gbc.gridy = 3;
        add(btnProfilo, gbc);

        btnEsci = creaBottone("Esci");
        gbc.gridy = 4;
        add(btnEsci, gbc);
    }

    private JButton creaBottone(String testo) {
        JButton btn = new JButton(testo);
        btn.setFont(Constants.BUTTON_FONT);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 60, 120));
        btn.setForeground(Constants.TEXT_WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.GOLD, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(80, 80, 160));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(60, 60, 120));
            }
        });
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Gradiente sfondo
        GradientPaint gradient = new GradientPaint(0, 0, new Color(15, 15, 50),
                0, getHeight(), new Color(35, 35, 80));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addNuovaPartitaListener(ActionListener l) {
        btnNuovaPartita.addActionListener(l);
    }

    public void addProfiloListener(ActionListener l) {
        btnProfilo.addActionListener(l);
    }

    public void addEsciListener(ActionListener l) {
        btnEsci.addActionListener(l);
    }
}
