package viewmodels.interfaces;

import javafx.beans.property.StringProperty;

public interface TagViewInterface {
    void setText(String text);

    void setId(int id);

    String getText();

    StringProperty getTextProperty();

    int getId();
}
