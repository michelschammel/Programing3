package source.models;

import source.models.interfaces.OnlineSourceInterface;
import source.models.interfaces.QuoteInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 07.11.2017.
 * Onlinesource
 */
public class Onlinesource implements OnlineSourceInterface{

    private String year;
    private String title;
    private String author;
    private int id;
    private String url;
    private String pollingDate;
    private List<QuoteInterface> quoteList;

    public Onlinesource(int id, String title, String author, String year, String pollingDate, String url) {
        this.quoteList = new ArrayList<>();
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.pollingDate = pollingDate;
        this.url = url;
    }

    public Onlinesource() {
        this.quoteList = new ArrayList<>();
    }

    @Override
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public void setTitle(String titel) {
        this.title = titel;
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setQuoteList(List<QuoteInterface> list) {
        this.quoteList = list;
    }

    @Override
    public String getYear() {
        return this.year;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public List<QuoteInterface> getQuoteList() {
        return this.quoteList;
    }

    @Override
    public void addQuote(QuoteInterface quote) {
        this.quoteList.add(quote);
    }

    @Override
    public void removeQuote(QuoteInterface quote) {
        this.quoteList.remove(quote);
    }

    @Override
    public List<Object> getAttributeList() {
        return null;
    }

    @Override
    public void setPollingDate(String pollingDate) {
        this.pollingDate = pollingDate;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getPollingDate() {
        return this.pollingDate;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}
