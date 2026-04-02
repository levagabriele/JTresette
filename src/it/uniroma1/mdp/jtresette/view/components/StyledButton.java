package it.uniroma1.mdp.jtresette.view.components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import it.uniroma1.mdp.jtresette.util.Constants;

/**
 * Bottone flat moderno con rendering custom.
 */
public class StyledButton extends JButton {

    private static final long serialVersionUID = 1L;

    private Color bgColor;
    private Color bgHoverColor;
    private Color bgPressedColor;
    private Color textColor;
    private boolean hovered = false;
    private boolean pressed = false;

    public StyledButton(String text) {
        this(text, Constants.BTN_DEFAULT);
    }

    public StyledButton(String text, Color bgColor) {
        super(text);
        this.bgColor = bgColor;
        this.bgHoverColor = brighten(bgColor, 25);
        this.bgPressedColor = bgColor.darker();
        this.textColor = Constants.TEXT_WHITE;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setFont(Constants.BUTTON_FONT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(250, 48));

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
        return new StyledButton(text, Constants.BTN_GREEN);
    }

    /** Crea un bottone rosso (per azioni distruttive). */
    public static StyledButton rosso(String text) {
        return new StyledButton(text, Constants.BTN_RED);
    }

    /** Crea un bottone piccolo per la barra superiore. */
    public static StyledButton piccolo(String text, Color bgColor) {
        StyledButton btn = new StyledButton(text, bgColor);
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

        // Sfondo flat
        Color bg = pressed ? bgPressedColor : (hovered ? bgHoverColor : bgColor);
        g2d.setColor(bg);
        g2d.fillRoundRect(0, 0, w, h, 14, 14);

        // Testo centrato
        g2d.setFont(getFont());
        FontMetrics fm = g2d.getFontMetrics();
        String text = getText();
        int textX = (w - fm.stringWidth(text)) / 2;
        int textY = (h + fm.getAscent() - fm.getDescent()) / 2;

        g2d.setColor(textColor);
        g2d.drawString(text, textX, textY);

        g2d.dispose();
    }

    private static Color brighten(Color c, int amount) {
        return new Color(
                Math.min(255, c.getRed() + amount),
                Math.min(255, c.getGreen() + amount),
                Math.min(255, c.getBlue() + amount));
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        this.bgHoverColor = brighten(bgColor, 25);
        this.bgPressedColor = bgColor.darker();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
}
