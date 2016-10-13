package tk.alerighi.life;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Finestra principale del Game of life
 *
 * @author Alessandro Righi
 */

/*
 * TODO:
 *  - pulizia del codice
 *  - creare una classe separata per il timer ?
 *  - gestire meglio gli stati del programma (creare una classe ? O aggiungere alla classe game ?)
 *  - rimuovere metodi poco usati, creare metodi nuovi per spezzare metodi troppo lunghi, tipo metodi load e save
 *  - generare pacchetto jar ?
 */
class MainWindow extends JFrame {

    private final String TITLE = "Game of life";
    private GameFieldPanel gameFieldPanel;
    private Timer timer = new Timer();
    private boolean timerRunning = false;
    private JScrollPane scrollPane;
    private FileManager fileManager;
    private String status = "[New]";
    private int refreshIntervalms = 1000;

    /**
     * Costruttore della finestra principale
     */
    MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridLayout());
        createMenuBar();

        gameFieldPanel = new GameFieldPanel();
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(gameFieldPanel);
        getContentPane().add(scrollPane);

        fileManager = new FileManager();
        setTitle();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void setTitle() {
        String title = TITLE;
        if (fileManager.getFile() != null) {
            title += " (" + fileManager.getFile() + ")";
        }
        title += " - " + gameFieldPanel.getGame().getWidth() + "x" + gameFieldPanel.getGame().getHeight();
        title += " " + status;
        setTitle(title);
    }

    /**
     * Metodo che mostra il dialogo per il cambo delle dimensioni del campo.
     */
    private void showChangeSizeDialog() {
        stopTimer();
        new GameSettingsDialog(this, gameFieldPanel);
        gameFieldPanel.recomputeSize();
        scrollPane.updateUI();
        setTitle();
    }

    /**
     * Metodo che avvia il timer
     */
    private void startTimer() {
        if (!timerRunning) {
            timer = new Timer();
            timer.schedule(new EvolveGame(), 0, refreshIntervalms);
            timerRunning = true;
            status = "[Running]";
            setTitle();
        }
    }

    /**
     * Metodo che ferma il timer.
     */
    private void stopTimer() {
        if (timerRunning) {
            timer.cancel();
            timerRunning = false;
            status = "[Paused]";
            setTitle();
        }
    }

    /**
     * Metodo che inizializza un nuovo gioco
     */
    private void newGame() {
        stopTimer();
        gameFieldPanel.resetGame();
        status = "[New]";
        setTitle();
    }

    /**
     * Avvia e stoppa il timer
     */
    private void runStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    /**
     * Metodo che salva il file. Nel caso non sia stato fatto precedentemente un salvataggio, chiede dove salvare il
     * file.
     */
    private void save() {
        if (fileManager.getFile() == null) {
            saveAs();
        }
        fileManager.saveGame(gameFieldPanel.getGame());
    }

    /**
     * Metodo che mostra un dialogo per il salvataggio del file e poi salva il file.
     */
    private void saveAs() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Game of life files", "life");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setCurrentDirectory(fileManager.getCurrentDir());
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File choosedFile = fileChooser.getSelectedFile();
            if (!choosedFile.toString().endsWith(".life")) {
                choosedFile = new File(choosedFile + ".life");
            }
            fileManager.setFile(choosedFile);
            fileManager.saveGame(gameFieldPanel.getGame());
            setTitle();
        }
    }

    /**
     * Metodo che mostra un dialogo per l'apertura di un file e quindi lo apre.
     */
    private void open() {
        stopTimer();
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Game of life files", "life");
        fileChooser.setFileFilter(fileFilter);
        fileChooser.setCurrentDirectory(fileManager.getCurrentDir());
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileManager.setFile(fileChooser.getSelectedFile());
            gameFieldPanel.setGame(fileManager.loadGame());
            scrollPane.updateUI();
        }
        setTitle();
    }

    private void openLastSaved() {
        if (fileManager.getFile() != null) {
            stopTimer();
            gameFieldPanel.setGame(fileManager.loadGame());
            scrollPane.updateUI();
            setTitle();
        }
    }

    /**
     * Metodo che crea la barra dei menu
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);

        JMenuItem itemStart = new JMenuItem("Run/Stop");
        itemStart.addActionListener(e -> runStop());
        itemStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        gameMenu.add(itemStart);

        JMenuItem itemReset = new JMenuItem("Reset");
        itemReset.addActionListener(e -> newGame());
        itemReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        gameMenu.add(itemReset);

        JMenuItem itemResize = new JMenuItem("Settings");
        itemResize.addActionListener(e -> showChangeSizeDialog());
        itemResize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        gameMenu.add(itemResize);

        JMenuItem itemZoomIn = new JMenuItem("ZoomIn");
        itemZoomIn.addActionListener(e -> {
            gameFieldPanel.zoomIn();
            scrollPane.updateUI();
        });
        itemZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        gameMenu.add(itemZoomIn);

        JMenuItem itemZoomOut = new JMenuItem("ZoomOut");
        itemZoomOut.addActionListener(e -> {
            gameFieldPanel.zoomOut();
            scrollPane.updateUI();
        });
        itemZoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        gameMenu.add(itemZoomOut);

        JMenuItem itemLoad = new JMenuItem("Open");
        itemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        itemLoad.addActionListener(e -> open());
        fileMenu.add(itemLoad);

        JMenuItem itemReLoad = new JMenuItem("Open Last File");
        itemReLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        itemReLoad.addActionListener(e -> openLastSaved());
        fileMenu.add(itemReLoad);

        JMenuItem itemSave = new JMenuItem("Save");
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        itemSave.addActionListener(e -> save());
        fileMenu.add(itemSave);

        JMenuItem itemSaveAs = new JMenuItem("Save as");
        itemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        itemSaveAs.addActionListener(e -> saveAs());
        fileMenu.add(itemSaveAs);

    }

    int getRefreshIntervalms() {
        return refreshIntervalms;
    }

    void setRefreshIntervalms(int refreshIntervalms) {
        this.refreshIntervalms = refreshIntervalms;
    }

    /**
     * Classe che estende un timerTask per evolvere il gioco.
     */
    private class EvolveGame extends TimerTask {
        @Override
        public void run() {
            gameFieldPanel.evolveGame();
            if (!gameFieldPanel.getGame().isAlive()) {
                stopTimer();
                status = "[Dead]";
                setTitle();
            }
        }

    }

}
