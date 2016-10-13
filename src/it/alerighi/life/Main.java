package it.alerighi.life;

import javax.swing.*;

/**
 * Classe main per il GameOfLife
 * <p>
 * Perché creare una classe Main separata ? Perché se si mette il metodo main nella classe
 * della finestra principale per un non so quale strano bug con la JVM su Apple il titolo
 * dell'applicazione che poi è quello che viene mostrato nella barra in alto affianco alla
 * mela e nel dock non viene settato.
 */
public class Main {

    /**
     * Metodo main dell'appllicazione
     *
     * @param args argomenti da riga di comando
     */
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Game Of Life");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new MainWindow();
    }
}
