package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Onlinequelle extends Quelle{
    private StringProperty aufrufdatum;
    private StringProperty einrichtung;

    public Onlinequelle (String titel, String autor, String jahr, String aufrufdatum, String einrichtung) {
        super(titel, autor, jahr);
        this.aufrufdatum = new SimpleStringProperty(aufrufdatum);
        this. einrichtung = new SimpleStringProperty(einrichtung);
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

    public String getEinrichtung() {
        return einrichtung.get();
    }

    public StringProperty einrichtungProperty() {
        return einrichtung;
    }

    public void setEinrichtung(String einrichtung) {
        this.einrichtung.set(einrichtung);
    }
}
