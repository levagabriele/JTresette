package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.AvatarRenderer;
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

    // Avatar
    private final JLabel lblAvatar;
    private final JPanel avatarSelectionPanel;
    private String selectedAvatarId = "avatar_01";

    // Feedback salvataggio
    private final JLabel lblFeedback;
    private Timer feedbackTimer;

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
        gbc.insets = new Insets(0, 0, 20, 0);
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

        // Avatar corrente (grande, centrato sopra il nickname)
        lblAvatar = new JLabel();
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        aggiornaIconaAvatar();
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0, 10, 12, 10);
        card.add(lblAvatar, gc);

        // Selettore avatar (griglia di mini-avatar cliccabili)
        avatarSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        avatarSelectionPanel.setOpaque(false);
        creaSelettoreAvatar();
        gc.gridy = 1;
        gc.insets = new Insets(0, 10, 12, 10);
        card.add(avatarSelectionPanel, gc);

        // Reset gridwidth per le righe successive
        gc.gridwidth = 1;
        gc.insets = new Insets(6, 10, 6, 10);

        // Nickname
        gc.gridy = 2;
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

        gc.gridy = 3;
        card.add(creaLabel("Partite Giocate:"), gc);
        lblPartiteGiocate = creaValoreLabel("0");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblPartiteGiocate, gc);

        gc.gridy = 4;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Vittorie:"), gc);
        lblPartiteVinte = creaValoreLabel("0");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblPartiteVinte, gc);

        gc.gridy = 5;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Sconfitte:"), gc);
        lblPartitePerse = creaValoreLabel("0");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblPartitePerse, gc);

        gc.gridy = 6;
        gc.gridx = 0;
        gc.anchor = GridBagConstraints.EAST;
        card.add(creaLabel("Win Rate:"), gc);
        lblWinRate = creaValoreLabel("0%");
        gc.gridx = 1;
        gc.anchor = GridBagConstraints.WEST;
        card.add(lblWinRate, gc);

        gc.gridy = 7;
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

        // Label di feedback (inizialmente nascosta)
        lblFeedback = new JLabel("", SwingConstants.CENTER);
        lblFeedback.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblFeedback.setVisible(false);
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(lblFeedback, gbc);
    }

    /** Crea la griglia di mini-avatar selezionabili. */
    private void creaSelettoreAvatar() {
        int count = AvatarRenderer.getCount();
        for (int i = 0; i < count; i++) {
            final int index = i;
            BufferedImage miniAvatar = AvatarRenderer.render(i, 40);
            JLabel lbl = new JLabel(new ImageIcon(miniAvatar));
            lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lbl.setToolTipText("Avatar " + (i + 1));
            lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            lbl.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selezionaAvatar(index);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    lbl.setBorder(BorderFactory.createLineBorder(Constants.GOLD, 2));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    String id = AvatarRenderer.getAvatarId(index);
                    if (id.equals(selectedAvatarId)) {
                        lbl.setBorder(BorderFactory.createLineBorder(Constants.GOLD, 2));
                    } else {
                        lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                    }
                }
            });
            avatarSelectionPanel.add(lbl);
        }
    }

    /** Gestisce la selezione di un avatar dalla griglia. */
    private void selezionaAvatar(int index) {
        selectedAvatarId = AvatarRenderer.getAvatarId(index);
        aggiornaIconaAvatar();
        aggiornaBordiSelezione();
    }

    /** Aggiorna l'icona grande dell'avatar corrente. */
    private void aggiornaIconaAvatar() {
        int avatarIndex = AvatarRenderer.getIndexFromId(selectedAvatarId);
        BufferedImage img = AvatarRenderer.render(avatarIndex, 80);
        lblAvatar.setIcon(new ImageIcon(img));
    }

    /** Evidenzia il bordo dell'avatar selezionato nella griglia. */
    private void aggiornaBordiSelezione() {
        int selectedIndex = AvatarRenderer.getIndexFromId(selectedAvatarId);
        Component[] children = avatarSelectionPanel.getComponents();
        for (int i = 0; i < children.length; i++) {
            JLabel lbl = (JLabel) children[i];
            if (i == selectedIndex) {
                lbl.setBorder(BorderFactory.createLineBorder(Constants.GOLD, 2));
            } else {
                lbl.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            }
        }
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

    /** Mostra un messaggio di conferma temporaneo sotto i pulsanti. */
    public void mostraConferma(String messaggio) {
        lblFeedback.setText(messaggio);
        lblFeedback.setForeground(new Color(80, 200, 80));
        lblFeedback.setVisible(true);

        if (feedbackTimer != null && feedbackTimer.isRunning()) {
            feedbackTimer.stop();
        }
        feedbackTimer = new Timer(2500, e -> lblFeedback.setVisible(false));
        feedbackTimer.setRepeats(false);
        feedbackTimer.start();
    }

    // --- Getters / Setters pubblici ---

    public String getNickname() { return txtNickname.getText().trim(); }
    public void setNickname(String nick) { txtNickname.setText(nick); }

    public String getSelectedAvatarId() { return selectedAvatarId; }

    public void setAvatar(String avatarId) {
        this.selectedAvatarId = avatarId;
        aggiornaIconaAvatar();
        aggiornaBordiSelezione();
    }

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