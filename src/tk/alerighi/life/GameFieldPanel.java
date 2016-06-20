package tk.alerighi.life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe che estende un JPanel per la visualizzazione del campo da gioco
 *
 * @author Alessandro Righi
 */
public class GameFieldPanel extends JPanel {

    private GameOfLife game;
    private int cellSize = 17;

    /**
     * Costruttore di un nuovo oggetto GameFieldPanel che mostra il gioco in un panel
     */
    public GameFieldPanel() {
        this.setGame(new GameOfLife(30, 60));
        addMouseListener(new MyMouseListener());
    }

    /**
     * Ritorna il gioco correntemente visualizzato
     *
     * @return il gioco corrente
     */
    public GameOfLife getGame() {
        return game;
    }

    /**
     * Setta un nuovo gioco
     *
     * @param game il nuovo gioco da settare
     */
    public void setGame(GameOfLife game) {
        this.game = game;
        recomputeSize();
    }

    public void recomputeSize() {
        int width = game.getWidth();
        int height = game.getHeight();
        setPreferredSize(new Dimension(width * cellSize, height * cellSize));
        repaint();
    }

    /**
     * Ritorna la dimensione in pixel di una cella del campo
     *
     * @return dimensione di una cella
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Setta la dimensione di una cella del campo
     *
     * @param cellSize dimensione di una cella del campo
     */
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
        recomputeSize();
    }

    /**
     * Aumenta di 2 la dimensione din una cella, e ridisegna il campo.
     */
    public void zoomIn() {
        if (cellSize < 30) {
            cellSize++;
        }
        recomputeSize();
    }

    /**
     * Diminuisce di 2 la dimensione di una cella, e ridisegna il campo.
     */
    public void zoomOut() {
        if (cellSize > 5) {
            cellSize--;
        }
        recomputeSize();
    }

    /**
     * Esegue un evoluzione del game of life, se così si può chiamare.
     */
    public void evolveGame() {
        game.evolve();
        repaint();
    }

    public void resetGame() {
        game.reset();
        repaint();
    }

    /**
     * Metodo che disegna il contenuto del frame
     *
     * @param g area grafica per il disegno
     */
    @Override
    public void paint(Graphics g) {
        int width = getGame().getWidth();
        int height = getGame().getHeight();

        for (int i = 0; i <= height; i++) {
            g.drawLine(0, i * cellSize, width * cellSize, i * cellSize);
        }

        for (int i = 0; i <= width; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, height * cellSize);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (getGame().getXY(i, j)) {
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    /**
     * Listener per prendere il click del mouse
     */
    private class MyMouseListener extends MouseAdapter {

        /**
         * Metodo che viene chiamato ad ogni click del mouse. Inverte il contenuto
         * di una cella, ossia se era attiva la disattiva, e viceversa.
         *
         * @param e evento del mouse
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() / cellSize;
            int y = e.getY() / cellSize;
            getGame().setYX(y, x, !getGame().getXY(y, x));
            repaint();
        }
    }

}
