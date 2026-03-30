package it.uniroma1.mdp.jtresette.model.profile;

/**
 * Statistiche di gioco di un giocatore.
 */
public class Statistiche {

    private int partiteGiocate;
    private int partiteVinte;
    private int partitePerse;
    private int puntiTotaliSegnati;

    public Statistiche() {
        this(0, 0, 0, 0);
    }

    public Statistiche(int partiteGiocate, int partiteVinte, int partitePerse, int puntiTotaliSegnati) {
        this.partiteGiocate = partiteGiocate;
        this.partiteVinte = partiteVinte;
        this.partitePerse = partitePerse;
        this.puntiTotaliSegnati = puntiTotaliSegnati;
    }

    public void registraVittoria(int puntiSegnati) {
        partiteGiocate++;
        partiteVinte++;
        puntiTotaliSegnati += puntiSegnati;
    }

    public void registraSconfitta(int puntiSegnati) {
        partiteGiocate++;
        partitePerse++;
        puntiTotaliSegnati += puntiSegnati;
    }

    public int getPartiteGiocate() { return partiteGiocate; }
    public int getPartiteVinte() { return partiteVinte; }
    public int getPartitePerse() { return partitePerse; }
    public int getPuntiTotaliSegnati() { return puntiTotaliSegnati; }

    /** Percentuale di vittorie (0-100). */
    public double getWinRate() {
        if (partiteGiocate == 0) return 0;
        return (double) partiteVinte / partiteGiocate * 100;
    }
}
