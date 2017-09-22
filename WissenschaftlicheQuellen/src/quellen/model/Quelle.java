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

    /**
     * The Constructor.
     * 
     * @param autor
     * @param titel
     * @param jahr
     * 
     */
    public Quelle(String titel, String autor, String jahr) {
        this.autor = new SimpleStringProperty(autor);
        this.titel = new SimpleStringProperty(titel);
        this.jahr = new SimpleStringProperty(jahr);
        zitatData= FXCollections.observableArrayList();
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
}
