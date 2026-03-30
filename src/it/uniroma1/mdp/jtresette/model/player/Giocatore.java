package it.uniroma1.mdp.jtresette.model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.uniroma1.mdp.jtresette.model.card.Carta;

/**
 * Classe astratta che rappresenta un giocatore di Tresette.
 * Gestisce la mano di carte e fornisce operazioni comuni.
 */
public abstract class Giocatore {

    private final String nome;
    private final int indice;
    private final List<Carta> mano;

    protected Giocatore(String nome, int indice) {
        this.nome = nome;
        this.indice = indice;
        this.mano = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public int getIndice() {
        return indice;
    }

    /** Restituisce una vista non modificabile della mano. */
    public List<Carta> getMano() {
        return Collections.unmodifiableList(mano);
    }

    /** Numero di carte in mano. */
    public int getNumCarte() {
        return mano.size();
    }

    /** Aggiunge carte alla mano (usato durante la distribuzione). */
    public void riceviCarte(List<Carta> carte) {
        mano.addAll(carte);
        Collections.sort(mano);
    }

    /** Rimuove una carta dalla mano dopo averla giocata. */
    public void rimuoviCarta(Carta carta) {
        if (!mano.remove(carta)) {
            throw new IllegalArgumentException("Carta non presente nella mano: " + carta);
        }
    }

    /** Svuota la mano (inizio nuova distribuzione). */
    public void svuotaMano() {
        mano.clear();
    }

    /** Indica se il giocatore e' umano. */
    public abstract boolean isUmano();

    @Override
    public String toString() {
        return nome;
    }
}
