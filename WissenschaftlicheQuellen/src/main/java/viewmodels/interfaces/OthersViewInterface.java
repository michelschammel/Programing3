package viewmodels.interfaces;

import javafx.beans.property.StringProperty;

public interface OthersViewInterface extends SourceViewInterface{
    void setPublisher(String publisher);

    void setEdition(String edition);

    String getPublisher();

    StringProperty getPublisherProperty();

    String getEdition();

    StringProperty getEditionProperty();
}
