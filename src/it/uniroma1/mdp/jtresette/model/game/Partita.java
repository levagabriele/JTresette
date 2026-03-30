package it.uniroma1.mdp.jtresette.model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.Mazzo;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.model.card.Seme;
import it.uniroma1.mdp.jtresette.model.event.*;
import it.uniroma1.mdp.jtresette.model.GameObservable;
import it.uniroma1.mdp.jtresette.model.player.Giocatore;

/**
 * Classe centrale del modello. Gestisce una partita completa di Tresette,
 * composta da piu' mani, fino al raggiungimento del punteggio obiettivo.
 * Estende GameObservable per notificare la View di ogni cambiamento di stato.
 */
public class Partita extends GameObservable {

    private final List<Giocatore> giocatori;
    private final int[] punteggiTotali;
    private final int obiettivoTerzi;
    private final List<Carta> carteGiocateStorico; // tutte le carte uscite nella mano corrente
    private Mano manoCorrente;
    private GamePhase fase;
    private int mazziere;
    private int numeroMano;

    /**
     * Crea una nuova partita.
     *
     * @param giocatori lista dei giocatori (umano + AI)
     * @param obiettivoTerzi punteggio obiettivo in terzi (63 = 21 punti)
     */
    public Partita(List<Giocatore> giocatori, int obiettivoTerzi) {
        this.giocatori = new ArrayList<>(giocatori);
        this.punteggiTotali = new int[giocatori.size()];
        this.obiettivoTerzi = obiettivoTerzi;
        this.carteGiocateStorico = new ArrayList<>();
        this.fase = GamePhase.SETUP;
        this.mazziere = 0;
        this.numeroMano = 0;
    }

    /** Crea con obiettivo default di 21 punti. */
    public Partita(List<Giocatore> giocatori) {
        this(giocatori, RegoleTresette.OBIETTIVO_DEFAULT_TERZI);
    }

    // ==================== Flusso di gioco ====================

    /** Cambia fase e notifica gli observer. */
    private void cambiaFase(GamePhase nuovaFase) {
        GamePhase vecchiaFase = this.fase;
        this.fase = nuovaFase;
        notifyObservers(new PhaseChangedEvent(vecchiaFase, nuovaFase));
    }

    /**
     * Inizia una nuova mano: mescola, distribuisce, controlla dichiarazioni.
     */
    public void iniziaMano() {
        numeroMano++;
        carteGiocateStorico.clear();
        cambiaFase(GamePhase.DEALING);

        // Svuota mani dei giocatori
        giocatori.forEach(Giocatore::svuotaMano);

        // Crea e distribuisci un nuovo mazzo
        Mazzo mazzo = new Mazzo();
        mazzo.mescola();
        List<List<Carta>> mani = mazzo.distribuisci(giocatori.size());
        for (int i = 0; i < giocatori.size(); i++) {
            giocatori.get(i).riceviCarte(mani.get(i));
        }

        manoCorrente = new Mano(giocatori.size(), mazziere);
        notifyObservers(new DealEvent(mani.get(0).size(), numeroMano));

        // Verifica dichiarazioni
        cambiaFase(GamePhase.DECLARING);
        for (int i = 0; i < giocatori.size(); i++) {
            List<Map.Entry<Dichiarazione, List<Carta>>> dichiarazioni =
                    RegoleTresette.verificaDichiarazioni(giocatori.get(i).getMano());
            for (Map.Entry<Dichiarazione, List<Carta>> dich : dichiarazioni) {
                // Aggiungi bonus ai punti
                punteggiTotali[i] += dich.getKey().getBonusTerzi();
                notifyObservers(new DeclarationEvent(giocatori.get(i), dich.getKey(), dich.getValue()));
            }
        }

        notifyObservers(new ScoreUpdatedEvent(punteggiTotali.clone()));
    }

    /** Inizia un nuovo turno nella mano corrente. */
    public Turno iniziaTurno() {
        cambiaFase(GamePhase.PLAYING_TURN);
        return manoCorrente.iniziaTurno();
    }

    /**
     * Gioca una carta nel turno corrente.
     *
     * @param indiceGiocatore indice del giocatore
     * @param carta la carta giocata
     */
    public void giocaCarta(int indiceGiocatore, Carta carta) {
        Giocatore g = giocatori.get(indiceGiocatore);
        g.rimuoviCarta(carta);
        manoCorrente.giocaCarta(indiceGiocatore, carta);
        carteGiocateStorico.add(carta);
        notifyObservers(new CardPlayedEvent(g, carta, indiceGiocatore));
    }

    /**
     * Chiude il turno corrente.
     *
     * @return indice del vincitore del turno
     */
    public int chiudiTurno() {
        boolean isUltimoTurno = manoCorrente.getNumTurniCompletati() + 1 == manoCorrente.getNumTurni();
        int vincitore = manoCorrente.chiudiTurno(isUltimoTurno);

        cambiaFase(GamePhase.TRICK_COMPLETE);
        Turno turnoAppenaChiuso = getTurniCompletati().get(getTurniCompletati().size() - 1);
        notifyObservers(new TrickWonEvent(giocatori.get(vincitore), vincitore,
                turnoAppenaChiuso.getCarteSolamente(), turnoAppenaChiuso.calcolaPuntiTerzi()));

        return vincitore;
    }

    /**
     * Chiude la mano corrente, aggiorna i punteggi totali.
     *
     * @return array dei punti guadagnati in questa mano (in terzi)
     */
    public int[] chiudiMano() {
        int[] puntiMano = manoCorrente.calcolaPunteggio();
        for (int i = 0; i < giocatori.size(); i++) {
            punteggiTotali[i] += puntiMano[i];
        }

        // Ruota il mazziere
        mazziere = (mazziere + 1) % giocatori.size();

        cambiaFase(GamePhase.ROUND_COMPLETE);
        notifyObservers(new RoundOverEvent(puntiMano, punteggiTotali.clone(), numeroMano));
        notifyObservers(new ScoreUpdatedEvent(punteggiTotali.clone()));

        return puntiMano;
    }

    /**
     * Verifica se la partita e' finita (qualcuno ha raggiunto l'obiettivo).
     *
     * @return true se la partita e' terminata
     */
    public boolean isFinita() {
        for (int p : punteggiTotali) {
            if (p >= obiettivoTerzi) return true;
        }
        return false;
    }

    /**
     * Determina il vincitore della partita.
     *
     * @return indice del vincitore, o -1 se nessuno ha ancora vinto
     */
    public int getVincitore() {
        int maxPunti = -1;
        int vincitore = -1;
        for (int i = 0; i < punteggiTotali.length; i++) {
            if (punteggiTotali[i] >= obiettivoTerzi && punteggiTotali[i] > maxPunti) {
                maxPunti = punteggiTotali[i];
                vincitore = i;
            }
        }
        return vincitore;
    }

    /** Termina la partita. */
    public void terminaPartita() {
        int vincitore = getVincitore();
        cambiaFase(GamePhase.GAME_OVER);
        notifyObservers(new GameOverEvent(
                vincitore >= 0 ? giocatori.get(vincitore) : null,
                vincitore,
                punteggiTotali.clone()));
    }

    // ==================== Getters ====================

    public List<Giocatore> getGiocatori() {
        return Collections.unmodifiableList(giocatori);
    }

    public int getNumGiocatori() {
        return giocatori.size();
    }

    public int[] getPunteggiTotali() {
        return punteggiTotali.clone();
    }

    public int getObiettivoTerzi() {
        return obiettivoTerzi;
    }

    public Mano getManoCorrente() {
        return manoCorrente;
    }

    public GamePhase getFase() {
        return fase;
    }

    public int getMazziere() {
        return mazziere;
    }

    public int getNumeroMano() {
        return numeroMano;
    }

    public List<Carta> getCarteGiocateStorico() {
        return Collections.unmodifiableList(carteGiocateStorico);
    }

    /** Seme guida del turno corrente (null se nessun turno attivo o prima carta). */
    public Seme getSemeGuidaCorrente() {
        if (manoCorrente == null || manoCorrente.getTurnoCorrente() == null) return null;
        return manoCorrente.getTurnoCorrente().getSemeGuida();
    }

    private List<Turno> getTurniCompletati() {
        if (manoCorrente == null) return List.of();
        return manoCorrente.getTurniCompletati();
    }
}
