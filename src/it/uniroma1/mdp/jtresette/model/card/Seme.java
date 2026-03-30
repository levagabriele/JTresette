package it.uniroma1.mdp.jtresette.model.card;

/**
 * Rappresenta i quattro semi delle carte napoletane.
 */
public enum Seme {
    COPPE("Coppe"),
    DENARI("Denari"),
    BASTONI("Bastoni"),
    SPADE("Spade");

    private final String nome;

    Seme(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
