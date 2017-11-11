package source.viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import source.viewmodels.interfaces.QuoteViewInterface;
import source.viewmodels.interfaces.SourceViewInterface;
import static source.viewmodels.constants.ViewModelConstants.*;

public class SourceView implements SourceViewInterface{

    private StringProperty author;
    private StringProperty title;
    private StringProperty year;
    private ObservableList<QuoteViewInterface> quoteList;
    private int id;

    public SourceView() {
        quoteList= FXCollections.observableArrayList();
        this.author = new SimpleStringProperty(EMPTY);
        this.title = new SimpleStringProperty(EMPTY);
        this.year = new SimpleStringProperty(EMPTY);
    }

    /**
     * Creates a copy of an existing Object
     * @param quelle quelle to copy
     */
    public SourceView(source.model.Quelle quelle) {
        this.author = new SimpleStringProperty(quelle.getAutor());
        this.title = new SimpleStringProperty(quelle.getTitel());
        this.year = new SimpleStringProperty(quelle.getJahr());
        this.id = quelle.getId();
        this.quoteList = FXCollections.observableArrayList();
        //quelle.getZitatList().forEach(zitat -> quoteList.add(new Zitat(zitat)));
    }

    @Override
    public void setAuthor(String author) {
        this.author.setValue(author);
    }

    @Override
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    @Override
    public void setYear(String year) {
        this.year.setValue(year);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getAuthor() {
        return this.author.getValue();
    }

    @Override
    public StringProperty getAuthorProperty() {
        return this.author;
    }

    @Override
    public String getTitle() {
        return this.title.getValue();
    }

    @Override
    public StringProperty getTitleProperty() {
        return this.title;
    }

    @Override
    public String getYear() {
        return this.year.getValue();
    }

    @Override
    public StringProperty getYearProperty() {
        return this.year;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void addQuote(QuoteViewInterface quote) {
        this.quoteList.add(quote);
    }

    @Override
    public void removeQuote(QuoteViewInterface quote) {
        this.quoteList.remove(quote);
    }

    public String toString() {
        return "Author: " + this.author.getValue() + " | Title: " + this.title.getValue() + " | Year: " + this.year.getValue() + " | ID: " + this.id;
    }
}
