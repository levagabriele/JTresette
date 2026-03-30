package it.uniroma1.mdp.jtresette.view.components;

import java.awt.*;
import java.util.stream.IntStream;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Pannello laterale che mostra i punteggi dei giocatori.
 */
public class ScorePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private String[] nomiGiocatori;
    private int[] punteggi;
    private int obiettivoTerzi = Constants.OBIETTIVO_TERZI_DEFAULT;

    public ScorePanel() {
        setPreferredSize(new Dimension(180, 300));
        setOpaque(false);
    }

    public void setNomiGiocatori(String[] nomi) {
        this.nomiGiocatori = nomi;
        repaint();
    }

    public void setPunteggi(int[] punteggi) {
        this.punteggi = punteggi;
        repaint();
    }

    public void setObiettivoTerzi(int obiettivo) {
        this.obiettivoTerzi = obiettivo;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sfondo semi-trasparente
        g2d.setColor(Constants.PANEL_BG);
        g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
        g2d.setColor(Constants.GOLD);
        g2d.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);

        // Titolo
        g2d.setFont(Constants.SCORE_FONT);
        g2d.setColor(Constants.GOLD);
        g2d.drawString("PUNTEGGI", 30, 30);

        g2d.setColor(Constants.GOLD);
        g2d.drawLine(15, 38, getWidth() - 15, 38);

        // Obiettivo
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2d.setColor(Constants.TEXT_WHITE);
        g2d.drawString("Obiettivo: " + RegoleTresette.formattaPunti(obiettivoTerzi), 15, 55);

        if (nomiGiocatori == null || punteggi == null) return;

        // Ordina indici per punteggio decrescente (Stream)
        int[] ordinati = IntStream.range(0, nomiGiocatori.length)
                .boxed()
                .sorted((a, b) -> Integer.compare(punteggi[b], punteggi[a]))
                .mapToInt(Integer::intValue)
                .toArray();

        // Punteggi giocatori (ordinati per punteggio)
        int y = 80;
        for (int idx : ordinati) {
            g2d.setFont(Constants.SCORE_FONT);
            g2d.setColor(idx == 0 ? Constants.GOLD : Constants.TEXT_WHITE);
            g2d.drawString(nomiGiocatori[idx], 15, y);

            String puntiStr = RegoleTresette.formattaPunti(punteggi[idx]);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(puntiStr, getWidth() - 15 - fm.stringWidth(puntiStr), y);

            // Barra progresso
            y += 5;
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRoundRect(15, y, getWidth() - 30, 8, 4, 4);
            float progresso = Math.min(1f, (float) punteggi[idx] / obiettivoTerzi);
            g2d.setColor(idx == 0 ? Constants.GOLD : new Color(100, 150, 255));
            g2d.fillRoundRect(15, y, (int) ((getWidth() - 30) * progresso), 8, 4, 4);

            y += 25;
        }
    }
}
