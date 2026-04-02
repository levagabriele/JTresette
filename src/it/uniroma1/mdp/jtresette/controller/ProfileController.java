package it.uniroma1.mdp.jtresette.controller;

import it.uniroma1.mdp.jtresette.model.profile.ProfileManager;
import it.uniroma1.mdp.jtresette.model.profile.ProfiloGiocatore;
import it.uniroma1.mdp.jtresette.model.profile.Statistiche;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.MainFrame;
import it.uniroma1.mdp.jtresette.view.screens.ProfileScreen;

/**
 * Controller per la gestione del profilo utente.
 */
public class ProfileController {

    private final MainFrame mainFrame;
    private final ProfileManager profileManager;

    public ProfileController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.profileManager = ProfileManager.getInstance();
        inizializzaListeners();
    }

    private void inizializzaListeners() {
        ProfileScreen screen = mainFrame.getProfileScreen();
        screen.addSalvaListener(e -> salvaProfilo());
        screen.addIndietroListener(e -> mainFrame.mostraSchermata(Constants.SCREEN_MENU));
    }

    /** Carica e mostra i dati del profilo nella schermata. */
    public void mostraProfilo() {
        ProfiloGiocatore profilo = profileManager.getProfilo();
        Statistiche stats = profilo.getStatistiche();
        ProfileScreen screen = mainFrame.getProfileScreen();

        screen.setNickname(profilo.getNickname());
        screen.setAvatar(profilo.getAvatarPath());
        screen.setStatistiche(
                stats.getPartiteGiocate(),
                stats.getPartiteVinte(),
                stats.getPartitePerse(),
                profilo.getLivello()
        );
    }

    private void salvaProfilo() {
        ProfileScreen screen = mainFrame.getProfileScreen();
        String nickname = screen.getNickname();
        String avatarId = screen.getSelectedAvatarId();

        if (!nickname.isEmpty()) {
            profileManager.aggiornaNickname(nickname);
        }
        profileManager.aggiornaAvatar(avatarId);
        screen.mostraConferma("Profilo salvato!");
    }
}