package viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import viewmodels.interfaces.TagViewInterface;
import static viewmodels.constants.ViewModelConstants.*;

public class TagView implements TagViewInterface{

    private final StringProperty text;
    private int id;

    public TagView() {
        this.text = new SimpleStringProperty(EMPTY);
    }

    @Override
    public void setText(String text) {
        this.text.setValue(text);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return this.text.getValue();
    }

    @Override
    public StringProperty getTextProperty() {
        return this.text;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
