package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Artikel extends Quelle{
    private StringProperty ausgabe;

    public Artikel (String titel, String autor, String jahr, String ausgabe) {
        super(titel, autor, jahr);
        this.ausgabe = new SimpleStringProperty(ausgabe);
    }

    public Artikel (int id, String titel, String autor, String jahr, String ausgabe) {
        super(id, titel, autor, jahr);
        this.ausgabe = new SimpleStringProperty(ausgabe);
    }

    public String getAusgabe() {
        return ausgabe.get();
    }

    public StringProperty ausgabeProperty() {
        return ausgabe;
    }

    public void setAusgabe(String ausgabe) {
        this.ausgabe.set(ausgabe);
    }
}
