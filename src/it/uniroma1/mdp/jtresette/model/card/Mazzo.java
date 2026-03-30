package it.uniroma1.mdp.jtresette.model.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rappresenta un mazzo di 40 carte napoletane.
 * Supporta mescolamento e distribuzione per 2, 3 o 4 giocatori.
 */
public class Mazzo {

    private final List<Carta> carte;

    /**
     * Crea un mazzo completo di 40 carte usando Stream.
     */
    public Mazzo() {
        this.carte = Arrays.stream(Seme.values())
                .flatMap(seme -> Arrays.stream(Valore.values())
                        .map(valore -> new Carta(seme, valore)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /** Mescola il mazzo. */
    public void mescola() {
        Collections.shuffle(carte);
    }

    /**
     * Distribuisce le carte ai giocatori.
     *
     * - 2 giocatori: 10 carte ciascuno (20 carte inutilizzate)
     * - 3 giocatori: si rimuovono i Quattro (36 carte), 12 ciascuno
     * - 4 giocatori: 10 carte ciascuno (standard)
     *
     * @param numGiocatori numero di giocatori (2, 3 o 4)
     * @return lista di mani (una lista di carte per ogni giocatore)
     * @throws IllegalArgumentException se numGiocatori non e' 2, 3 o 4
     */
    public List<List<Carta>> distribuisci(int numGiocatori) {
        if (numGiocatori < 2 || numGiocatori > 4) {
            throw new IllegalArgumentException("Numero giocatori deve essere 2, 3 o 4");
        }

        List<Carta> mazzoGioco;
        int cartePerGiocatore;

        if (numGiocatori == 3) {
            // Rimuovi i Quattro per avere 36 carte = 12 per giocatore
            mazzoGioco = carte.stream()
                    .filter(c -> c.getValore() != Valore.QUATTRO)
                    .collect(Collectors.toCollection(ArrayList::new));
            cartePerGiocatore = 12;
        } else {
            mazzoGioco = new ArrayList<>(carte);
            cartePerGiocatore = 10;
        }

        Collections.shuffle(mazzoGioco);

        List<List<Carta>> mani = new ArrayList<>();
        for (int i = 0; i < numGiocatori; i++) {
            int inizio = i * cartePerGiocatore;
            int fine = inizio + cartePerGiocatore;
            mani.add(new ArrayList<>(mazzoGioco.subList(inizio, fine)));
        }

        return mani;
    }

    /** Restituisce una copia immutabile delle carte nel mazzo. */
    public List<Carta> getCarte() {
        return Collections.unmodifiableList(carte);
    }

    /** Numero di carte nel mazzo. */
    public int size() {
        return carte.size();
    }
}
