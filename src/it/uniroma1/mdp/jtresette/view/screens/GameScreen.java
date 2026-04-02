package it.uniroma1.mdp.jtresette.view.screens;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.model.GameObserver;
import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.model.event.*;
import it.uniroma1.mdp.jtresette.model.game.GamePhase;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.components.HandPanel;
import it.uniroma1.mdp.jtresette.view.components.ScorePanel;
import it.uniroma1.mdp.jtresette.view.components.StyledButton;
import it.uniroma1.mdp.jtresette.view.components.TavoloPanel;

/**
 * Schermata di gioco principale. Implementa GameObserver per ricevere
 * notifiche dal Model e aggiornare la GUI di conseguenza.
 * Questo e' il cuore del pattern MVC+Observer lato View.
 */
public class GameScreen extends JPanel implements GameObserver {

    private static final long serialVersionUID = 1L;

    private final TavoloPanel tavoloPanel;
    private final HandPanel handPanel;
    private final ScorePanel scorePanel;
    private final JLabel lblStato;
    private final JLabel lblManoNumero;
    private final StyledButton btnAbbandona;
    private final StyledButton btnRicomincia;

    private String[] nomiGiocatori;
    private Runnable onGameOver;
    private Runnable onAbbandona;
    private Runnable onRicomincia;

    public GameScreen() {
        setLayout(new BorderLayout());
        setBackground(Constants.TABLE_GREEN_DARK);

        // Pannello superiore con info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));

        lblManoNumero = new JLabel("Mano: 1");
        lblManoNumero.setFont(Constants.SCORE_FONT);
        lblManoNumero.setForeground(Constants.TEXT_WHITE);
        topPanel.add(lblManoNumero, BorderLayout.WEST);

        lblStato = new JLabel("In attesa...", SwingConstants.CENTER);
        lblStato.setFont(Constants.LABEL_FONT);
        lblStato.setForeground(Constants.TEXT_WHITE);
        topPanel.add(lblStato, BorderLayout.CENTER);

        // Pulsanti Abbandona e Ricomincia
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnPanel.setOpaque(false);

        btnRicomincia = StyledButton.piccolo("Ricomincia", Constants.BTN_DEFAULT);
        btnRicomincia.addActionListener(e -> {
            if (onRicomincia != null) onRicomincia.run();
        });
        btnPanel.add(btnRicomincia);

        btnAbbandona = StyledButton.piccolo("Menu", Constants.BTN_RED);
        btnAbbandona.addActionListener(e -> {
            int scelta = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Vuoi abbandonare la partita?", "Abbandona",
                    javax.swing.JOptionPane.YES_NO_OPTION);
            if (scelta == javax.swing.JOptionPane.YES_OPTION && onAbbandona != null) {
                onAbbandona.run();
            }
        });
        btnPanel.add(btnAbbandona);

        topPanel.add(btnPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Pannello centrale: tavolo + separatore + mano (stessa larghezza)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        tavoloPanel = new TavoloPanel();
        centerPanel.add(tavoloPanel, BorderLayout.CENTER);

        // Separatore sfumato orizzontale
        JPanel separatore = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint fade = new GradientPaint(
                        0, 0, new Color(0, 45, 0, 0),
                        0, getHeight(), new Color(0, 50, 0, 255));
                g2d.setPaint(fade);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        separatore.setOpaque(false);
        separatore.setPreferredSize(new Dimension(0, 16));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(separatore, BorderLayout.NORTH);

        handPanel = new HandPanel();
        bottomPanel.add(handPanel, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Punteggi (destra)
        scorePanel = new ScorePanel();
        add(scorePanel, BorderLayout.EAST);
    }

    /** Inizializza la schermata con i dati della partita. */
    public void inizializza(String[] nomi, int numGiocatori, int obiettivoTerzi) {
        this.nomiGiocatori = nomi;
        tavoloPanel.setNumGiocatori(numGiocatori);
        tavoloPanel.setNomiGiocatori(nomi);
        tavoloPanel.setNumCarteInMano(new int[numGiocatori]);
        scorePanel.setNomiGiocatori(nomi);
        scorePanel.setPunteggi(new int[numGiocatori]);
        scorePanel.setObiettivoTerzi(obiettivoTerzi);
    }

    /** Imposta gli avatar dei giocatori (score panel e tavolo). */
    public void setAvatars(String[] avatarIds) {
        scorePanel.setAvatars(avatarIds);
        tavoloPanel.setAvatars(avatarIds);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Sfondo verde scuro uniforme
        g2d.setColor(new Color(0, 50, 0));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    // ==================== GameObserver ====================

    @Override
    public void onGameEvent(GameEvent event) {
        switch (event) {
            case DealEvent e -> gestisciDeal(e);
            case CardPlayedEvent e -> gestisciCardPlayed(e);
            case TrickWonEvent e -> gestisciTrickWon(e);
            case RoundOverEvent e -> gestisciRoundOver(e);
            case GameOverEvent e -> gestisciGameOver(e);
            case DeclarationEvent e -> gestisciDeclaration(e);
            case ScoreUpdatedEvent e -> gestisciScoreUpdate(e);
            case PhaseChangedEvent e -> gestisciPhaseChange(e);
        }
    }

    private void gestisciDeal(DealEvent e) {
        tavoloPanel.pulisci();
        lblManoNumero.setText("Mano: " + e.numeroMano());
        lblStato.setText("Carte distribuite!");
    }

    private void gestisciCardPlayed(CardPlayedEvent e) {
        tavoloPanel.aggiungiCartaGiocata(e.indiceGiocatore(), e.carta());
        // Aggiorna stato
        lblStato.setText(e.giocatore().getNome() + " gioca " + e.carta());
    }

    private void gestisciTrickWon(TrickWonEvent e) {
        lblStato.setText(e.vincitore().getNome() + " vince il turno! (+" +
                RegoleTresette.formattaPunti(e.puntiTerzi()) + ")");
    }

    private void gestisciRoundOver(RoundOverEvent e) {
        lblStato.setText("Fine mano " + e.numeroMano() + "!");
    }

    private void gestisciGameOver(GameOverEvent e) {
        lblStato.setText("PARTITA FINITA! Vince " + e.vincitore().getNome());
        if (onGameOver != null) {
            // Ritarda un po' per mostrare il messaggio
            Timer timer = new Timer(2000, evt -> onGameOver.run());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void gestisciDeclaration(DeclarationEvent e) {
        tavoloPanel.mostraMessaggio(
                e.giocatore().getNome() + ": " + e.dichiarazione().getNome() + "!",
                2000);
    }

    private void gestisciScoreUpdate(ScoreUpdatedEvent e) {
        scorePanel.setPunteggi(e.punteggi());
    }

    private void gestisciPhaseChange(PhaseChangedEvent e) {
        if (e.nuovaFase() == GamePhase.PLAYING_TURN) {
            // Nuovo turno: pulisci carte giocate se necessario
        }
    }

    // ==================== Metodi pubblici per il Controller ====================

    /** Aggiorna la mano del giocatore umano. */
    public void aggiornaManoUmano(List<Carta> mano) {
        handPanel.setCarte(mano);
    }

    /** Mostra le carte valide e abilita l'input. */
    public void abilitaInput(List<Carta> carteValide) {
        handPanel.setCarteValide(carteValide);
        handPanel.setInputAbilitato(true);
        lblStato.setText("E' il tuo turno! Scegli una carta.");
    }

    /** Disabilita l'input del giocatore. */
    public void disabilitaInput() {
        handPanel.setInputAbilitato(false);
    }

    /** Pulisce il tavolo per un nuovo turno. */
    public void pulisciTavolo() {
        tavoloPanel.pulisci();
    }

    /** Aggiorna il conteggio carte in mano dei giocatori AI. */
    public void aggiornaCarteAI(int[] numCarte) {
        tavoloPanel.setNumCarteInMano(numCarte);
    }

    public HandPanel getHandPanel() { return handPanel; }
    public TavoloPanel getTavoloPanel() { return tavoloPanel; }
    public ScorePanel getScorePanel() { return scorePanel; }

    public void setOnGameOver(Runnable callback) { this.onGameOver = callback; }
    public void setOnAbbandona(Runnable callback) { this.onAbbandona = callback; }
    public void setOnRicomincia(Runnable callback) { this.onRicomincia = callback; }
}
