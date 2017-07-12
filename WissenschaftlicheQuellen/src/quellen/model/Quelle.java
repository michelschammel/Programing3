package quellen.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for a Zitat.
 *
 */
public class Quelle {

    private final StringProperty autor;
    private final StringProperty titel;
    private final StringProperty jahr;
    private ObservableList<Zitat> zitate;

    /**
     * Constructor with some initial data.
     * 
     * @param autor
     * @param titel
     * ...
     * 
     */
    public Quelle(String titel, String autor, String jahr) {
        this.autor = new SimpleStringProperty(autor);
        this.titel = new SimpleStringProperty(titel);
        this.jahr = new SimpleStringProperty(jahr);
        this.zitate = FXCollections.observableArrayList();
        
    }
    
    public void addZitat (Zitat z) {
        zitate.add(z);    
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
}
