package tk.alerighi.life;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Classe che gestisce il caricamento ed il salvataggio del gioco su file XML
 */
public class FileManager {

    private File file;
    private File currentDir;
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;
    private Unmarshaller jaxbUnmarshaller;

    /**
     * Costruttore della classe
     */
    public FileManager() {
        currentDir = new File(System.getProperty("user.home"));
        try {
            jaxbContext = JAXBContext.newInstance(GameOfLife.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che salva il corrispondente gioco su file
     *
     * @param game il gioco da salvare
     */
    public void saveGame(GameOfLife game) {
        try {
            jaxbMarshaller.marshal(game, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che carica il gioco dal file
     *
     * @return il gioco caricato
     */
    public GameOfLife loadGame() {
        try {
            return (GameOfLife) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo che ritorna il file in uso
     *
     * @return file selezionato
     */
    public File getFile() {
        return file;
    }

    /**
     * Metodo che setta il file in uso
     *
     * @param file file in uso
     */
    public void setFile(File file) {
        this.file = file;
        currentDir = file.getParentFile();
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(File currentDir) {
        this.currentDir = currentDir;
    }
}
