package source.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * @author Cedric Schreiner
 */
public class Artikel extends Quelle{
    private StringProperty ausgabe;
    private StringProperty magazin;

    public Artikel (String titel, String autor, String jahr, String ausgabe, String magazin) {
        super(titel, autor, jahr);
        this.ausgabe = new SimpleStringProperty(ausgabe);
        this.magazin = new SimpleStringProperty(magazin);
    }

    public Artikel (int id, String titel, String autor, String jahr, String ausgabe, String magazin) {
        super(id, titel, autor, jahr);
        this.ausgabe = new SimpleStringProperty(ausgabe);
        this.magazin = new SimpleStringProperty(magazin);
    }

    public Artikel (int id, String titel, String autor, String jahr, String ausgabe, String magazin, ObservableList<Zitat> zitatList) {
        super(id, titel, autor, jahr);
        this.ausgabe = new SimpleStringProperty(ausgabe);
        this.magazin = new SimpleStringProperty(magazin);
        super.setZitatListe(zitatList);
    }

    /**
     * Creates a copy of an existing Object
     * @param artikel quelle to copy
     */
    public Artikel (Artikel artikel) {
        super(artikel);
        this.ausgabe = new SimpleStringProperty(artikel.getAusgabe());
        this.magazin = new SimpleStringProperty(artikel.getMagazin());
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

    public String getMagazin() {
        return magazin.get();
    }

    public StringProperty magazinProperty() {
        return magazin;
    }

    public void setMagazin(String magazin) {
        this.magazin.set(magazin);
    }
}
