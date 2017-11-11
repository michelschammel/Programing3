package source.viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import source.viewmodels.interfaces.BookViewInterface;
import source.viewmodels.interfaces.QuoteViewInterface;
import static source.viewmodels.constants.ViewModelConstants.*;

public class BookView implements BookViewInterface{

    private StringProperty author;
    private StringProperty title;
    private StringProperty year;
    private StringProperty publisher;
    private StringProperty edition;
    private StringProperty month;
    private StringProperty isbn;
    private ObservableList<QuoteViewInterface> quoteList;
    private int id;

    public BookView() {
        this.quoteList = FXCollections.observableArrayList();
        this.author = new SimpleStringProperty(EMPTY);
        this.title = new SimpleStringProperty(EMPTY);
        this.year = new SimpleStringProperty(EMPTY);
        this.publisher = new SimpleStringProperty(EMPTY);
        this.edition = new SimpleStringProperty(EMPTY);
        this.month = new SimpleStringProperty(EMPTY);
        this.isbn = new SimpleStringProperty(EMPTY);
    }

    @Override
    public void setPublisher(String publisher) {
        this.publisher.setValue(publisher);
    }

    @Override
    public void setEdition(String edition) {
        this.edition.setValue(edition);
    }

    @Override
    public void setIsbn(String isbn) {
        this.isbn.setValue(isbn);
    }

    @Override
    public void setMonth(String month) {
        this.month.setValue(month);
    }

    @Override
    public String getPublisher() {
        return this.publisher.getValue();
    }

    @Override
    public StringProperty getPublisherProperty() {
        return this.publisher;
    }

    @Override
    public String getEdition() {
        return this.edition.getValue();
    }

    @Override
    public StringProperty getEditionProperty() {
        return this.edition;
    }

    @Override
    public String getIsbn() {
        return this.isbn.getValue();
    }

    @Override
    public StringProperty getIsbnProperty() {
        return this.isbn;
    }

    @Override
    public String getMonth() {
        return this.month.getValue();
    }

    @Override
    public StringProperty getMonthProperty() {
        return this.month;
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
