package it.uniroma1.mdp.jtresette.model.game;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.model.card.Seme;

/**
 * Rappresenta un singolo turno (trick) del Tresette.
 * Ogni giocatore gioca una carta; vince chi ha la carta piu' forte del seme guida.
 */
public class Turno {

    private final int numGiocatori;
    private final int giocatoreIniziale;
    private final List<Map.Entry<Integer, Carta>> carteGiocate;
    private Seme semeGuida;

    public Turno(int numGiocatori, int giocatoreIniziale) {
        this.numGiocatori = numGiocatori;
        this.giocatoreIniziale = giocatoreIniziale;
        this.carteGiocate = new ArrayList<>();
        this.semeGuida = null;
    }

    /**
     * Aggiunge una carta giocata al turno.
     *
     * @param indiceGiocatore l'indice del giocatore che ha giocato
     * @param carta la carta giocata
     */
    public void aggiungiCarta(int indiceGiocatore, Carta carta) {
        if (isCompleto()) {
            throw new IllegalStateException("Il turno e' gia' completo");
        }
        if (carteGiocate.isEmpty()) {
            semeGuida = carta.getSeme();
        }
        carteGiocate.add(new AbstractMap.SimpleEntry<>(indiceGiocatore, carta));
    }

    /** Il turno e' completo quando tutti i giocatori hanno giocato. */
    public boolean isCompleto() {
        return carteGiocate.size() == numGiocatori;
    }

    /** Seme della prima carta giocata (null se nessuna carta giocata). */
    public Seme getSemeGuida() {
        return semeGuida;
    }

    /** Indice del giocatore che ha aperto il turno. */
    public int getGiocatoreIniziale() {
        return giocatoreIniziale;
    }

    /** Carte giocate finora in questo turno (vista non modificabile). */
    public List<Map.Entry<Integer, Carta>> getCarteGiocate() {
        return Collections.unmodifiableList(carteGiocate);
    }

    /** Restituisce solo le carte (senza indici) giocate finora. */
    public List<Carta> getCarteSolamente() {
        return carteGiocate.stream()
                .map(Map.Entry::getValue)
                .toList();
    }

    /**
     * Determina il vincitore del turno.
     *
     * @return indice del giocatore vincitore
     * @throws IllegalStateException se il turno non e' completo
     */
    public int calcolaVincitore() {
        if (!isCompleto()) {
            throw new IllegalStateException("Il turno non e' ancora completo");
        }
        return RegoleTresette.vincitoreTurno(carteGiocate, semeGuida);
    }

    /** Calcola i punti (in terzi) delle carte giocate in questo turno. */
    public int calcolaPuntiTerzi() {
        return RegoleTresette.calcolaPunti(getCarteSolamente());
    }

    /** Indice del prossimo giocatore che deve giocare. */
    public int getProssimoGiocatore() {
        if (isCompleto()) return -1;
        int offset = carteGiocate.size();
        return (giocatoreIniziale + offset) % numGiocatori;
    }

    /** Numero di carte giocate finora. */
    public int getNumCarteGiocate() {
        return carteGiocate.size();
    }
}
