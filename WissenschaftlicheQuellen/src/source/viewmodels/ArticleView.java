package source.viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import source.viewmodels.interfaces.ArticleViewInterface;
import source.viewmodels.interfaces.QuoteViewInterface;
import static source.viewmodels.constants.ViewModelConstants.*;

public class ArticleView implements ArticleViewInterface{

    private StringProperty author;
    private StringProperty title;
    private StringProperty year;
    private StringProperty magazine;
    private StringProperty edition;
    private ObservableList<QuoteViewInterface> quoteList;
    private int id;

    public ArticleView() {
        quoteList= FXCollections.observableArrayList();
        this.author = new SimpleStringProperty(EMPTY);
        this.title = new SimpleStringProperty(EMPTY);
        this.year = new SimpleStringProperty(EMPTY);
        this.magazine = new SimpleStringProperty(EMPTY);
        this.edition = new SimpleStringProperty(EMPTY);
    }

    @Override
    public void setEdition(String edition) {
        this.edition.setValue(edition);
    }

    @Override
    public void setMagazine(String magazine) {
        this.magazine.setValue(magazine);
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
    public String getMagazine() {
        return this.magazine.getValue();
    }

    @Override
    public StringProperty getMagazineProperty() {
        return this.magazine;
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
