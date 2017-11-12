package viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import viewmodels.interfaces.OnlinesourceViewInterface;
import viewmodels.interfaces.QuoteViewInterface;
import static viewmodels.constants.ViewModelConstants.*;

public class OnlinesourceView implements OnlinesourceViewInterface {
    private StringProperty author;
    private StringProperty title;
    private StringProperty year;
    private StringProperty url;
    private StringProperty pollingDate;
    private ObservableList<QuoteViewInterface> quoteList;
    private int id;

    public OnlinesourceView() {
        quoteList= FXCollections.observableArrayList();
        this.author = new SimpleStringProperty(EMPTY);
        this.title = new SimpleStringProperty(EMPTY);
        this.year = new SimpleStringProperty(EMPTY);
        this.url = new SimpleStringProperty(EMPTY);
        this.pollingDate = new SimpleStringProperty(EMPTY);
    }

    @Override
    public void setUrl(String url) {
        this.url.setValue(url);
    }

    @Override
    public void setPollingDate(String pollingDate) {
        this.pollingDate.setValue(pollingDate);
    }

    @Override
    public String getUrl() {
        return this.url.getValue();
    }

    @Override
    public StringProperty getUrlProperty() {
        return this.url;
    }

    @Override
    public String getPollingDate() {
        return this.pollingDate.getValue();
    }

    @Override
    public StringProperty getPollingDateProperty() {
        return this.pollingDate;
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
}
