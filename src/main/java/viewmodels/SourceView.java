package viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.SourceViewInterface;
import static viewmodels.constants.ViewModelConstants.*;

public class SourceView implements SourceViewInterface{

    private final StringProperty author;
    private final StringProperty title;
    private final StringProperty year;
    private final ObservableList<QuoteViewInterface> quoteList;
    private int id;

    public SourceView() {
        quoteList= FXCollections.observableArrayList();
        this.author = new SimpleStringProperty(EMPTY);
        this.title = new SimpleStringProperty(EMPTY);
        this.year = new SimpleStringProperty(EMPTY);
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

    @Override
    public ObservableList<QuoteViewInterface> getQuoteList() {
        return this.quoteList;
    }

    public String toString() {
        return "Author: " + this.author.getValue() + " | Title: " + this.title.getValue() + " | Year: " + this.year.getValue() + " | ID: " + this.id;
    }
}
