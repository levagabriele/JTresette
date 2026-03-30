package it.uniroma1.mdp.jtresette.model.player;

import java.util.concurrent.CompletableFuture;

import it.uniroma1.mdp.jtresette.model.card.Carta;

/**
 * Giocatore umano. La scelta della carta avviene tramite input dalla GUI,
 * comunicata al controller che risolve il CompletableFuture.
 */
public class GiocatoreUmano extends Giocatore {

    private CompletableFuture<Carta> cartaScelta;

    public GiocatoreUmano(String nome, int indice) {
        super(nome, indice);
    }

    /**
     * Prepara l'attesa per la scelta della carta.
     * Il controller chiamera' questo metodo prima del turno dell'umano.
     *
     * @return un CompletableFuture che verra' completato quando l'umano sceglie una carta
     */
    public CompletableFuture<Carta> attediSceltaCarta() {
        cartaScelta = new CompletableFuture<>();
        return cartaScelta;
    }

    /**
     * Chiamato dal controller/InputHandler quando l'umano clicca una carta.
     *
     * @param carta la carta scelta
     */
    public void scegliCarta(Carta carta) {
        if (cartaScelta != null && !cartaScelta.isDone()) {
            cartaScelta.complete(carta);
        }
    }

    @Override
    public boolean isUmano() {
        return true;
    }
}
