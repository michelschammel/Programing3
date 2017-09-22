package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Buch extends Quelle{

    private StringProperty herausgeber;
    private StringProperty auflage;
    private StringProperty monat;
    private StringProperty isbn;

    public Buch (String titel, String autor, String jahr, String herausgeber, String auflage, String monat, String isbn) {
        super(titel, autor, jahr);
        this.herausgeber = new SimpleStringProperty(herausgeber);
        this.auflage = new SimpleStringProperty(auflage);
        this.monat = new SimpleStringProperty(monat);
        this.isbn = new SimpleStringProperty(isbn);
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

    public String getMonat() {
        return monat.get();
    }

    public StringProperty monatProperty() {
        return monat;
    }

    public void setMonat(String monat) {
        this.monat.set(monat);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }
}
