package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WissenschaftlicheArbeit extends Quelle{
    private StringProperty herausgeber;
    private StringProperty einrichtung;

    public WissenschaftlicheArbeit (String titel, String autor, String jahr, String herausgeber, String einrichtung) {
        super(titel, autor, jahr);
        this.herausgeber = new SimpleStringProperty(herausgeber);
        this.einrichtung = new SimpleStringProperty(einrichtung);
    }
    public WissenschaftlicheArbeit (int id, String titel, String autor, String jahr, String herausgeber, String einrichtung) {
        super(id, titel, autor, jahr);
        this.herausgeber = new SimpleStringProperty(herausgeber);
        this.einrichtung = new SimpleStringProperty(einrichtung);
    }
    /**
     * Creates a copy of an existing Object
     * @param wissenschaftlicheArbeit quelle to copy
     */
    public WissenschaftlicheArbeit (WissenschaftlicheArbeit wissenschaftlicheArbeit) {
        super(wissenschaftlicheArbeit);
        this.herausgeber = new SimpleStringProperty(wissenschaftlicheArbeit.getHerausgeber());
        this.einrichtung = new SimpleStringProperty(wissenschaftlicheArbeit.getEinrichtung());
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
