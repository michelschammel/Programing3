package viewmodels.interfaces;

import javafx.beans.property.StringProperty;

public interface BookViewInterface extends SourceViewInterface{
    void setPublisher(String publisher);

    void setEdition(String edition);

    void setIsbn(String isbn);

    void setMonth(String month);

    String getPublisher();

    StringProperty getPublisherProperty();

    String getEdition();

    StringProperty getEditionProperty();

    String getIsbn();

    StringProperty getIsbnProperty();

    String getMonth();

    StringProperty getMonthProperty();
}
