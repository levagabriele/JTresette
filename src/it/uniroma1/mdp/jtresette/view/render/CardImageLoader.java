package it.uniroma1.mdp.jtresette.view.render;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.Seme;
import it.uniroma1.mdp.jtresette.model.card.Valore;
import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Carica e cache le immagini delle carte (Flyweight pattern).
 * Se le immagini non esistono, genera placeholder colorati.
 */
public class CardImageLoader {

    private static CardImageLoader instance;
    private final Map<Carta, BufferedImage> cache = new HashMap<>();
    private BufferedImage dorso;

    private CardImageLoader() {
        caricaTutte();
    }

    public static synchronized CardImageLoader getInstance() {
        if (instance == null) {
            instance = new CardImageLoader();
        }
        return instance;
    }

    private void caricaTutte() {
        // Carica tutte le 40 carte usando Stream
        Arrays.stream(Seme.values()).forEach(seme ->
            Arrays.stream(Valore.values()).forEach(valore -> {
                Carta carta = new Carta(seme, valore);
                BufferedImage img = caricaImmagine(carta.getImagePath());
                if (img == null) {
                    img = creaPlaceholder(carta);
                }
                cache.put(carta, ridimensiona(img, Constants.CARD_WIDTH, Constants.CARD_HEIGHT));
            })
        );

        // Carica dorso
        dorso = caricaImmagine(Constants.CARD_BACK_PATH);
        if (dorso == null) {
            dorso = creaPlaceholderDorso();
        }
        dorso = ridimensiona(dorso, Constants.CARD_WIDTH, Constants.CARD_HEIGHT);
    }

    private BufferedImage caricaImmagine(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento immagine: " + path);
        }
        return null;
    }

    /** Crea un placeholder colorato per una carta mancante. */
    private BufferedImage creaPlaceholder(Carta carta) {
        BufferedImage img = new BufferedImage(150, 250, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sfondo bianco con bordo arrotondato
        g.setColor(java.awt.Color.WHITE);
        g.fillRoundRect(0, 0, 150, 250, 15, 15);
        g.setColor(java.awt.Color.DARK_GRAY);
        g.drawRoundRect(0, 0, 149, 249, 15, 15);

        // Colore per seme
        java.awt.Color colore = switch (carta.getSeme()) {
            case COPPE -> new java.awt.Color(0, 120, 200);
            case DENARI -> new java.awt.Color(218, 165, 32);
            case BASTONI -> new java.awt.Color(0, 128, 0);
            case SPADE -> new java.awt.Color(100, 100, 100);
        };
        g.setColor(colore);

        // Valore grande al centro
        g.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 36));
        String val = carta.getValore().getNumero() <= 7
                ? String.valueOf(carta.getValore().getNumero())
                : carta.getValore().getNome().substring(0, 1);
        java.awt.FontMetrics fm = g.getFontMetrics();
        g.drawString(val, (150 - fm.stringWidth(val)) / 2, 90);

        // Nome seme sotto
        g.setFont(new java.awt.Font("Serif", java.awt.Font.PLAIN, 16));
        fm = g.getFontMetrics();
        String seme = carta.getSeme().getNome();
        g.drawString(seme, (150 - fm.stringWidth(seme)) / 2, 170);

        // Valore piccolo in alto a sinistra
        g.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 18));
        g.drawString(val, 10, 25);

        g.dispose();
        return img;
    }

    /** Crea un placeholder per il dorso della carta. */
    private BufferedImage creaPlaceholderDorso() {
        BufferedImage img = new BufferedImage(150, 250, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sfondo blu scuro
        g.setColor(new java.awt.Color(25, 25, 100));
        g.fillRoundRect(0, 0, 150, 250, 15, 15);

        // Bordo dorato
        g.setColor(Constants.GOLD);
        g.drawRoundRect(3, 3, 143, 243, 12, 12);
        g.drawRoundRect(8, 8, 133, 233, 10, 10);

        // Pattern decorativo al centro
        g.setColor(Constants.GOLD);
        g.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 28));
        java.awt.FontMetrics fm = g.getFontMetrics();
        String text = "JT";
        g.drawString(text, (150 - fm.stringWidth(text)) / 2, 135);

        g.dispose();
        return img;
    }

    private BufferedImage ridimensiona(BufferedImage originale, int larghezza, int altezza) {
        BufferedImage ridimensionata = new BufferedImage(larghezza, altezza, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ridimensionata.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originale, 0, 0, larghezza, altezza, null);
        g.dispose();
        return ridimensionata;
    }

    /** Ottiene l'immagine di una carta. */
    public BufferedImage getImmagine(Carta carta) {
        return cache.get(carta);
    }

    /** Ottiene l'immagine del dorso della carta. */
    public BufferedImage getDorso() {
        return dorso;
    }
}
