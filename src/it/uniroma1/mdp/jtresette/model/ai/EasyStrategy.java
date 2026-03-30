package it.uniroma1.mdp.jtresette.model.ai;

import java.util.List;
import java.util.Random;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.model.card.Seme;

/**
 * Strategia AI facile: sceglie una carta casuale tra le mosse valide.
 */
public class EasyStrategy implements AIStrategy {

    private final Random random = new Random();

    @Override
    public Carta scegliCarta(List<Carta> mano, Seme semeGuida,
                             List<Carta> carteGiocateNelTurno,
                             List<Carta> carteGiocateNellaPartita) {
        List<Carta> valide = RegoleTresette.mosseValide(mano, semeGuida);
        return valide.get(random.nextInt(valide.size()));
    }

    @Override
    public String getNomeDifficolta() {
        return "Facile";
    }
}
