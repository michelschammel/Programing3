package source.viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import source.model.Zitat;

public class SourceView {
    private StringProperty autor;
    private StringProperty titel;
    private StringProperty jahr;
    private ObservableList<Zitat> zitatData;
    private int id;
    private String unterkategorie;

    /**
     * The Constructor.
     *
     * @param author name if the author
     * @param titel title if the source
     * @param year  the year were the source was published
     *
     */
    public SourceView(int id, String titel, String author, String year) {
        this.autor = new SimpleStringProperty(author);
        this.titel = new SimpleStringProperty(titel);
        this.jahr = new SimpleStringProperty(year);
        this.id = id;
        zitatData= FXCollections.observableArrayList();
    }

    public SourceView(String titel, String autor, String jahr) {
        this.autor = new SimpleStringProperty(autor);
        this.titel = new SimpleStringProperty(titel);
        this.jahr = new SimpleStringProperty(jahr);
        zitatData= FXCollections.observableArrayList();
    }

    /**
     * Creates a copy of an existing Object
     * @param quelle quelle to copy
     */
    public SourceView(source.model.Quelle quelle) {
        this.autor = new SimpleStringProperty(quelle.getAutor());
        this.titel = new SimpleStringProperty(quelle.getTitel());
        this.jahr = new SimpleStringProperty(quelle.getJahr());
        this.id = quelle.getId();
        this.zitatData = FXCollections.observableArrayList();
        quelle.getZitatList().forEach(zitat -> zitatData.add(new Zitat(zitat)));
    }

    public void addZitat (Zitat z) {
        zitatData.add(z);
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

    public ObservableList<Zitat> getZitatList() {
        return this.zitatData;
    }

    public void setZitatListe(ObservableList<Zitat> zitatListe) {
        this.zitatData = zitatListe;
    }

    public int getId() {
        return id;
    }

    public String getUnterkategorie() {return unterkategorie;}

    public void setId(int id) {
        this.id = id;
    }

    public void setUnterkategorie(String unterkategorie) {this.unterkategorie = unterkategorie; }
}
