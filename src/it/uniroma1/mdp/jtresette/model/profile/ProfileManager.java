package it.uniroma1.mdp.jtresette.model.profile;

import java.io.IOException;

import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.util.JsonUtil;

/**
 * Singleton che gestisce il caricamento e salvataggio del profilo giocatore.
 */
public class ProfileManager {

    private static ProfileManager instance;
    private ProfiloGiocatore profilo;

    private ProfileManager() {
        carica();
    }

    public static synchronized ProfileManager getInstance() {
        if (instance == null) {
            instance = new ProfileManager();
        }
        return instance;
    }

    /** Carica il profilo dal file JSON. Se non esiste, crea un profilo default. */
    private void carica() {
        try {
            String json = JsonUtil.leggiFile(Constants.PROFILE_PATH);
            profilo = JsonUtil.fromJson(json);
        } catch (IOException e) {
            // Primo avvio: crea profilo default
            profilo = new ProfiloGiocatore();
            salva();
        }
    }

    /** Salva il profilo su file JSON. */
    public void salva() {
        try {
            String json = JsonUtil.toJson(profilo);
            JsonUtil.salvaFile(json, Constants.PROFILE_PATH);
        } catch (IOException e) {
            System.err.println("Errore salvataggio profilo: " + e.getMessage());
        }
    }

    /** Registra la fine di una partita. Restituisce gli XP guadagnati. */
    public int registraPartita(boolean vittoria, int puntiSegnati) {
        int xpGuadagnati;
        if (vittoria) {
            xpGuadagnati = profilo.getStatistiche().registraVittoria(puntiSegnati);
        } else {
            xpGuadagnati = profilo.getStatistiche().registraSconfitta(puntiSegnati);
        }
        salva();
        return xpGuadagnati;
    }

    public ProfiloGiocatore getProfilo() {
        return profilo;
    }

    /** Aggiorna il nickname e salva. */
    public void aggiornaNickname(String nickname) {
        profilo.setNickname(nickname);
        salva();
    }

    /** Aggiorna l'avatar e salva. */
    public void aggiornaAvatar(String avatarId) {
        profilo.setAvatarPath(avatarId);
        salva();
    }
}
