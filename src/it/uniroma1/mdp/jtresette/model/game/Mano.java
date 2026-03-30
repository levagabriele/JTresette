package it.uniroma1.mdp.jtresette.model.game;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;

/**
 * Rappresenta una mano (round) di Tresette: dalla distribuzione delle carte
 * alla fine di tutti i turni. Ogni mano contiene N turni (10 per 4 giocatori,
 * 12 per 3 giocatori, 10 per 2 giocatori).
 */
public class Mano {

    private final int numGiocatori;
    private final int numTurni;
    private final List<Turno> turniCompletati;
    private final List<List<Carta>> cartePrese; // carte vinte per ogni giocatore
    private Turno turnoCorrente;
    private int prossimoIniziale;

    /**
     * Crea una nuova mano.
     *
     * @param numGiocatori numero di giocatori
     * @param mazziere indice del mazziere (il giocatore dopo il mazziere inizia)
     */
    public Mano(int numGiocatori, int mazziere) {
        this.numGiocatori = numGiocatori;
        this.numTurni = (numGiocatori == 3) ? 12 : 10;
        this.turniCompletati = new ArrayList<>();
        this.cartePrese = new ArrayList<>();
        for (int i = 0; i < numGiocatori; i++) {
            cartePrese.add(new ArrayList<>());
        }
        // Il giocatore a destra del mazziere inizia (il successivo)
        this.prossimoIniziale = (mazziere + 1) % numGiocatori;
    }

    /** Inizia un nuovo turno. */
    public Turno iniziaTurno() {
        turnoCorrente = new Turno(numGiocatori, prossimoIniziale);
        return turnoCorrente;
    }

    /**
     * Gioca una carta nel turno corrente.
     *
     * @param indiceGiocatore indice del giocatore
     * @param carta la carta giocata
     */
    public void giocaCarta(int indiceGiocatore, Carta carta) {
        if (turnoCorrente == null) {
            throw new IllegalStateException("Nessun turno in corso");
        }
        turnoCorrente.aggiungiCarta(indiceGiocatore, carta);
    }

    /**
     * Chiude il turno corrente, assegna le carte al vincitore.
     *
     * @param isUltimoTurno se true, aggiunge il bonus dell'ultimo turno (1 terzo)
     * @return indice del vincitore del turno
     */
    public int chiudiTurno(boolean isUltimoTurno) {
        if (turnoCorrente == null || !turnoCorrente.isCompleto()) {
            throw new IllegalStateException("Il turno non e' completo");
        }

        int vincitore = turnoCorrente.calcolaVincitore();
        cartePrese.get(vincitore).addAll(turnoCorrente.getCarteSolamente());
        turniCompletati.add(turnoCorrente);

        // Il vincitore apre il prossimo turno
        prossimoIniziale = vincitore;
        turnoCorrente = null;

        return vincitore;
    }

    /** Verifica se la mano e' finita (tutti i turni giocati). */
    public boolean isCompleta() {
        return turniCompletati.size() == numTurni;
    }

    /**
     * Calcola i punti (in terzi) per ogni giocatore nella mano.
     * L'ultimo turno vale 1 terzo bonus per il vincitore.
     *
     * @return array di punti in terzi, indicizzato per giocatore
     */
    public int[] calcolaPunteggio() {
        int[] punti = new int[numGiocatori];
        for (int i = 0; i < numGiocatori; i++) {
            punti[i] = RegoleTresette.calcolaPunti(cartePrese.get(i));
        }
        // Bonus ultimo turno: 1 terzo al vincitore dell'ultimo turno
        if (!turniCompletati.isEmpty()) {
            Turno ultimo = turniCompletati.get(turniCompletati.size() - 1);
            int vincitoreUltimo = ultimo.calcolaVincitore();
            punti[vincitoreUltimo] += 1; // 1 terzo
        }
        return punti;
    }

    /** Turno corrente (null se nessun turno in corso). */
    public Turno getTurnoCorrente() {
        return turnoCorrente;
    }

    /** Numero di turni completati. */
    public int getNumTurniCompletati() {
        return turniCompletati.size();
    }

    /** Lista dei turni completati. */
    public List<Turno> getTurniCompletati() {
        return java.util.Collections.unmodifiableList(turniCompletati);
    }

    /** Numero totale di turni nella mano. */
    public int getNumTurni() {
        return numTurni;
    }

    /** Carte prese da un giocatore. */
    public List<Carta> getCartePrese(int indiceGiocatore) {
        return cartePrese.get(indiceGiocatore);
    }

    /** Tutte le carte giocate nella mano (per card counting AI). */
    public List<Carta> getTutteLeCarteGiocate() {
        List<Carta> tutte = new ArrayList<>();
        for (List<Carta> prese : cartePrese) {
            tutte.addAll(prese);
        }
        if (turnoCorrente != null) {
            tutte.addAll(turnoCorrente.getCarteSolamente());
        }
        return tutte;
    }

    /** Indice del giocatore che deve iniziare il prossimo turno. */
    public int getProssimoIniziale() {
        return prossimoIniziale;
    }
}
