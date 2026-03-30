package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.StyledButton;

/**
 * Schermata del menu principale con pulsanti per navigazione.
 */
public class MenuScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final StyledButton btnNuovaPartita;
    private final StyledButton btnProfilo;
    private final StyledButton btnEsci;

    public MenuScreen() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Titolo
        JLabel titolo = new JLabel("JTresette", SwingConstants.CENTER);
        titolo.setFont(new Font("Serif", Font.BOLD, 64));
        titolo.setForeground(Constants.GOLD);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        add(titolo, gbc);

        // Sottotitolo
        JLabel sottotitolo = new JLabel("Il classico gioco di carte italiano", SwingConstants.CENTER);
        sottotitolo.setFont(new Font("Serif", Font.ITALIC, 18));
        sottotitolo.setForeground(new Color(180, 180, 200));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(sottotitolo, gbc);

        // Linea decorativa
        JPanel linea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int y = getHeight() / 2;
                GradientPaint gp = new GradientPaint(0, y, new Color(218, 165, 32, 0),
                        getWidth() / 2, y, Constants.GOLD);
                g2d.setPaint(gp);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, y, getWidth() / 2, y);
                gp = new GradientPaint(getWidth() / 2, y, Constants.GOLD,
                        getWidth(), y, new Color(218, 165, 32, 0));
                g2d.setPaint(gp);
                g2d.drawLine(getWidth() / 2, y, getWidth(), y);
            }
        };
        linea.setOpaque(false);
        linea.setPreferredSize(new Dimension(350, 20));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(linea, gbc);

        // Pulsanti
        gbc.insets = new Insets(8, 0, 8, 0);

        btnNuovaPartita = StyledButton.verde("Nuova Partita");
        gbc.gridy = 3;
        add(btnNuovaPartita, gbc);

        btnProfilo = new StyledButton("Profilo");
        gbc.gridy = 4;
        add(btnProfilo, gbc);

        btnEsci = StyledButton.rosso("Esci");
        gbc.gridy = 5;
        add(btnEsci, gbc);

        // Versione
        JLabel versione = new JLabel("v1.0 - Metodologie di Programmazione 2024/25", SwingConstants.CENTER);
        versione.setFont(new Font("SansSerif", Font.PLAIN, 11));
        versione.setForeground(new Color(100, 100, 130));
        gbc.gridy = 6;
        gbc.insets = new Insets(50, 0, 0, 0);
        add(versione, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Gradiente sfondo scuro
        GradientPaint bg = new GradientPaint(0, 0, new Color(10, 10, 40),
                0, h, new Color(30, 30, 70));
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, w, h);

        // Cerchi decorativi sfumati (effetto bokeh)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.04f));
        g2d.setColor(Constants.GOLD);
        g2d.fillOval(w / 2 - 300, h / 2 - 300, 600, 600);
        g2d.fillOval(-100, h - 200, 400, 400);
        g2d.fillOval(w - 200, -100, 300, 300);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void addNuovaPartitaListener(ActionListener l) { btnNuovaPartita.addActionListener(l); }
    public void addProfiloListener(ActionListener l) { btnProfilo.addActionListener(l); }
    public void addEsciListener(ActionListener l) { btnEsci.addActionListener(l); }
}
