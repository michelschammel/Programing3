package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * @author Cedric Schreiner
 */
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

    public Buch (int id, String titel, String autor, String jahr, String herausgeber, String auflage, String monat, String isbn) {
        super(id, titel, autor, jahr);
        this.herausgeber = new SimpleStringProperty(herausgeber);
        this.auflage = new SimpleStringProperty(auflage);
        this.monat = new SimpleStringProperty(monat);
        this.isbn = new SimpleStringProperty(isbn);
    }

    public Buch (int id, String titel, String autor, String jahr, String herausgeber, String auflage, String monat, String isbn, ObservableList<Zitat> zitatList) {
        super(id, titel, autor, jahr);
        this.herausgeber = new SimpleStringProperty(herausgeber);
        this.auflage = new SimpleStringProperty(auflage);
        this.monat = new SimpleStringProperty(monat);
        this.isbn = new SimpleStringProperty(isbn);
        super.setZitatListe(zitatList);
    }

    /**
     * Creates a copy of an existing Object
     * @param buch quelle to copy
     */
    public Buch (Buch buch) {
        super(buch);
        this.herausgeber = new SimpleStringProperty(buch.getHerausgeber());
        this.auflage = new SimpleStringProperty(buch.getAuflage());
        this.monat = new SimpleStringProperty(buch.getMonat());
        this.isbn = new SimpleStringProperty(buch.getIsbn());
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
