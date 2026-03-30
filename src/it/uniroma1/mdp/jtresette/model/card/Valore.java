package it.uniroma1.mdp.jtresette.model.card;

/**
 * Rappresenta i valori delle carte napoletane con punti e forza di presa.
 *
 * Gerarchia di presa: 3 > 2 > Asso > Re > Cavallo > Fante > 7 > 6 > 5 > 4
 * Punti (in terzi): Asso = 3 terzi (1 punto), Due/Tre/Re/Cavallo/Fante = 1 terzo (1/3 punto)
 * Quattro/Cinque/Sei/Sette = 0 punti
 */
public enum Valore {
    ASSO(1, 3, 8, "Asso"),
    DUE(2, 1, 9, "Due"),
    TRE(3, 1, 10, "Tre"),
    QUATTRO(4, 0, 1, "Quattro"),
    CINQUE(5, 0, 2, "Cinque"),
    SEI(6, 0, 3, "Sei"),
    SETTE(7, 0, 4, "Sette"),
    FANTE(8, 1, 5, "Fante"),
    CAVALLO(9, 1, 6, "Cavallo"),
    RE(10, 1, 7, "Re");

    private final int numero;
    private final int puntiTerzi;
    private final int forzaDiPresa;
    private final String nome;

    Valore(int numero, int puntiTerzi, int forzaDiPresa, String nome) {
        this.numero = numero;
        this.puntiTerzi = puntiTerzi;
        this.forzaDiPresa = forzaDiPresa;
        this.nome = nome;
    }

    /** Numero sulla carta (1-10). */
    public int getNumero() {
        return numero;
    }

    /** Punti in terzi: Asso=3, figure e 2/3=1, resto=0. */
    public int getPuntiTerzi() {
        return puntiTerzi;
    }

    /** Forza di presa (1=piu debole, 10=piu forte). Tre=10, Due=9, Asso=8, ... */
    public int getForzaDiPresa() {
        return forzaDiPresa;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
