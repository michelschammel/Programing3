package source.models;

import source.Interfaces.ArticleInterface;
import source.Interfaces.QuoteInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 07.11.2017.
 * ArticleInterface
 */
public class Article implements ArticleInterface{

    private String year;
    private String titel;
    private String author;
    private String edition;
    private String magazine;
    private int id;
    private List<QuoteInterface> quoteList;

    public Article(int id, String titel, String author, String year, String edition, String magazine) {
        this.quoteList = new ArrayList<>();
        this.year = year;
        this.titel = titel;
        this.author = author;
        this.id = id;
        this.edition = edition;
        this.magazine = magazine;
    }

    public Article() {
        this.quoteList = new ArrayList<>();
    }

    @Override
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public void setTitel(String titel) {
        this.titel = titel;
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
    public String getTitel() {
        return this.titel;
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
        List<Object> attributeList = new ArrayList<>();
        attributeList.add(this.id);
        attributeList.add(this.edition);
        attributeList.add(this.magazine);
        return attributeList;
    }

    @Override
    public void setEdition(String edition) {
        this.edition = edition;
    }

    @Override
    public void setMagazine(String magazine) {
        this.magazine = magazine;
    }

    @Override
    public String getEdition() {
        return this.edition;
    }

    @Override
    public String getMagazine() {
        return this.magazine;
    }
}
