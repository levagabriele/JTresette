package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;

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
    private final JButton btnSalva;
    private final JButton btnIndietro;

    public ProfileScreen() {
        setLayout(new GridBagLayout());
        setBackground(Constants.MENU_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 10, 20);

        // Titolo
        JLabel titolo = new JLabel("Profilo Giocatore", SwingConstants.CENTER);
        titolo.setFont(Constants.SUBTITLE_FONT);
        titolo.setForeground(Constants.GOLD);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(titolo, gbc);

        // Nickname
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblNick = creaLabel("Nickname:");
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(lblNick, gbc);

        txtNickname = new JTextField(15);
        txtNickname.setFont(Constants.LABEL_FONT);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtNickname, gbc);

        // Statistiche
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridy = 2;
        add(creaLabel("Partite Giocate:"), gbc);
        lblPartiteGiocate = creaValoreLabel("0");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblPartiteGiocate, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(creaLabel("Vittorie:"), gbc);
        lblPartiteVinte = creaValoreLabel("0");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblPartiteVinte, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(creaLabel("Sconfitte:"), gbc);
        lblPartitePerse = creaValoreLabel("0");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblPartitePerse, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(creaLabel("Livello:"), gbc);
        lblLivello = creaValoreLabel("1");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblLivello, gbc);

        // Pulsanti
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBottoni.setOpaque(false);
        btnIndietro = creaBottone("Indietro");
        btnSalva = creaBottone("Salva");
        btnSalva.setBackground(new Color(0, 100, 0));
        panelBottoni.add(btnIndietro);
        panelBottoni.add(btnSalva);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 0, 0);
        add(panelBottoni, gbc);
    }

    private JLabel creaLabel(String testo) {
        JLabel lbl = new JLabel(testo);
        lbl.setFont(Constants.LABEL_FONT);
        lbl.setForeground(Constants.TEXT_WHITE);
        return lbl;
    }

    private JLabel creaValoreLabel(String testo) {
        JLabel lbl = new JLabel(testo);
        lbl.setFont(Constants.LABEL_FONT);
        lbl.setForeground(Constants.GOLD);
        return lbl;
    }

    private JButton creaBottone(String testo) {
        JButton btn = new JButton(testo);
        btn.setFont(Constants.BUTTON_FONT);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(60, 60, 120));
        btn.setForeground(Constants.TEXT_WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.GOLD, 2),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, new Color(15, 15, 50),
                0, getHeight(), new Color(35, 35, 80));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    // Getters e setters per i dati del profilo
    public String getNickname() { return txtNickname.getText().trim(); }
    public void setNickname(String nick) { txtNickname.setText(nick); }

    public void setStatistiche(int giocate, int vinte, int perse, int livello) {
        lblPartiteGiocate.setText(String.valueOf(giocate));
        lblPartiteVinte.setText(String.valueOf(vinte));
        lblPartitePerse.setText(String.valueOf(perse));
        lblLivello.setText(String.valueOf(livello));
    }

    public void addSalvaListener(ActionListener l) { btnSalva.addActionListener(l); }
    public void addIndietroListener(ActionListener l) { btnIndietro.addActionListener(l); }
}
