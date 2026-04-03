package it.uniroma1.mdp.jtresette.view;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.screens.*;

/**
 * Finestra principale dell'applicazione JTresette.
 * Usa CardLayout per gestire la navigazione tra schermate
 * con transizione fade tra le schermate.
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private MenuScreen menuScreen;
    private GameSetupScreen gameSetupScreen;
    private GameScreen gameScreen;
    private ProfileScreen profileScreen;
    private GameOverScreen gameOverScreen;

    // Fade transition
    private final FadeOverlay fadeOverlay;
    private static final int FADE_DURATION_MS = 250;

    public MainFrame() {
        super("JTresette");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_SIZE);
        setMinimumSize(Constants.WINDOW_SIZE);
        setLocationRelativeTo(null);

        // Icona applicazione
        File iconFile = new File(Constants.RESOURCES_PATH + "images/icon.png");
        if (iconFile.exists()) {
            setIconImage(Toolkit.getDefaultToolkit().getImage(iconFile.getAbsolutePath()));
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inizializza schermate
        menuScreen = new MenuScreen();
        gameSetupScreen = new GameSetupScreen();
        profileScreen = new ProfileScreen();
        gameOverScreen = new GameOverScreen();

        // Aggiungi schermate
        mainPanel.add(menuScreen, Constants.SCREEN_MENU);
        mainPanel.add(gameSetupScreen, Constants.SCREEN_SETUP);
        mainPanel.add(profileScreen, Constants.SCREEN_PROFILE);
        mainPanel.add(gameOverScreen, Constants.SCREEN_GAME_OVER);

        // Overlay per fade
        fadeOverlay = new FadeOverlay();
        setGlassPane(fadeOverlay);

        add(mainPanel);
        mostraSchermata(Constants.SCREEN_MENU);
    }

    /** Mostra una schermata con transizione fade. */
    public void mostraSchermata(String nome) {
        // Se non ancora visibile, mostra direttamente
        if (!isVisible()) {
            cardLayout.show(mainPanel, nome);
            return;
        }
        fadeOverlay.eseguiFade(() -> cardLayout.show(mainPanel, nome));
    }

    /** Imposta e mostra la schermata di gioco (senza fade per non ritardare l'inizio). */
    public void setGameScreen(GameScreen screen) {
        if (this.gameScreen != null) {
            mainPanel.remove(this.gameScreen);
        }
        this.gameScreen = screen;
        mainPanel.add(gameScreen, Constants.SCREEN_GAME);
        mostraSchermata(Constants.SCREEN_GAME);
    }

    public MenuScreen getMenuScreen() { return menuScreen; }
    public GameSetupScreen getGameSetupScreen() { return gameSetupScreen; }
    public GameScreen getGameScreen() { return gameScreen; }
    public ProfileScreen getProfileScreen() { return profileScreen; }
    public GameOverScreen getGameOverScreen() { return gameOverScreen; }
    public JPanel getMainPanel() { return mainPanel; }

    /**
     * Pannello overlay trasparente usato come glass pane per
     * l'effetto fade-out/fade-in tra schermate.
     */
    private class FadeOverlay extends JPanel {

        private float alpha = 0f;
        private Timer timer;
        private boolean fadingOut = true;
        private Runnable onMidFade;
        private long fadeInizio;

        FadeOverlay() {
            setOpaque(false);
        }

        void eseguiFade(Runnable midAction) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
                // Esegui subito l'azione in caso di fade interrotto
                midAction.run();
                alpha = 0f;
                setVisible(false);
                repaint();
                return;
            }

            this.onMidFade = midAction;
            this.fadingOut = true;
            this.alpha = 0f;
            this.fadeInizio = System.currentTimeMillis();
            setVisible(true);

            timer = new Timer(Constants.ANIMATION_DELAY_MS, e -> aggiornaFade());
            timer.start();
        }

        private void aggiornaFade() {
            long elapsed = System.currentTimeMillis() - fadeInizio;

            if (fadingOut) {
                // Fade out: 0 -> 1
                alpha = Math.min(1f, (float) elapsed / FADE_DURATION_MS);
                if (alpha >= 1f) {
                    // Meta' della transizione: cambia schermata
                    if (onMidFade != null) onMidFade.run();
                    fadingOut = false;
                    fadeInizio = System.currentTimeMillis();
                }
            } else {
                // Fade in: 1 -> 0
                alpha = 1f - Math.min(1f, (float) elapsed / FADE_DURATION_MS);
                if (alpha <= 0f) {
                    alpha = 0f;
                    timer.stop();
                    setVisible(false);
                }
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (alpha > 0f) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        }
    }
}
