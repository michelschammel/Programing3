package models;

import models.interfaces.QuoteInterface;
import models.interfaces.SourceInterface;

import java.util.ArrayList;
import java.util.List;

public class Source implements SourceInterface{

    private String year;
    private String title;
    private String author;
    private int id;
    private List<QuoteInterface> quoteList;

    public Source(int id, String title, String author, String year) {
        this.quoteList = new ArrayList<>();
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Source() {
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
}
