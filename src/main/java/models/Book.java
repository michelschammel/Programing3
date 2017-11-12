package models;

import models.interfaces.BookInterface;
import models.interfaces.QuoteInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 07.11.2017.
 * Book
 */
public class Book implements BookInterface{

    private String year;
    private String title;
    private String author;
    private int id;
    private String publisher;
    private String edition;
    private String isbn;
    private String month;
    private List<QuoteInterface> quoteList;

    public Book(int id, String title, String author, String year, String publisher, String edition, String month, String isbn) {
        this.quoteList = new ArrayList<>();
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.edition = edition;
        this.month = month;
        this.isbn = isbn;
    }

    public Book() {
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
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public void setEdition(String edition) {
        this.edition = edition;
    }

    @Override
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String getPublisher() {
        return this.publisher;
    }

    @Override
    public String getEdition() {
        return this.edition;
    }

    @Override
    public String getIsbn() {
        return this.isbn;
    }

    @Override
    public String getMonth() {
        return this.month;
    }
}
