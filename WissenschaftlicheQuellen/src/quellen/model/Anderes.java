package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Anderes extends Quelle{
    private StringProperty herausgeber;
    private StringProperty auflage;
    private StringProperty ausgabe;

    public Anderes (String titel, String autor, String jahr, String herausgeber, String auflage, String ausgabe) {
        super (titel, autor, jahr);
        this.herausgeber = new SimpleStringProperty(herausgeber);
        this.auflage = new SimpleStringProperty(auflage);
        this.ausgabe = new SimpleStringProperty(ausgabe);
    }

    public String getHerausgeber() {
        return herausgeber.get();
    }

    public StringProperty herausgeberProperty() {
        return herausgeber;
    }

    public void setHerausgeber(String herausgeber) {
        this.herausgeber.set(herausgeber);
    }

    public String getAuflage() {
        return auflage.get();
    }

    public StringProperty auflageProperty() {
        return auflage;
    }

    public void setAuflage(String auflage) {
        this.auflage.set(auflage);
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
