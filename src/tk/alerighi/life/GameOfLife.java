package tk.alerighi.life;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe che implementa il famoso "Game of Life" di John Conway
 *
 * @author Alessandro Righi
 */
@XmlRootElement
public class GameOfLife {

    private boolean[][] field;
    private int width;
    private int height;
    private boolean alive = false;

    /**
     * Crea un nuovo gioco game of life
     *
     * @param height altezza del campo
     * @param width  larghezza del campo
     */
    public GameOfLife(int height, int width) {
        this.width = width;
        this.height = height;
        this.setField(new boolean[height][width]);
    }

    public GameOfLife() {
        this(10, 10);
    }

    /**
     * Ritorna vero se il gioco è vivo, ossia c'è ancora almeno una casella viva
     *
     * @return true se il gioco è vivo
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Metodo che setta il valore di una cella del campo
     *
     * @param y   coordinata y
     * @param x   coordinata x
     * @param val valore da settare
     */
    public void setYX(int y, int x, boolean val) {
        if (y < 0 || x < 0 || y >= height || x >= width) {
            /* TODO: eccezione */
            return;
        }
        field[y][x] = val;
        alive = true;
    }

    /**
     * Metodo che ritorna il valore corrispondente alla cella indicata
     *
     * @param y coordinata y
     * @param x coordinata x
     * @return valore contenuto nella cella indicata
     */
    public boolean getXY(int y, int x) {
        if (y < 0 || x < 0 || y >= height || x >= width) {
            /* TODO: eccezione */
            return false;
        }
        return field[y][x];
    }

    /**
     * Metodo che ritorna l'altezza del campo
     *
     * @return altezza del campo
     */
    public int getHeight() {
        return height;
    }

    /**
     * Metodo che cambia l'altezza del campo. Provoca il reset del gioco.
     *
     * @param height la nuova altezza
     */
    @XmlAttribute
    public void setHeight(int height) {
        this.height = height;
        reset();
    }

    /**
     * Metodo che ritorna la larghezza del campo
     *
     * @return larghezza del campo
     */
    public int getWidth() {
        return width;
    }

    /**
     * Metodo che cambia la larghezza del campo. Provoca il reset del gioco.
     *
     * @param width la nuova larghezza
     */
    @XmlAttribute
    public void setWidth(int width) {
        this.width = width;
        reset();
    }

    /**
     * Resetta il gioco, reinizzializzando il campo a 0
     */
    public void reset() {
        setField(new boolean[height][width]);
    }

    /**
     * Metodo che conta il numero di celle vicine alla cella data
     *
     * @param y coordinata y della cella
     * @param x coordinata x della cella
     * @return numero di celle adiacenti a quella indicata
     */
    private int countNearCells(int y, int x) {
        int count = 0;
        if (y > 0 && x > 0 && field[y - 1][x - 1]) count++;
        if (y > 0 && field[y - 1][x]) count++;
        if (y > 0 && x + 1 < width && field[y - 1][x + 1]) count++;
        if (x > 0 && field[y][x - 1]) count++;
        if (x + 1 < width && field[y][x + 1]) count++;
        if (y + 1 < height && x > 0 && field[y + 1][x - 1]) count++;
        if (y + 1 < height && field[y + 1][x]) count++;
        if (y + 1 < height && x + 1 < width && field[y + 1][x + 1]) count++;
        return count;
    }

    /**
     * Metodo che evolve il gioco
     * Il gioco si evolve nel seguente modo:
     * a) se una cella vuota è affiancata da esattamente 3 celle popolate, avviene una nascita in quella cella
     * b) se una cella popolata è circondata da 4 o più celle popolate, muore per sovraffollamento
     * c) se una cella popolata è circondata da meno di 2 celle popolate, muore per isolamento
     * d) se una cella popolata è circondata da 2 o 3 celle popolate, sopravvive
     */
    public void evolve() {
        boolean[][] tmp = new boolean[height][width];
        alive = false;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int near = countNearCells(y, x);

                if ((field[y][x] && (near == 2 || near == 3)) || (!field[y][x] && near == 3)) {
                    tmp[y][x] = true;
                    alive = true;
                } else {
                    tmp[y][x] = false;
                }

            }
        }

        setField(tmp);

    }

    /**
     * Ritorna un array del campo da gioco
     *
     * @return array del campo da gioco
     */
    @XmlElement
    public boolean[][] getField() {
        return field;
    }

    /**
     * Setta l'array del campo da gioco
     *
     * @param field array del campo da gioco
     */
    public void setField(boolean[][] field) {
        this.field = field;
    }
}
