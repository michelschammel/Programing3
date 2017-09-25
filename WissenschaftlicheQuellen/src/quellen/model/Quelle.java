package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Zitat.
 *
 */
public class Quelle {

    private StringProperty autor;
    private StringProperty titel;
    private StringProperty jahr;
    private ObservableList<Zitat> zitatData;
    private int id;

    /**
     * The Constructor.
     * 
     * @param autor
     * @param titel
     * @param jahr
     * 
     */
    public Quelle(int id, String titel, String autor, String jahr) {
        this.autor = new SimpleStringProperty(autor);
        this.titel = new SimpleStringProperty(titel);
        this.jahr = new SimpleStringProperty(jahr);
        this.id = id;
        zitatData= FXCollections.observableArrayList();
    }

    public Quelle(String titel, String autor, String jahr) {
        this.autor = new SimpleStringProperty(autor);
        this.titel = new SimpleStringProperty(titel);
        this.jahr = new SimpleStringProperty(jahr);
        zitatData= FXCollections.observableArrayList();
    }

    /**
     * Creates a copy of an existing Object
     * @param quelle quelle to copy
     */
    public Quelle(Quelle quelle) {
        this.autor = new SimpleStringProperty(quelle.getAutor());
        this.titel = new SimpleStringProperty(quelle.getTitel());
        this.jahr = new SimpleStringProperty(quelle.getJahr());
        this.id = quelle.getId();
        this.zitatData = FXCollections.observableArrayList();
        quelle.getZitatList().forEach(zitat -> zitatData.add(new Zitat(zitat)));
    }

    public void addZitat (Zitat z) {
        zitatData.add(z);    
    }
    
    public String getTitel() {
        return titel.get();
    }

    public void setTitel(String titel) {
        this.titel.set(titel);
    }

    public StringProperty titelProperty() {
        return titel;
    }

    public String getAutor() {
        return autor.get();
    }

    public void setAutor(String autor) {
        this.autor.set(autor);
    }

    public StringProperty autorProperty() {
        return autor;
    }

    public String getJahr() {
        return jahr.get();
    }

    public void setJahr(String jahr) {
        this.jahr.set(jahr);
    }

    public StringProperty jahrProperty() {
        return jahr;
    }
    
    public ObservableList<Zitat> getZitatList() {
        return this.zitatData;
    }

    public void setZitatListe(ObservableList<Zitat> zitatListe) {
        this.zitatData = zitatListe;
    }

    public int getId() {
        return id;
    }
}
