package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.StyledButton;

/**
 * Schermata del profilo utente: nickname, avatar, statistiche.
 */
public class ProfileScreen extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField txtNickname;
    private final JLabel lblPartiteGiocate;
    private final JLabel lblPartiteVinte;
    private final JLabel lblPartitePerse;
    private final JLabel lblLivello;
    private final JLabel lblWinRate;
    private final StyledButton btnSalva;
    private final StyledButton btnIndietro;

    public ProfileScreen() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 20, 8, 20);

        // Titolo
        JLabel titolo = new JLabel("Profilo Giocatore", SwingConstants.CENTER);
        titolo.setFont(new Font("Serif", Font.BOLD, 36));
        titolo.setForeground(Constants.GOLD);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(titolo, gbc);

        // Card profilo (pannello semi-trasparente)
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(218, 165, 32, 80));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 10, 6, 10);

        // Nickname
        gc.gridy = 0;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Nickname:"), gc);

        txtNickname = new JTextField(15);
        txtNickname.setFont(Constants.LABEL_FONT);
        txtNickname.setBackground(new Color(40, 40, 80));
        txtNickname.setForeground(Constants.TEXT_WHITE);
        txtNickname.setCaretColor(Constants.TEXT_WHITE);
        txtNickname.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 160), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(txtNickname, gc);

        // Statistiche
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;

        gc.gridy = 1;
        card.add(creaLabel("Partite Giocate:"), gc);
        lblPartiteGiocate = creaValoreLabel("0");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblPartiteGiocate, gc);

        gc.gridy = 2;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Vittorie:"), gc);
        lblPartiteVinte = creaValoreLabel("0");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblPartiteVinte, gc);

        gc.gridy = 3;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Sconfitte:"), gc);
        lblPartitePerse = creaValoreLabel("0");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblPartitePerse, gc);

        gc.gridy = 4;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Win Rate:"), gc);
        lblWinRate = creaValoreLabel("0%");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblWinRate, gc);

        gc.gridy = 5;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Livello:"), gc);
        lblLivello = creaValoreLabel("1");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblLivello, gc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        add(card, gbc);

        // Pulsanti
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBottoni.setOpaque(false);
        btnIndietro = new StyledButton("Indietro");
        btnIndietro.setPreferredSize(new Dimension(150, 45));
        btnSalva = StyledButton.verde("Salva");
        btnSalva.setPreferredSize(new Dimension(150, 45));
        panelBottoni.add(btnIndietro);
        panelBottoni.add(btnSalva);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(panelBottoni, gbc);
    }

    private JLabel creaLabel(String testo) {
        JLabel lbl = new JLabel(testo);
        lbl.setFont(Constants.LABEL_FONT);
        lbl.setForeground(new Color(180, 180, 210));
        return lbl;
    }

    private JLabel creaValoreLabel(String testo) {
        JLabel lbl = new JLabel(testo);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(Constants.GOLD);
        return lbl;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint bg = new GradientPaint(0, 0, new Color(10, 10, 40),
                0, getHeight(), new Color(30, 30, 70));
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public String getNickname() { return txtNickname.getText().trim(); }
    public void setNickname(String nick) { txtNickname.setText(nick); }

    public void setStatistiche(int giocate, int vinte, int perse, int livello) {
        lblPartiteGiocate.setText(String.valueOf(giocate));
        lblPartiteVinte.setText(String.valueOf(vinte));
        lblPartitePerse.setText(String.valueOf(perse));
        lblLivello.setText(String.valueOf(livello));
        double winRate = giocate > 0 ? (double) vinte / giocate * 100 : 0;
        lblWinRate.setText(String.format("%.1f%%", winRate));
    }

    public void addSalvaListener(ActionListener l) { btnSalva.addActionListener(l); }
    public void addIndietroListener(ActionListener l) { btnIndietro.addActionListener(l); }
}
