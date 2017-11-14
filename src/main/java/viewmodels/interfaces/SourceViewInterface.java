package viewmodels.interfaces;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface SourceViewInterface {

    void setAuthor(String author);

    void setTitle(String title);

    void setYear(String year);

    void setId(int id);

    String getAuthor();

    StringProperty getAuthorProperty();

    String getTitle();

    StringProperty getTitleProperty();

    String getYear();

    StringProperty getYearProperty();

    int getId();

    void addQuote(QuoteViewInterface quote);

    void removeQuote(QuoteViewInterface quote);

    public ObservableList<QuoteViewInterface> getQuoteList();
}
