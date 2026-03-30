package it.uniroma1.mdp.jtresette.model.card;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.uniroma1.mdp.jtresette.model.game.Dichiarazione;

/**
 * Classe di utilita' con le regole del Tresette.
 * Contiene metodi statici per determinare mosse valide, vincitore del turno,
 * calcolo punti e rilevamento dichiarazioni.
 * Uso intensivo di Stream&lt;T&gt;.
 */
public final class RegoleTresette {

    /** Punti totali in terzi per mano (incluso ultimo trick). */
    public static final int PUNTI_TOTALI_MANO_TERZI = 34;

    /** Obiettivo di default: 21 punti = 63 terzi. */
    public static final int OBIETTIVO_DEFAULT_TERZI = 63;

    private RegoleTresette() {}

    /**
     * Restituisce le mosse valide data la mano del giocatore e il seme guida.
     * Se il seme guida e' null (il giocatore e' il primo a giocare), tutte le carte sono valide.
     * Altrimenti, deve rispondere al seme se possibile.
     *
     * @param mano le carte in mano al giocatore
     * @param semeGuida il seme della prima carta giocata nel turno (null se primo a giocare)
     * @return lista di carte giocabili
     */
    public static List<Carta> mosseValide(List<Carta> mano, Seme semeGuida) {
        if (semeGuida == null) {
            return new ArrayList<>(mano);
        }
        // Filtra le carte dello stesso seme guida
        List<Carta> stessoSeme = mano.stream()
                .filter(c -> c.getSeme() == semeGuida)
                .collect(Collectors.toList());
        // Se ha carte del seme guida, deve giocarle; altrimenti puo' giocare qualsiasi
        return stessoSeme.isEmpty() ? new ArrayList<>(mano) : stessoSeme;
    }

    /**
     * Determina il vincitore di un turno.
     * Vince chi ha giocato la carta piu' forte del seme guida.
     * Le carte di seme diverso dal seme guida non possono vincere.
     *
     * @param carteGiocate lista ordinata di (indice giocatore, carta)
     * @param semeGuida il seme della prima carta giocata
     * @return l'indice del giocatore vincitore
     */
    public static int vincitoreTurno(List<Map.Entry<Integer, Carta>> carteGiocate, Seme semeGuida) {
        return carteGiocate.stream()
                .filter(e -> e.getValue().getSeme() == semeGuida)
                .max(Comparator.comparingInt(e -> e.getValue().getForzaDiPresa()))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("Nessuna carta del seme guida"));
    }

    /**
     * Calcola i punti (in terzi) delle carte prese.
     *
     * @param cartePrese le carte vinte
     * @return punti in terzi
     */
    public static int calcolaPunti(List<Carta> cartePrese) {
        return cartePrese.stream()
                .mapToInt(Carta::getPuntiTerzi)
                .sum();
    }

    /**
     * Formatta i punti in terzi come stringa leggibile.
     * Es: 10 terzi = "3 + 1/3", 3 terzi = "1", 1 terzo = "1/3"
     *
     * @param puntiTerzi punti in terzi
     * @return stringa formattata
     */
    public static String formattaPunti(int puntiTerzi) {
        int interi = puntiTerzi / 3;
        int resto = puntiTerzi % 3;
        if (resto == 0) return String.valueOf(interi);
        if (interi == 0) return resto + "/3";
        return interi + " + " + resto + "/3";
    }

    /**
     * Rileva le dichiarazioni possibili data una mano di carte.
     *
     * Dichiarazioni:
     * - NAPOLITANA: Asso, Due, Tre dello stesso seme (vale 3 punti = 9 terzi)
     * - TRESETTE: tre carte di valore Tre (vale 4 punti = 12 terzi) -- nota: non standard in tutte le varianti
     *
     * @param mano le carte in mano
     * @return lista di dichiarazioni possibili con le carte coinvolte
     */
    public static List<Map.Entry<Dichiarazione, List<Carta>>> verificaDichiarazioni(List<Carta> mano) {
        List<Map.Entry<Dichiarazione, List<Carta>>> dichiarazioni = new ArrayList<>();

        // Raggruppa per seme usando Stream
        Map<Seme, List<Carta>> perSeme = mano.stream()
                .collect(Collectors.groupingBy(Carta::getSeme));

        // Verifica Napolitana: Asso + Due + Tre dello stesso seme
        perSeme.forEach((seme, carteSeme) -> {
            boolean hasAsso = carteSeme.stream().anyMatch(c -> c.getValore() == Valore.ASSO);
            boolean hasDue = carteSeme.stream().anyMatch(c -> c.getValore() == Valore.DUE);
            boolean hasTre = carteSeme.stream().anyMatch(c -> c.getValore() == Valore.TRE);

            if (hasAsso && hasDue && hasTre) {
                List<Carta> carteNapolitana = carteSeme.stream()
                        .filter(c -> c.getValore() == Valore.ASSO ||
                                     c.getValore() == Valore.DUE ||
                                     c.getValore() == Valore.TRE)
                        .collect(Collectors.toList());
                dichiarazioni.add(new AbstractMap.SimpleEntry<>(Dichiarazione.NAPOLITANA, carteNapolitana));
            }
        });

        // Verifica Tresette: tre Tre
        List<Carta> treCareTre = mano.stream()
                .filter(c -> c.getValore() == Valore.TRE)
                .collect(Collectors.toList());
        if (treCareTre.size() >= 3) {
            dichiarazioni.add(new AbstractMap.SimpleEntry<>(Dichiarazione.TRESETTE, treCareTre));
        }

        return dichiarazioni;
    }

    /**
     * Verifica se una carta e' "master" (la piu' forte rimasta del suo seme).
     *
     * @param carta la carta da verificare
     * @param carteGiaGiocate le carte gia' uscite nella partita
     * @return true se nessuna carta piu' forte dello stesso seme e' ancora in gioco
     */
    public static boolean isMaster(Carta carta, List<Carta> carteGiaGiocate) {
        return java.util.Arrays.stream(Valore.values())
                .filter(v -> v.getForzaDiPresa() > carta.getForzaDiPresa())
                .map(v -> new Carta(carta.getSeme(), v))
                .allMatch(carteGiaGiocate::contains);
    }
}
