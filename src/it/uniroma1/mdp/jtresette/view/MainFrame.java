package it.uniroma1.mdp.jtresette.view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.screens.*;

/**
 * Finestra principale dell'applicazione JTresette.
 * Usa CardLayout per gestire la navigazione tra schermate.
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

    public MainFrame() {
        super("JTresette");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_SIZE);
        setMinimumSize(Constants.WINDOW_SIZE);
        setLocationRelativeTo(null);
        setResizable(false);

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

        add(mainPanel);
        mostraSchermata(Constants.SCREEN_MENU);
    }

    /** Mostra una schermata dato il suo nome. */
    public void mostraSchermata(String nome) {
        cardLayout.show(mainPanel, nome);
    }

    /** Imposta e mostra la schermata di gioco. */
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
}
