package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Zitat.
 *
 */
public class Zitat {

    private final StringProperty buch;
    private final StringProperty autor;
    private final StringProperty text;
    
    /**
     * Default constructor.
     */
    public Zitat() {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param buch
     * @param autor
     */
    public Zitat(String buch, String autor) {
        this.buch = new SimpleStringProperty(buch);
        this.autor = new SimpleStringProperty(autor);
        this.text = new SimpleStringProperty("test text");
    }

    public String getBuch() {
        return buch.get();
    }

    public void setBuch(String buch) {
        this.buch.set(buch);
    }

    public StringProperty buchProperty() {
        return buch;
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

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty textProperty() {
        return text;
    }
}
