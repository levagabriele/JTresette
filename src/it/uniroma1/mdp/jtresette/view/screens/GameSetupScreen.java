package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Schermata di configurazione partita: scelta numero avversari e difficolta'.
 */
public class GameSetupScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ButtonGroup gruppoGiocatori;
    private final JRadioButton rb1vs1, rb1vs2, rb1vs3;
    private final ButtonGroup gruppoDifficolta;
    private final JRadioButton rbFacile, rbMedio, rbDifficile;
    private final JButton btnInizia;
    private final JButton btnIndietro;

    public GameSetupScreen() {
        setLayout(new GridBagLayout());
        setBackground(Constants.MENU_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Titolo
        JLabel titolo = new JLabel("Configura Partita", SwingConstants.CENTER);
        titolo.setFont(Constants.SUBTITLE_FONT);
        titolo.setForeground(Constants.GOLD);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(titolo, gbc);

        // Sezione giocatori
        JLabel lblGiocatori = new JLabel("Numero di avversari:");
        lblGiocatori.setFont(Constants.BUTTON_FONT);
        lblGiocatori.setForeground(Constants.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 5, 0);
        add(lblGiocatori, gbc);

        JPanel panelGiocatori = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelGiocatori.setOpaque(false);
        gruppoGiocatori = new ButtonGroup();

        rb1vs1 = creaRadio("1 vs 1");
        rb1vs2 = creaRadio("1 vs 2");
        rb1vs3 = creaRadio("1 vs 3");
        rb1vs3.setSelected(true);

        gruppoGiocatori.add(rb1vs1);
        gruppoGiocatori.add(rb1vs2);
        gruppoGiocatori.add(rb1vs3);
        panelGiocatori.add(rb1vs1);
        panelGiocatori.add(rb1vs2);
        panelGiocatori.add(rb1vs3);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(panelGiocatori, gbc);

        // Sezione difficolta'
        JLabel lblDifficolta = new JLabel("Difficolta' AI:");
        lblDifficolta.setFont(Constants.BUTTON_FONT);
        lblDifficolta.setForeground(Constants.TEXT_WHITE);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 5, 0);
        add(lblDifficolta, gbc);

        JPanel panelDifficolta = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelDifficolta.setOpaque(false);
        gruppoDifficolta = new ButtonGroup();

        rbFacile = creaRadio("Facile");
        rbMedio = creaRadio("Medio");
        rbDifficile = creaRadio("Difficile");
        rbMedio.setSelected(true);

        gruppoDifficolta.add(rbFacile);
        gruppoDifficolta.add(rbMedio);
        gruppoDifficolta.add(rbDifficile);
        panelDifficolta.add(rbFacile);
        panelDifficolta.add(rbMedio);
        panelDifficolta.add(rbDifficile);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(panelDifficolta, gbc);

        // Pulsanti
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBottoni.setOpaque(false);

        btnIndietro = creaBottone("Indietro");
        btnInizia = creaBottone("Inizia Partita");
        btnInizia.setBackground(new Color(0, 100, 0));

        panelBottoni.add(btnIndietro);
        panelBottoni.add(btnInizia);

        gbc.gridy = 5;
        add(panelBottoni, gbc);
    }

    private JRadioButton creaRadio(String testo) {
        JRadioButton rb = new JRadioButton(testo);
        rb.setFont(Constants.LABEL_FONT);
        rb.setForeground(Constants.TEXT_WHITE);
        rb.setOpaque(false);
        rb.setFocusPainted(false);
        return rb;
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    /** Restituisce il numero totale di giocatori (incluso l'umano). */
    public int getNumGiocatoriTotali() {
        if (rb1vs1.isSelected()) return 2;
        if (rb1vs2.isSelected()) return 3;
        return 4;
    }

    /** Restituisce il livello di difficolta' selezionato (0=facile, 1=medio, 2=difficile). */
    public int getLivelloDifficolta() {
        if (rbFacile.isSelected()) return 0;
        if (rbMedio.isSelected()) return 1;
        return 2;
    }

    public void addIniziaListener(ActionListener l) {
        btnInizia.addActionListener(l);
    }

    public void addIndietroListener(ActionListener l) {
        btnIndietro.addActionListener(l);
    }
}
