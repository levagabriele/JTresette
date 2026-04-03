package it.uniroma1.mdp.jtresette.view.components;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Renderer che genera avatar per il profilo giocatore.
 * Ogni avatar e' un cerchio colorato con un simbolo dei semi italiani.
 */
public class AvatarRenderer {

    /** Definizione di un avatar: colore di sfondo e simbolo. */
    private static final List<AvatarDef> AVATARS = List.of(
            new AvatarDef(new Color(180, 40, 40),   "\u2660"),  // Spade
            new AvatarDef(new Color(40, 100, 180),   "\u2663"),  // Bastoni
            new AvatarDef(new Color(200, 160, 30),   "\u2666"),  // Denari
            new AvatarDef(new Color(160, 40, 160),   "\u2665"),  // Coppe
            new AvatarDef(new Color(40, 150, 80),    "\u265A"),  // Re
            new AvatarDef(new Color(200, 100, 40),   "\u2655"),  // Regina
            new AvatarDef(new Color(60, 60, 140),    "\u2658"),  // Cavallo
            new AvatarDef(new Color(120, 60, 40),    "\u2659")   // Fante
    );

    private AvatarRenderer() {}

    /** Restituisce il numero di avatar disponibili. */
    public static int getCount() {
        return AVATARS.size();
    }

    /** Genera l'ID avatar (es. "avatar_01") dato l'indice 0-based. */
    public static String getAvatarId(int index) {
        return String.format("avatar_%02d", index + 1);
    }

    /** Restituisce l'indice 0-based dato l'ID avatar. Ritorna 0 se non valido. */
    public static int getIndexFromId(String avatarId) {
        if (avatarId == null || !avatarId.startsWith("avatar_")) return 0;
        try {
            return Integer.parseInt(avatarId.substring(7)) - 1;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Disegna un avatar come immagine della dimensione specificata.
     * @param index indice dell'avatar (0-based)
     * @param size dimensione in pixel (larghezza e altezza)
     * @return BufferedImage con l'avatar renderizzato
     */
    public static BufferedImage render(int index, int size) {
        if (index < 0 || index >= AVATARS.size()) index = 0;
        AvatarDef def = AVATARS.get(index);

        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Cerchio di sfondo con gradiente
        Ellipse2D circle = new Ellipse2D.Float(2, 2, size - 4, size - 4);
        GradientPaint gp = new GradientPaint(0, 0, def.color.brighter(),
                size, size, def.color.darker());
        g2.setPaint(gp);
        g2.fill(circle);

        // Bordo
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(2f));
        g2.draw(circle);

        // Simbolo centrato
        g2.setColor(new Color(255, 255, 255, 220));
        int fontSize = (int) (size * 0.45);
        g2.setFont(new Font("Serif", Font.BOLD, fontSize));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (size - fm.stringWidth(def.symbol)) / 2 + 1;
        int textY = (size + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(def.symbol, textX, textY);

        g2.dispose();
        return img;
    }

    /** Disegna un avatar dato il suo ID (es. "avatar_01"). */
    public static BufferedImage render(String avatarId, int size) {
        return render(getIndexFromId(avatarId), size);
    }

    private record AvatarDef(Color color, String symbol) {}
}