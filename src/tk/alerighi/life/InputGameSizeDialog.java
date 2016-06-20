package tk.alerighi.life;

import javax.swing.*;
import java.awt.*;

/**
 * Classe che crea un JDialog per le impostazioni del gioco.
 *
 * @author Alessandro Righi
 */
public class InputGameSizeDialog extends JDialog {

    private GameOfLife game;
    private GameFieldPanel fieldPanel;
    private JSpinner inputHeight;
    private JSpinner inputWidth;
    private JSpinner inputCellSize;

    /**
     * Costruttore che costruisce un nuovo dialogo per il cambio delle dimensini del campo
     *
     * @param fieldPanel il pannello del gioco da modificare
     */
    public InputGameSizeDialog(GameFieldPanel fieldPanel) {
        this.fieldPanel = fieldPanel;
        game = fieldPanel.getGame();

        setSize(200, 200);
        setTitle("Change Field size");
        setModal(true);
        setLayout(new GridLayout(4, 2));
        setLocationRelativeTo(getParent());

        JLabel labelHeight = new JLabel(" Height");
        JLabel labelWidth = new JLabel(" Width");
        JLabel labelCellSize = new JLabel(" Cell size");

        inputHeight = new JSpinner(new SpinnerNumberModel(game.getHeight(), 10, 1000, 1));
        inputWidth = new JSpinner(new SpinnerNumberModel(game.getWidth(), 10, 1000, 1));
        inputCellSize = new JSpinner(new SpinnerNumberModel(fieldPanel.getCellSize(), 5, 100, 1));

        JButton buttonOk = new JButton("OK");
        buttonOk.addActionListener(e -> onButtonOkClick());
        getRootPane().setDefaultButton(buttonOk);

        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(e -> onButtonCancelClick());

        getContentPane().add(labelWidth);
        getContentPane().add(inputWidth);
        getContentPane().add(labelHeight);
        getContentPane().add(inputHeight);
        getContentPane().add(labelCellSize);
        getContentPane().add(inputCellSize);
        getContentPane().add(buttonOk);
        getContentPane().add(buttonCancel);

        pack();
        setVisible(true);
    }

    /**
     * Metodo che viene richiamato alla pressione del pulsante Ok.
     * Nel caso siano cambiati, aggiorna i parametri del gioco e del
     * campo, e chiude il dialogo.
     */
    private void onButtonOkClick() {
        int height = (int) inputHeight.getValue();
        int width = (int) inputWidth.getValue();
        int cellSize = (int) inputCellSize.getValue();

        if (height != game.getHeight()) {
            game.setHeight(height);
        }

        if (width != game.getWidth()) {
            game.setWidth(width);
        }

        if (cellSize != fieldPanel.getCellSize()) {
            fieldPanel.setCellSize(cellSize);
        }

        setVisible(false);
        dispose();
    }

    /**
     * Metodo che viene chiamato alla pressione del pulsante Cancel, esce senza fare nulla.
     */
    private void onButtonCancelClick() {
        setVisible(false);
        dispose();
    }
}
