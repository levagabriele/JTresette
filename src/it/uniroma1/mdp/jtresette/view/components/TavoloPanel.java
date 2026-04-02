package it.uniroma1.mdp.jtresette.view.components;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.AvatarRenderer;
import it.uniroma1.mdp.jtresette.view.render.CardImageLoader;

/**
 * Pannello centrale che mostra le carte giocate nel turno corrente
 * e le posizioni dei giocatori AI.
 */
public class TavoloPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final List<Map.Entry<Integer, Carta>> carteGiocate = new ArrayList<>();
    private int numGiocatori = 4;
    private String[] nomiGiocatori;
    private int[] numCarteInMano;
    private String messaggioTemporaneo;
    private long messaggioScadenza;
    private BufferedImage[] avatarImages;
    private static final int AVATAR_SIZE = 28;

    public TavoloPanel() {
        setOpaque(false);
    }

    public void setNumGiocatori(int num) {
        this.numGiocatori = num;
    }

    public void setNomiGiocatori(String[] nomi) {
        this.nomiGiocatori = nomi;
    }

    public void setNumCarteInMano(int[] numCarte) {
        this.numCarteInMano = numCarte;
        repaint();
    }

    /** Aggiunge una carta giocata al tavolo. */
    public void aggiungiCartaGiocata(int indiceGiocatore, Carta carta) {
        carteGiocate.add(Map.entry(indiceGiocatore, carta));
        repaint();
    }

    /** Pulisce il tavolo (nuovo turno). */
    public void pulisci() {
        carteGiocate.clear();
        repaint();
    }

    /** Imposta gli avatar dei giocatori. */
    public void setAvatars(String[] avatarIds) {
        avatarImages = new BufferedImage[avatarIds.length];
        for (int i = 0; i < avatarIds.length; i++) {
            avatarImages[i] = AvatarRenderer.render(avatarIds[i], AVATAR_SIZE);
        }
        repaint();
    }

    /** Mostra un messaggio temporaneo (es. dichiarazioni). */
    public void mostraMessaggio(String msg, int durataMs) {
        this.messaggioTemporaneo = msg;
        this.messaggioScadenza = System.currentTimeMillis() + durataMs;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Disegna sfondo tavolo (ovale verde)
        g2d.setColor(Constants.TABLE_GREEN);
        g2d.fillOval(centerX - 250, centerY - 180, 500, 360);
        g2d.setColor(Constants.TABLE_GREEN_DARK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(centerX - 250, centerY - 180, 500, 360);

        // Disegna nomi giocatori AI e dorso carte
        disegnaGiocatoriAI(g2d, centerX, centerY);

        // Disegna carte giocate al centro
        disegnaCarteGiocate(g2d, centerX, centerY);

        // Messaggio temporaneo
        if (messaggioTemporaneo != null && System.currentTimeMillis() < messaggioScadenza) {
            g2d.setFont(Constants.SUBTITLE_FONT);
            g2d.setColor(Constants.GOLD);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(messaggioTemporaneo,
                    centerX - fm.stringWidth(messaggioTemporaneo) / 2, centerY - 140);
        }
    }

    private void disegnaGiocatoriAI(Graphics2D g2d, int centerX, int centerY) {
        if (nomiGiocatori == null) return;
        CardImageLoader loader = CardImageLoader.getInstance();
        BufferedImage dorso = loader.getDorso();

        // Posizioni giocatori: 0=umano(basso), 1=sinistra, 2=alto, 3=destra
        Point[] posNomi = getPosizioniNomi(centerX, centerY);
        Point[] posCarte = getPosizioniCarteAI(centerX, centerY);

        for (int i = 1; i < numGiocatori; i++) {
            // Avatar accanto al nome
            g2d.setFont(Constants.SCORE_FONT);
            g2d.setColor(Constants.TEXT_WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            int nomeW = fm.stringWidth(nomiGiocatori[i]);
            int totalW = nomeW + (avatarImages != null ? AVATAR_SIZE + 4 : 0);
            int startX = posNomi[i].x - totalW / 2;

            if (avatarImages != null && i < avatarImages.length && avatarImages[i] != null) {
                g2d.drawImage(avatarImages[i], startX, posNomi[i].y - AVATAR_SIZE + 4, null);
                startX += AVATAR_SIZE + 4;
            }
            g2d.drawString(nomiGiocatori[i], startX, posNomi[i].y);

            // Carte in mano (dorso piccolo)
            if (numCarteInMano != null && numCarteInMano[i] > 0) {
                int miniW = 30;
                int miniH = 48;
                int numCarte = numCarteInMano[i];
                for (int c = 0; c < numCarte; c++) {
                    int offsetX = c * 8;
                    g2d.drawImage(dorso, posCarte[i].x + offsetX, posCarte[i].y, miniW, miniH, null);
                }
            }
        }
    }

    private void disegnaCarteGiocate(Graphics2D g2d, int centerX, int centerY) {
        CardImageLoader loader = CardImageLoader.getInstance();

        // Posizioni dove appaiono le carte giocate (relative al centro)
        Point[] posizioni = getPosizioniCarteGiocate(centerX, centerY);

        for (Map.Entry<Integer, Carta> entry : carteGiocate) {
            int idx = entry.getKey();
            Carta carta = entry.getValue();
            BufferedImage img = loader.getImmagine(carta);
            if (img != null && idx < posizioni.length) {
                int cardW = (int)(Constants.CARD_WIDTH * 0.8);
                int cardH = (int)(Constants.CARD_HEIGHT * 0.8);
                g2d.drawImage(img, posizioni[idx].x - cardW / 2,
                        posizioni[idx].y - cardH / 2, cardW, cardH, null);
            }
        }
    }

    private Point[] getPosizioniNomi(int cx, int cy) {
        return new Point[]{
            new Point(cx, cy + 200),           // 0: umano (non usato qui)
            new Point(100, cy),                 // 1: sinistra
            new Point(cx, 30),                  // 2: alto
            new Point(getWidth() - 100, cy)     // 3: destra
        };
    }

    private Point[] getPosizioniCarteAI(int cx, int cy) {
        return new Point[]{
            new Point(cx - 50, cy + 160),       // 0: umano
            new Point(20, cy + 20),             // 1: sinistra
            new Point(cx - 50, 45),             // 2: alto
            new Point(getWidth() - 170, cy + 20) // 3: destra
        };
    }

    private Point[] getPosizioniCarteGiocate(int cx, int cy) {
        return new Point[]{
            new Point(cx, cy + 60),             // 0: umano (basso)
            new Point(cx - 100, cy),            // 1: sinistra
            new Point(cx, cy - 60),             // 2: alto
            new Point(cx + 100, cy)             // 3: destra
        };
    }
}
