package viewmodels.interfaces;

import javafx.beans.property.StringProperty;

public interface QuoteViewInterface {
    void setText(String text);

    void setId(int id);

    void setSourceId(int sourceId);

    String getText();

    StringProperty getTextProperty();

    int getId();

    int getSourceId();

    void addTag(TagViewInterface tag);

    void removeTag(TagViewInterface tag);
}
