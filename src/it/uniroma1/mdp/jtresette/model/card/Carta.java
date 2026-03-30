package it.uniroma1.mdp.jtresette.model.card;

import java.util.Objects;

/**
 * Rappresenta una carta da gioco napoletana (immutabile).
 * Una carta e' definita dal suo seme e dal suo valore.
 */
public final class Carta implements Comparable<Carta> {

    private final Seme seme;
    private final Valore valore;

    public Carta(Seme seme, Valore valore) {
        this.seme = Objects.requireNonNull(seme);
        this.valore = Objects.requireNonNull(valore);
    }

    public Seme getSeme() {
        return seme;
    }

    public Valore getValore() {
        return valore;
    }

    /** Punti in terzi di questa carta. */
    public int getPuntiTerzi() {
        return valore.getPuntiTerzi();
    }

    /** Forza di presa di questa carta (utile per determinare il vincitore del turno). */
    public int getForzaDiPresa() {
        return valore.getForzaDiPresa();
    }

    /**
     * Restituisce il path dell'immagine relativo alla cartella resources.
     * Formato: images/cards/{seme}_{numero:02d}.png
     */
    public String getImagePath() {
        return String.format("resources/images/cards/%s_%02d.png",
                seme.name().toLowerCase(), valore.getNumero());
    }

    @Override
    public int compareTo(Carta other) {
        int cmp = this.seme.compareTo(other.seme);
        if (cmp != 0) return cmp;
        return Integer.compare(this.valore.getForzaDiPresa(), other.valore.getForzaDiPresa());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carta carta)) return false;
        return seme == carta.seme && valore == carta.valore;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seme, valore);
    }

    @Override
    public String toString() {
        return valore.getNome() + " di " + seme.getNome();
    }
}
