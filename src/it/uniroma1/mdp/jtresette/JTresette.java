package it.uniroma1.mdp.jtresette;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import it.uniroma1.mdp.jtresette.controller.MenuController;
import it.uniroma1.mdp.jtresette.view.MainFrame;

/**
 * Classe principale dell'applicazione JTresette.
 * Contiene il main del gioco come richiesto dalle specifiche.
 */
public class JTresette {

    public static void main(String[] args) {
        // Usa cross-platform L&F per evitare problemi con bottoni custom su Windows
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Usa il default se non disponibile
        }

        // Avvia la GUI sull'EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            new MenuController(mainFrame);
            mainFrame.setVisible(true);
        });
    }
}
