package it.uniroma1.mdp.jtresette.model.profile;

/**
 * Profilo del giocatore con nickname, avatar, statistiche e livello.
 */
public class ProfiloGiocatore {

    private String nickname;
    private String avatarPath;
    private Statistiche statistiche;

    public ProfiloGiocatore() {
        this("Giocatore", "avatar_01", new Statistiche());
    }

    public ProfiloGiocatore(String nickname, String avatarPath, Statistiche statistiche) {
        this.nickname = nickname;
        this.avatarPath = avatarPath;
        this.statistiche = statistiche;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }

    public Statistiche getStatistiche() { return statistiche; }

    /** Livello calcolato in base agli XP. */
    public int getLivello() {
        return statistiche.getLivello();
    }

    @Override
    public String toString() {
        return nickname + " (Lv." + getLivello() + ")";
    }
}
