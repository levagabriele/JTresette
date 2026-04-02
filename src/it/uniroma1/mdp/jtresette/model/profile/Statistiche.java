package it.uniroma1.mdp.jtresette.model.profile;

/**
 * Statistiche di gioco di un giocatore.
 */
public class Statistiche {

    private static final int XP_PER_LIVELLO = 350;
    private static final int XP_VITTORIA = 100;
    private static final int XP_SCONFITTA = 30;

    private int partiteGiocate;
    private int partiteVinte;
    private int partitePerse;
    private int puntiTotaliSegnati;
    private int xpTotali;

    public Statistiche() {
        this(0, 0, 0, 0, 0);
    }

    public Statistiche(int partiteGiocate, int partiteVinte, int partitePerse, int puntiTotaliSegnati, int xpTotali) {
        this.partiteGiocate = partiteGiocate;
        this.partiteVinte = partiteVinte;
        this.partitePerse = partitePerse;
        this.puntiTotaliSegnati = puntiTotaliSegnati;
        this.xpTotali = xpTotali;
    }

    public int registraVittoria(int puntiSegnati) {
        partiteGiocate++;
        partiteVinte++;
        puntiTotaliSegnati += puntiSegnati;
        int xpGuadagnati = XP_VITTORIA;
        xpTotali += xpGuadagnati;
        return xpGuadagnati;
    }

    public int registraSconfitta(int puntiSegnati) {
        partiteGiocate++;
        partitePerse++;
        puntiTotaliSegnati += puntiSegnati;
        int xpGuadagnati = XP_SCONFITTA;
        xpTotali += xpGuadagnati;
        return xpGuadagnati;
    }

    public int getPartiteGiocate() { return partiteGiocate; }
    public int getPartiteVinte() { return partiteVinte; }
    public int getPartitePerse() { return partitePerse; }
    public int getPuntiTotaliSegnati() { return puntiTotaliSegnati; }
    public int getXpTotali() { return xpTotali; }

    /** Livello corrente (1-based). */
    public int getLivello() {
        return xpTotali / XP_PER_LIVELLO + 1;
    }

    /** XP accumulati nel livello corrente. */
    public int getXpNelLivello() {
        return xpTotali % XP_PER_LIVELLO;
    }

    /** XP necessari per completare il livello corrente. */
    public int getXpPerLivello() {
        return XP_PER_LIVELLO;
    }

    /** Percentuale di vittorie (0-100). */
    public double getWinRate() {
        if (partiteGiocate == 0) return 0;
        return (double) partiteVinte / partiteGiocate * 100;
    }
}
