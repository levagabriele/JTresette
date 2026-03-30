package it.uniroma1.mdp.jtresette.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import it.uniroma1.mdp.jtresette.audio.AudioManager;
import it.uniroma1.mdp.jtresette.audio.SoundEffect;
import it.uniroma1.mdp.jtresette.model.ai.AIStrategy;
import it.uniroma1.mdp.jtresette.model.ai.EasyStrategy;
import it.uniroma1.mdp.jtresette.model.ai.MediumStrategy;
import it.uniroma1.mdp.jtresette.model.ai.HardStrategy;
import it.uniroma1.mdp.jtresette.model.profile.ProfileManager;
import it.uniroma1.mdp.jtresette.model.card.Carta;
import it.uniroma1.mdp.jtresette.model.card.RegoleTresette;
import it.uniroma1.mdp.jtresette.model.card.Seme;
import it.uniroma1.mdp.jtresette.model.game.Partita;
import it.uniroma1.mdp.jtresette.model.game.Turno;
import it.uniroma1.mdp.jtresette.model.player.Giocatore;
import it.uniroma1.mdp.jtresette.model.player.GiocatoreAI;
import it.uniroma1.mdp.jtresette.model.player.GiocatoreUmano;
import it.uniroma1.mdp.jtresette.util.Constants;
import it.uniroma1.mdp.jtresette.view.MainFrame;
import it.uniroma1.mdp.jtresette.view.screens.GameScreen;

/**
 * Controller principale del gioco. Gestisce il game loop,
 * collega il Model (Partita) con la View (GameScreen),
 * e coordina l'input dell'umano con i turni dell'AI.
 */
public class GameController {

    private final MainFrame mainFrame;
    private final int numGiocatori;
    private final int difficolta;
    private Partita partita;
    private GameScreen gameScreen;
    private GiocatoreUmano giocatoreUmano;
    private final List<Giocatore> giocatori = new ArrayList<>();
    private volatile boolean partitaInterrotta = false;

    public GameController(MainFrame mainFrame, int numGiocatori, int difficolta) {
        this.mainFrame = mainFrame;
        this.numGiocatori = numGiocatori;
        this.difficolta = difficolta;
    }

    /** Prepara e avvia una nuova partita. */
    public void avviaPartita() {
        // Crea giocatori
        giocatori.clear();
        String nomeUmano = ProfileManager.getInstance().getProfilo().getNickname();
        giocatoreUmano = new GiocatoreUmano(nomeUmano, 0);
        giocatori.add(giocatoreUmano);

        String[] nomiAI = {"Bot Cesare", "Bot Bruto", "Bot Augusto"};
        AIStrategy strategy = creaStrategy(difficolta);
        for (int i = 1; i < numGiocatori; i++) {
            giocatori.add(new GiocatoreAI(nomiAI[i - 1], i, strategy));
        }

        // Crea partita (Model)
        partita = new Partita(giocatori);

        // Crea view
        gameScreen = new GameScreen();
        String[] nomi = giocatori.stream().map(Giocatore::getNome).toArray(String[]::new);
        gameScreen.inizializza(nomi, numGiocatori, partita.getObiettivoTerzi());

        // Registra observer: View osserva il Model (MVC + Observer pattern)
        partita.addObserver(gameScreen);

        // Setup callback click carta
        gameScreen.getHandPanel().setOnCartaCliccata(carta -> {
            giocatoreUmano.scegliCarta(carta);
        });

        // Callback game over
        gameScreen.setOnGameOver(() -> {
            int vincitore = partita.getVincitore();
            boolean haVinto = vincitore == 0;
            mainFrame.getGameOverScreen().mostraRisultati(
                    giocatori.get(vincitore).getNome(), haVinto,
                    partita.getPunteggiTotali(), nomi);
            mainFrame.mostraSchermata(Constants.SCREEN_GAME_OVER);
        });

        // Callback abbandona partita -> torna al menu
        gameScreen.setOnAbbandona(() -> {
            partitaInterrotta = true;
            mainFrame.mostraSchermata(Constants.SCREEN_MENU);
        });

        // Callback ricomincia partita
        gameScreen.setOnRicomincia(() -> {
            partitaInterrotta = true;
            // Piccolo ritardo per far terminare il thread corrente
            Timer restartTimer = new Timer(200, evt -> {
                GameController nuova = new GameController(mainFrame, numGiocatori, difficolta);
                nuova.avviaPartita();
            });
            restartTimer.setRepeats(false);
            restartTimer.start();
        });

        // Mostra schermata
        mainFrame.setGameScreen(gameScreen);

        // Avvia game loop su thread separato
        Thread gameThread = new Thread(this::gameLoop, "GameLoop");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    private AIStrategy creaStrategy(int livello) {
        return switch (livello) {
            case 0 -> new EasyStrategy();
            case 1 -> new MediumStrategy();
            case 2 -> new HardStrategy();
            default -> new MediumStrategy();
        };
    }

    /**
     * Game loop principale. Eseguito su un thread separato.
     * Gestisce mani e turni finche' la partita non finisce.
     */
    private void gameLoop() {
        try {
            while (!partita.isFinita() && !partitaInterrotta) {
                giocaMano();
                if (partitaInterrotta) break;
                partita.chiudiMano();

                // Pausa tra le mani
                Thread.sleep(1500);
            }
            if (partitaInterrotta) return;
            partita.terminaPartita();

            // Registra risultato nel profilo
            int vincitore = partita.getVincitore();
            boolean haVinto = vincitore == 0;
            int puntiUmano = partita.getPunteggiTotali()[0];
            ProfileManager.getInstance().registraPartita(haVinto, puntiUmano);

            if (haVinto) {
                AudioManager.getInstance().play(SoundEffect.GAME_WIN);
            } else {
                AudioManager.getInstance().play(SoundEffect.GAME_LOSE);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Gioca una mano completa. */
    private void giocaMano() throws InterruptedException {
        partita.iniziaMano();
        AudioManager.getInstance().play(SoundEffect.CARD_DEAL);

        // Aggiorna UI con le carte distribuite
        aggiornaUI();
        Thread.sleep(800);

        int numTurni = (numGiocatori == 3) ? 12 : 10;
        for (int t = 0; t < numTurni && !partitaInterrotta; t++) {
            Turno turno = partita.iniziaTurno();

            // Pulisci tavolo per nuovo turno
            SwingUtilities.invokeLater(() -> gameScreen.pulisciTavolo());
            Thread.sleep(300);

            // Ogni giocatore gioca una carta
            for (int p = 0; p < numGiocatori; p++) {
                int indiceGiocatore = turno.getProssimoGiocatore();
                Giocatore giocatore = giocatori.get(indiceGiocatore);
                Seme semeGuida = turno.getSemeGuida();

                Carta cartaGiocata;
                if (giocatore.isUmano()) {
                    cartaGiocata = attendiMossaUmano(semeGuida);
                } else {
                    Thread.sleep(Constants.AI_THINK_DELAY_MS);
                    cartaGiocata = eseguiMossaAI((GiocatoreAI) giocatore, semeGuida);
                }

                partita.giocaCarta(indiceGiocatore, cartaGiocata);
                AudioManager.getInstance().play(SoundEffect.CARD_PLAY);
                aggiornaUI();
                Thread.sleep(400);
            }

            // Chiudi turno
            int vincitoreTurno = partita.chiudiTurno();
            if (vincitoreTurno == 0) {
                AudioManager.getInstance().play(SoundEffect.TRICK_WIN);
            }
            Thread.sleep(Constants.TRICK_COLLECT_DELAY_MS);
        }
    }

    /** Attende che il giocatore umano scelga una carta. */
    private Carta attendiMossaUmano(Seme semeGuida) throws InterruptedException {
        List<Carta> mano = giocatoreUmano.getMano();
        List<Carta> valide = RegoleTresette.mosseValide(mano, semeGuida);

        // Abilita input sulla EDT
        SwingUtilities.invokeLater(() -> {
            gameScreen.aggiornaManoUmano(mano);
            gameScreen.abilitaInput(valide);
        });

        // Attende la scelta
        CompletableFuture<Carta> future = giocatoreUmano.attediSceltaCarta();
        try {
            Carta scelta = future.get();
            SwingUtilities.invokeLater(() -> gameScreen.disabilitaInput());
            return scelta;
        } catch (Exception e) {
            throw new InterruptedException("Attesa carta interrotta");
        }
    }

    /** Esegue la mossa di un giocatore AI. */
    private Carta eseguiMossaAI(GiocatoreAI ai, Seme semeGuida) {
        List<Carta> carteNelTurno = partita.getManoCorrente().getTurnoCorrente() != null
                ? partita.getManoCorrente().getTurnoCorrente().getCarteSolamente()
                : List.of();
        return ai.scegliCarta(semeGuida, carteNelTurno, partita.getCarteGiocateStorico());
    }

    /** Aggiorna la UI con lo stato corrente. */
    private void aggiornaUI() {
        SwingUtilities.invokeLater(() -> {
            // Aggiorna mano umano
            gameScreen.aggiornaManoUmano(giocatoreUmano.getMano());

            // Aggiorna conteggio carte AI
            int[] numCarte = new int[numGiocatori];
            for (int i = 0; i < numGiocatori; i++) {
                numCarte[i] = giocatori.get(i).getNumCarte();
            }
            gameScreen.aggiornaCarteAI(numCarte);
        });
    }
}
