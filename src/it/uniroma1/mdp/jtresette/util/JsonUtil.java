package it.uniroma1.mdp.jtresette.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import it.uniroma1.mdp.jtresette.model.profile.ProfiloGiocatore;
import it.uniroma1.mdp.jtresette.model.profile.Statistiche;

/**
 * Utilita' per serializzazione/deserializzazione JSON del profilo.
 * Implementazione senza librerie esterne.
 */
public final class JsonUtil {

    private JsonUtil() {}

    /**
     * Serializza un profilo in formato JSON.
     */
    public static String toJson(ProfiloGiocatore profilo) {
        Statistiche s = profilo.getStatistiche();
        return String.format("""
                {
                  "nickname": "%s",
                  "avatarPath": "%s",
                  "statistiche": {
                    "partiteGiocate": %d,
                    "partiteVinte": %d,
                    "partitePerse": %d,
                    "puntiTotaliSegnati": %d
                  },
                  "livello": %d
                }""",
                escapeJson(profilo.getNickname()),
                escapeJson(profilo.getAvatarPath()),
                s.getPartiteGiocate(),
                s.getPartiteVinte(),
                s.getPartitePerse(),
                s.getPuntiTotaliSegnati(),
                profilo.getLivello());
    }

    /**
     * Deserializza un profilo da JSON.
     */
    public static ProfiloGiocatore fromJson(String json) {
        String nickname = estraiStringa(json, "nickname");
        String avatarPath = estraiStringa(json, "avatarPath");
        int giocate = estraiIntero(json, "partiteGiocate");
        int vinte = estraiIntero(json, "partiteVinte");
        int perse = estraiIntero(json, "partitePerse");
        int punti = estraiIntero(json, "puntiTotaliSegnati");

        return new ProfiloGiocatore(nickname, avatarPath,
                new Statistiche(giocate, vinte, perse, punti));
    }

    /** Salva JSON su file. */
    public static void salvaFile(String json, String path) throws IOException {
        Path filePath = Path.of(path);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, json);
    }

    /** Legge JSON da file. */
    public static String leggiFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String estraiStringa(String json, String chiave) {
        String pattern = "\"" + chiave + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return "";
        idx = json.indexOf("\"", idx + pattern.length() + 1);
        if (idx < 0) return "";
        int fine = json.indexOf("\"", idx + 1);
        if (fine < 0) return "";
        return json.substring(idx + 1, fine);
    }

    private static int estraiIntero(String json, String chiave) {
        String pattern = "\"" + chiave + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return 0;
        idx = json.indexOf(":", idx + pattern.length());
        if (idx < 0) return 0;
        StringBuilder sb = new StringBuilder();
        for (int i = idx + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (Character.isDigit(c) || c == '-') {
                sb.append(c);
            } else if (!sb.isEmpty()) {
                break;
            }
        }
        try {
            return Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
