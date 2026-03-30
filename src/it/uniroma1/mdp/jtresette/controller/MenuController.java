package it.uniroma1.mdp.jtresette.controller;

import it.uniroma1.mdp.jtresette.model.profile.ProfileManager;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.MainFrame;

/**
 * Controller per la navigazione tra le schermate del menu.
 */
public class MenuController {

    private final MainFrame mainFrame;
    private final ProfileController profileController;

    public MenuController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.profileController = new ProfileController(mainFrame);
        inizializzaListeners();
    }

    private void inizializzaListeners() {
        // Menu principale
        mainFrame.getMenuScreen().addNuovaPartitaListener(
                e -> mainFrame.mostraSchermata(Constants.SCREEN_SETUP));
        mainFrame.getMenuScreen().addProfiloListener(e -> {
            profileController.mostraProfilo();
            mainFrame.mostraSchermata(Constants.SCREEN_PROFILE);
        });
        mainFrame.getMenuScreen().addEsciListener(
                e -> System.exit(0));

        // Setup partita
        mainFrame.getGameSetupScreen().addIndietroListener(
                e -> mainFrame.mostraSchermata(Constants.SCREEN_MENU));
        mainFrame.getGameSetupScreen().addIniziaListener(
                e -> iniziaPartita());

        // Profilo
        mainFrame.getProfileScreen().addIndietroListener(
                e -> mainFrame.mostraSchermata(Constants.SCREEN_MENU));

        // Game Over
        mainFrame.getGameOverScreen().addMenuListener(
                e -> mainFrame.mostraSchermata(Constants.SCREEN_MENU));
        mainFrame.getGameOverScreen().addNuovaPartitaListener(
                e -> mainFrame.mostraSchermata(Constants.SCREEN_SETUP));
    }

    private void iniziaPartita() {
        int numGiocatori = mainFrame.getGameSetupScreen().getNumGiocatoriTotali();
        int difficolta = mainFrame.getGameSetupScreen().getLivelloDifficolta();

        GameController gameController = new GameController(mainFrame, numGiocatori, difficolta);
        gameController.avviaPartita();
    }
}
