package models.interfaces;

import java.util.List;

public interface SourceInterface {

    void setYear(String year);

    void setTitle(String title);

    void setAuthor(String author);

    void setId(int id);

    void setQuoteList(List<QuoteInterface> list);

    String getYear();

    String getTitle();

    String getAuthor();

    int getId();

    List<QuoteInterface> getQuoteList();

    /**
     * Adds a new Quote with all it tags to to quote
     * @param quote to add to the source
     */
    void addQuote(QuoteInterface quote);

    /**
     * removes a qote
     * @param quote to be removed
     */
    void removeQuote(QuoteInterface quote);

    List<Object> getAttributeList();
}