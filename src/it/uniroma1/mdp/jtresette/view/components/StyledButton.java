package it.uniroma1.mdp.jtresette.view.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Bottone personalizzato con rendering custom che funziona
 * su tutti i Look&Feel (incluso Windows nativo).
 * Disegna sfondo, bordo e testo manualmente.
 */
public class StyledButton extends JButton {

    private static final long serialVersionUID = 1L;

    private Color bgColor;
    private Color bgHoverColor;
    private Color bgPressedColor;
    private Color borderColor;
    private Color textColor;
    private boolean hovered = false;
    private boolean pressed = false;

    public StyledButton(String text) {
        this(text, new Color(50, 50, 110), Constants.GOLD);
    }

    public StyledButton(String text, Color bgColor, Color borderColor) {
        super(text);
        this.bgColor = bgColor;
        this.bgHoverColor = bgColor.brighter();
        this.bgPressedColor = bgColor.darker();
        this.borderColor = borderColor;
        this.textColor = Constants.TEXT_WHITE;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setFont(Constants.BUTTON_FONT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(250, 50));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    /** Crea un bottone verde (per azioni principali). */
    public static StyledButton verde(String text) {
        return new StyledButton(text, new Color(20, 100, 30), new Color(80, 200, 80));
    }

    /** Crea un bottone rosso (per azioni distruttive). */
    public static StyledButton rosso(String text) {
        return new StyledButton(text, new Color(120, 30, 30), new Color(200, 80, 80));
    }

    /** Crea un bottone piccolo per la barra superiore. */
    public static StyledButton piccolo(String text, Color bgColor) {
        StyledButton btn = new StyledButton(text, bgColor, Constants.GOLD);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(100, 30));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Sfondo con stato
        Color bg = pressed ? bgPressedColor : (hovered ? bgHoverColor : bgColor);
        g2d.setColor(bg);
        g2d.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);

        // Effetto luce in alto (gradiente sottile)
        if (!pressed) {
            GradientPaint shine = new GradientPaint(0, 0, new Color(255, 255, 255, 40),
                    0, h / 2, new Color(255, 255, 255, 0));
            g2d.setPaint(shine);
            g2d.fillRoundRect(0, 0, w - 1, h / 2, 12, 12);
        }

        // Bordo
        g2d.setColor(hovered ? borderColor.brighter() : borderColor);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(1, 1, w - 3, h - 3, 12, 12);

        // Testo centrato con ombra
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        String text = getText();
        int textX = (w - fm.stringWidth(text)) / 2;
        int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

        // Ombra testo
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(text, textX + 1, textY + 1);

        // Testo
        g2d.setColor(textColor);
        g2d.drawString(text, textX, textY);

        g2d.dispose();
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        this.bgHoverColor = bgColor.brighter();
        this.bgPressedColor = bgColor.darker();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
}
