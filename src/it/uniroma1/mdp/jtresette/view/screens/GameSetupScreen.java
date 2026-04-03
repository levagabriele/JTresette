package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.StyledButton;

/**
 * Schermata di configurazione partita: scelta numero avversari e difficolta'.
 */
public class GameSetupScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ButtonGroup gruppoGiocatori;
    private final JRadioButton rb1vs1, rb1vs2, rb1vs3;
    private final ButtonGroup gruppoDifficolta;
    private final JRadioButton rbFacile, rbMedio, rbDifficile;
    private final StyledButton btnInizia;
    private final StyledButton btnIndietro;

    public GameSetupScreen() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Titolo
        JLabel titolo = new JLabel("Configura Partita", SwingConstants.CENTER);
        titolo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titolo.setForeground(Constants.TEXT_WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(titolo, gbc);

        // Sezione giocatori
        JLabel lblGiocatori = new JLabel("Numero di avversari:");
        lblGiocatori.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblGiocatori.setForeground(Constants.TEXT_WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(lblGiocatori, gbc);

        JPanel panelGiocatori = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
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
        gbc.insets = new Insets(0, 0, 25, 0);
        add(panelGiocatori, gbc);

        // Sezione difficolta'
        JLabel lblDifficolta = new JLabel("Difficolta' AI:");
        lblDifficolta.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDifficolta.setForeground(Constants.TEXT_WHITE);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(lblDifficolta, gbc);

        JPanel panelDifficolta = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
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
        gbc.insets = new Insets(0, 0, 35, 0);
        add(panelDifficolta, gbc);

        // Pulsanti
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBottoni.setOpaque(false);

        btnIndietro = new StyledButton("Indietro");
        btnIndietro.setPreferredSize(new Dimension(180, 45));
        btnInizia = StyledButton.verde("Inizia Partita");
        btnInizia.setPreferredSize(new Dimension(220, 50));

        panelBottoni.add(btnIndietro);
        panelBottoni.add(btnInizia);

        gbc.gridy = 5;
        add(panelBottoni, gbc);
    }

    private JRadioButton creaRadio(String testo) {
        JRadioButton rb = new JRadioButton(testo) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Cerchio radio
                int size = 16;
                int y = (getHeight() - size) / 2;
                g2d.setColor(new Color(60, 60, 65));
                g2d.fillOval(2, y, size, size);
                g2d.setColor(Constants.ACCENT);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawOval(2, y, size, size);

                if (isSelected()) {
                    g2d.setColor(Constants.TEXT_WHITE);
                    g2d.fillOval(6, y + 4, size - 8, size - 8);
                }

                // Testo
                g2d.setColor(Constants.TEXT_WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(getText(), size + 8, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rb.setForeground(Constants.TEXT_WHITE);
        rb.setOpaque(false);
        rb.setFocusPainted(false);
        rb.setPreferredSize(new Dimension(120, 30));
        return rb;
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

    public int getNumGiocatoriTotali() {
        if (rb1vs1.isSelected()) return 2;
        if (rb1vs2.isSelected()) return 3;
        return 4;
    }

    public int getLivelloDifficolta() {
        if (rbFacile.isSelected()) return 0;
        if (rbMedio.isSelected()) return 1;
        return 2;
    }

    public void addIniziaListener(ActionListener l) { btnInizia.addActionListener(l); }
    public void addIndietroListener(ActionListener l) { btnIndietro.addActionListener(l); }
}
