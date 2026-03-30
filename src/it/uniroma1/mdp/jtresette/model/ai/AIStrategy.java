package it.uniroma1.mdp.jtresette.model.ai;

import java.util.List;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.Seme;

/**
 * Interfaccia Strategy per l'intelligenza artificiale del Tresette.
 * Ogni livello di difficolta' implementa questa interfaccia con una
 * strategia diversa.
 */
public interface AIStrategy {

    /**
     * Sceglie una carta da giocare.
     *
     * @param mano le carte in mano al giocatore AI
     * @param semeGuida il seme della prima carta giocata nel turno (null se il giocatore apre)
     * @param carteGiocateNelTurno le carte gia' giocate in questo turno
     * @param carteGiocateNellaPartita tutte le carte gia' uscite nella partita
     * @return la carta scelta
     */
    Carta scegliCarta(List<Carta> mano, Seme semeGuida,
                      List<Carta> carteGiocateNelTurno,
                      List<Carta> carteGiocateNellaPartita);

    /** Nome della difficolta'. */
    String getNomeDifficolta();
}
