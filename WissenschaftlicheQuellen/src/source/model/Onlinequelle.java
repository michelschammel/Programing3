package source.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * @author Cedric Schreiner
 */
public class Onlinequelle extends Quelle{
    private StringProperty aufrufdatum;
    private StringProperty url;

    public Onlinequelle (String titel, String autor, String jahr, String aufrufdatum, String url) {
        super(titel, autor, jahr);
        this.aufrufdatum = new SimpleStringProperty(aufrufdatum);
        this.url = new SimpleStringProperty(url);
    }

    public Onlinequelle (int id, String titel, String autor, String jahr, String aufrufdatum, String url) {
        super(id, titel, autor, jahr);
        this.aufrufdatum = new SimpleStringProperty(aufrufdatum);
        this.url = new SimpleStringProperty(url);
    }

    public Onlinequelle (int id, String titel, String autor, String jahr, String aufrufdatum, String url, ObservableList<Zitat> zitatList) {
        super(id, titel, autor, jahr);
        this.aufrufdatum = new SimpleStringProperty(aufrufdatum);
        this.url = new SimpleStringProperty(url);
        super.setZitatListe(zitatList);
    }

    /**
     * Creates a copy of an existing Object
     * @param onlinequelle quelle to copy
     */
    public Onlinequelle (Onlinequelle onlinequelle) {
        super(onlinequelle);
        this.aufrufdatum = new SimpleStringProperty(onlinequelle.getAufrufdatum());
        this.url = new SimpleStringProperty(onlinequelle.getUrl());
    }

    public String getAufrufdatum() {
        return aufrufdatum.get();
    }

    public StringProperty aufrufdatumProperty() {
        return aufrufdatum;
    }

    public void setAufrufdatum(String aufrufdatum) {
        this.aufrufdatum.set(aufrufdatum);
    }

    public String getUrl() {
        return url.get();
    }

    public StringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
    }
}
