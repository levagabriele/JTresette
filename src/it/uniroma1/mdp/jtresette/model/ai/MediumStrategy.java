package it.uniroma1.mdp.jtresette.model.ai;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.model.card.Seme;

/**
 * Strategia AI media con euristiche di base:
 * - Se guida: gioca la carta piu' bassa dal seme piu' lungo
 * - Se segue e puo' vincere: gioca la carta minima che vince
 * - Se non puo' vincere: scarica la carta piu' bassa
 * - In fine mano: gioca aggressivamente per l'ultimo turno
 */
public class MediumStrategy implements AIStrategy {

    @Override
    public Carta scegliCarta(List<Carta> mano, Seme semeGuida,
                             List<Carta> carteGiocateNelTurno,
                             List<Carta> carteGiocateNellaPartita) {
        List<Carta> valide = RegoleTresette.mosseValide(mano, semeGuida);

        if (valide.size() == 1) return valide.get(0);

        // Se apriamo il turno (primo a giocare)
        if (semeGuida == null) {
            return scegliCartaApertura(mano, valide);
        }

        // Se seguiamo
        return scegliCartaRisposta(valide, semeGuida, carteGiocateNelTurno);
    }

    /** Sceglie la carta per aprire il turno. */
    private Carta scegliCartaApertura(List<Carta> mano, List<Carta> valide) {
        // Raggruppa per seme, gioca dal seme piu' lungo la carta piu' debole
        Map<Seme, List<Carta>> perSeme = valide.stream()
                .collect(Collectors.groupingBy(Carta::getSeme));

        // Trova il seme piu' lungo
        Seme semeLungo = perSeme.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()))
                .map(Map.Entry::getKey)
                .orElse(valide.get(0).getSeme());

        // Dal seme piu' lungo, gioca la carta con meno punti e forza bassa
        return perSeme.get(semeLungo).stream()
                .min(Comparator.comparingInt(Carta::getPuntiTerzi)
                        .thenComparingInt(Carta::getForzaDiPresa))
                .orElse(valide.get(0));
    }

    /** Sceglie la carta per rispondere. */
    private Carta scegliCartaRisposta(List<Carta> valide, Seme semeGuida,
                                      List<Carta> carteNelTurno) {
        // Trova la forza massima giocata nel seme guida
        int forzaMax = carteNelTurno.stream()
                .filter(c -> c.getSeme() == semeGuida)
                .mapToInt(Carta::getForzaDiPresa)
                .max().orElse(0);

        // Carte che possono vincere (dello stesso seme guida con forza maggiore)
        List<Carta> vincenti = valide.stream()
                .filter(c -> c.getSeme() == semeGuida && c.getForzaDiPresa() > forzaMax)
                .sorted(Comparator.comparingInt(Carta::getForzaDiPresa))
                .collect(Collectors.toList());

        if (!vincenti.isEmpty()) {
            // Gioca la carta minima che vince
            return vincenti.get(0);
        }

        // Non possiamo vincere: scarica la carta con meno punti
        return valide.stream()
                .min(Comparator.comparingInt(Carta::getPuntiTerzi)
                        .thenComparingInt(Carta::getForzaDiPresa))
                .orElse(valide.get(0));
    }

    @Override
    public String getNomeDifficolta() {
        return "Medio";
    }
}
