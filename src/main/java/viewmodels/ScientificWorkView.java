package viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.ScientificWorkViewInterface;
import static viewmodels.constants.ViewModelConstants.*;

public class ScientificWorkView implements ScientificWorkViewInterface {
    private StringProperty author;
    private StringProperty title;
    private StringProperty year;
    private StringProperty publisher;
    private StringProperty institution;
    private ObservableList<QuoteViewInterface> quoteList;
    private int id;

    public ScientificWorkView() {
        quoteList= FXCollections.observableArrayList();
        this.author = new SimpleStringProperty(EMPTY);
        this.title = new SimpleStringProperty(EMPTY);
        this.year = new SimpleStringProperty(EMPTY);
        this.publisher = new SimpleStringProperty(EMPTY);
        this.institution = new SimpleStringProperty(EMPTY);
    }

    @Override
    public void setPublisher(String publisher) {
        this.publisher.setValue(publisher);
    }

    @Override
    public void setInstitution(String institution) {
        this.institution.setValue(institution);
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
    public String getInstitution() {
        return this.institution.getValue();
    }

    @Override
    public StringProperty getInstitutionProperty() {
        return this.institution;
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
}
