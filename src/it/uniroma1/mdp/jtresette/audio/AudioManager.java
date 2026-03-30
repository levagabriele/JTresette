package it.uniroma1.mdp.jtresette.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Singleton per la gestione dell'audio nel gioco.
 * Utilizza javax.sound.sampled (compatibile JDK > 9).
 * Precarica tutti gli effetti sonori per ridurre la latenza.
 */
public class AudioManager {

    private static AudioManager instance;

    private final Map<SoundEffect, Clip> clips = new EnumMap<>(SoundEffect.class);
    private boolean muted = false;
    private float volume = 0.8f;

    /** Costruttore privato (Singleton pattern). */
    private AudioManager() {
        preloadAll();
    }

    /** Restituisce l'istanza unica di AudioManager. */
    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    /**
     * Precarica tutti gli effetti sonori usando Stream.
     */
    private void preloadAll() {
        Arrays.stream(SoundEffect.values()).forEach(this::loadClip);
    }

    private void loadClip(SoundEffect effect) {
        try {
            File file = new File(effect.getPath());
            if (!file.exists()) {
                System.out.println("Audio non trovato: " + effect.getPath() + " (verra' ignorato)");
                return;
            }
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clips.put(effect, clip);
        } catch (Exception e) {
            System.err.println("Errore caricamento audio " + effect.name() + ": " + e.getMessage());
        }
    }

    /**
     * Riproduce un effetto sonoro.
     *
     * @param effect l'effetto da riprodurre
     */
    public void play(SoundEffect effect) {
        if (muted) return;
        Clip clip = clips.get(effect);
        if (clip != null) {
            clip.setFramePosition(0);
            setClipVolume(clip, volume);
            clip.start();
        }
    }

    /**
     * Riproduce un file audio dato il suo path.
     * Metodo di compatibilita' con il formato richiesto dalle specifiche.
     *
     * @param filename path del file audio
     */
    public void play(String filename) {
        if (muted) return;
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filename));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("Errore riproduzione: " + filename);
        }
    }

    private void setClipVolume(Clip clip, float vol) {
        try {
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = control.getMaximum() - control.getMinimum();
            float gain = control.getMinimum() + (range * vol);
            control.setValue(gain);
        } catch (Exception e) {
            // Non tutti i sistemi supportano il controllo del volume
        }
    }

    /** Imposta il volume (0.0 - 1.0). */
    public void setVolume(float volume) {
        this.volume = Math.max(0f, Math.min(1f, volume));
    }

    public float getVolume() {
        return volume;
    }

    /** Attiva/disattiva il mute. */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }
}
