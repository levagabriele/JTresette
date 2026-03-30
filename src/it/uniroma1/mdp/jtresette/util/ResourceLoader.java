package it.uniroma1.mdp.jtresette.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utilita' per il caricamento di risorse dal filesystem.
 */
public final class ResourceLoader {

    private ResourceLoader() {}

    /**
     * Carica un'immagine dal path specificato.
     *
     * @param path percorso relativo dell'immagine
     * @return l'immagine caricata, o null se non trovata
     */
    public static BufferedImage caricaImmagine(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento: " + path);
        }
        return null;
    }

    /**
     * Verifica se un file risorsa esiste.
     */
    public static boolean esiste(String path) {
        return new File(path).exists();
    }
}
