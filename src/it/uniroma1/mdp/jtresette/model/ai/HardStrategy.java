package it.uniroma1.mdp.jtresette.model.ai;

import java.util.*;
import java.util.stream.Collectors;

import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.model.card.Seme;
import it.uniroma1.mdp.jtresette.model.card.Valore;

/**
 * Strategia AI difficile con card counting e inferenza.
 * - Tiene traccia di tutte le carte giocate
 * - Identifica le carte "master" (piu' forti rimaste in gioco)
 * - Inferisce i semi vuoti degli avversari
 * - Gestisce strategicamente le carte di alto valore
 */
public class HardStrategy implements AIStrategy {

    @Override
    public Carta scegliCarta(List<Carta> mano, Seme semeGuida,
                             List<Carta> carteGiocateNelTurno,
                             List<Carta> carteGiocateNellaPartita) {
        List<Carta> valide = RegoleTresette.mosseValide(mano, semeGuida);

        if (valide.size() == 1) return valide.get(0);

        if (semeGuida == null) {
            return scegliCartaApertura(mano, valide, carteGiocateNellaPartita);
        }

        return scegliCartaRisposta(valide, semeGuida, carteGiocateNelTurno, carteGiocateNellaPartita);
    }

    private Carta scegliCartaApertura(List<Carta> mano, List<Carta> valide,
                                       List<Carta> storico) {
        // Cerca carte master (piu' forte rimasta del seme)
        Optional<Carta> master = valide.stream()
                .filter(c -> RegoleTresette.isMaster(c, storico))
                .max(Comparator.comparingInt(Carta::getPuntiTerzi));

        // Se abbiamo una carta master che vale punti, giochiamola
        if (master.isPresent() && master.get().getPuntiTerzi() > 0) {
            return master.get();
        }

        // Se abbiamo una master qualsiasi, giochiamola per prendere il turno
        if (master.isPresent()) {
            return master.get();
        }

        // Altrimenti gioca dal seme con piu' carte rimaste in gioco
        // (maggiore probabilita' che gli avversari debbano rispondere)
        Map<Seme, Long> carteRimanentiPerSeme = Arrays.stream(Seme.values())
                .collect(Collectors.toMap(
                        s -> s,
                        s -> contaCarteRimanenti(s, mano, storico)));

        // Preferisci il seme dove abbiamo piu' carte e ci sono ancora carte in gioco
        Map<Seme, List<Carta>> manoPerSeme = valide.stream()
                .collect(Collectors.groupingBy(Carta::getSeme));

        return manoPerSeme.entrySet().stream()
                .max(Comparator.<Map.Entry<Seme, List<Carta>>>comparingInt(e -> e.getValue().size())
                        .thenComparingLong(e -> carteRimanentiPerSeme.getOrDefault(e.getKey(), 0L)))
                .map(e -> e.getValue().stream()
                        .min(Comparator.comparingInt(Carta::getPuntiTerzi)
                                .thenComparingInt(Carta::getForzaDiPresa))
                        .orElse(valide.get(0)))
                .orElse(valide.get(0));
    }

    private Carta scegliCartaRisposta(List<Carta> valide, Seme semeGuida,
                                       List<Carta> carteNelTurno,
                                       List<Carta> storico) {
        int forzaMax = carteNelTurno.stream()
                .filter(c -> c.getSeme() == semeGuida)
                .mapToInt(Carta::getForzaDiPresa)
                .max().orElse(0);

        // Punti in gioco nel turno
        int puntiInGioco = carteNelTurno.stream()
                .mapToInt(Carta::getPuntiTerzi)
                .sum();

        // Carte che possono vincere
        List<Carta> vincenti = valide.stream()
                .filter(c -> c.getSeme() == semeGuida && c.getForzaDiPresa() > forzaMax)
                .sorted(Comparator.comparingInt(Carta::getForzaDiPresa))
                .collect(Collectors.toList());

        if (!vincenti.isEmpty()) {
            // Se ci sono punti in gioco, vale la pena vincere
            if (puntiInGioco > 0) {
                return vincenti.get(0); // minima carta vincente
            }
            // Se non ci sono punti, valuta se sprecare una carta alta
            // Gioca la vincente piu' bassa solo se non e' una carta preziosa
            Carta minimaVincente = vincenti.get(0);
            if (minimaVincente.getPuntiTerzi() <= 1) {
                return minimaVincente;
            }
            // Altrimenti scarica
        }

        // Non possiamo vincere o non conviene: scarica la carta peggiore
        // Preferisci scarti da semi dove non abbiamo carte forti
        return valide.stream()
                .min(Comparator.comparingInt(Carta::getPuntiTerzi)
                        .thenComparingInt(Carta::getForzaDiPresa))
                .orElse(valide.get(0));
    }

    /** Conta le carte di un seme ancora in gioco (non nella mano e non nello storico). */
    private long contaCarteRimanenti(Seme seme, List<Carta> mano, List<Carta> storico) {
        return Arrays.stream(Valore.values())
                .map(v -> new Carta(seme, v))
                .filter(c -> !mano.contains(c) && !storico.contains(c))
                .count();
    }

    @Override
    public String getNomeDifficolta() {
        return "Difficile";
    }
}
