package it.uniroma1.mdp.jtresette.model.game;

/**
 * Rappresenta le dichiarazioni possibili nel Tresette.
 *
 * - BUSSO: indica possesso di buone carte (segnale al compagno)
 * - VOLO: si gioca l'ultima carta di un seme
 * - STRISCIO: 3+ carte dello stesso seme incluso il Tre (segnale)
 * - NAPOLITANA: Asso, Due, Tre dello stesso seme (vale 3 punti bonus)
 * - TRESETTE: tre carte di valore Tre (vale 4 punti bonus)
 */
public enum Dichiarazione {
    BUSSO("Busso", "Possesso di buone carte", 0),
    VOLO("Volo", "Ultima carta di un seme", 0),
    STRISCIO("Striscio", "3+ carte dello stesso seme con il Tre", 0),
    NAPOLITANA("Napolitana", "Asso, Due, Tre dello stesso seme", 9),   // 3 punti = 9 terzi
    TRESETTE("Tresette", "Tre carte di valore Tre", 12);               // 4 punti = 12 terzi

    private final String nome;
    private final String descrizione;
    private final int bonusTerzi;

    Dichiarazione(String nome, String descrizione, int bonusTerzi) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.bonusTerzi = bonusTerzi;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    /** Punti bonus in terzi assegnati per questa dichiarazione. */
    public int getBonusTerzi() {
        return bonusTerzi;
    }

    @Override
    public String toString() {
        return nome;
    }
}
