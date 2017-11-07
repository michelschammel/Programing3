package source.Interfaces;

import java.util.List;

public interface SourceInterface {
    /*Setter*/
    void setYear(String year);

    void setTitel(String titel);

    void setAuthor(String author);

    void setId(int id);

    void setQuoteList(List<QuoteInterface> list);

    /*Getter*/
    String getYear();

    String getTitel();

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
}
