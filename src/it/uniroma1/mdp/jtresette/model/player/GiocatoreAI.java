package it.uniroma1.mdp.jtresette.model.player;

import java.util.List;

import it.uniroma1.mdp.jtresette.model.ai.AIStrategy;
import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.Seme;

/**
 * Giocatore controllato dall'intelligenza artificiale.
 * Delega la scelta della carta alla strategia (Strategy pattern).
 */
public class GiocatoreAI extends Giocatore {

    private final AIStrategy strategy;

    public GiocatoreAI(String nome, int indice, AIStrategy strategy) {
        super(nome, indice);
        this.strategy = strategy;
    }

    /**
     * Sceglie una carta usando la strategia AI.
     *
     * @param semeGuida il seme guida del turno corrente (null se apre)
     * @param carteGiocateNelTurno carte gia' giocate nel turno
     * @param carteGiocateNellaPartita tutte le carte gia' uscite
     * @return la carta scelta
     */
    public Carta scegliCarta(Seme semeGuida, List<Carta> carteGiocateNelTurno,
                             List<Carta> carteGiocateNellaPartita) {
        return strategy.scegliCarta(getMano(), semeGuida, carteGiocateNelTurno, carteGiocateNellaPartita);
    }

    public AIStrategy getStrategy() {
        return strategy;
    }

    @Override
    public boolean isUmano() {
        return false;
    }
}
