package models.interfaces;

/**
 * Created by Cedric on 07.11.2017.
 * Book
 */
public interface BookInterface extends SourceInterface{
    void setPublisher(String publisher);

    void setEdition(String edition);

    void setIsbn(String isbn);

    void setMonth(String month);

    String getPublisher();

    String getEdition();

    String getIsbn();

    String getMonth();
}
