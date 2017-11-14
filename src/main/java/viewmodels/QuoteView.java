package viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.TagViewInterface;

import static viewmodels.constants.ViewModelConstants.*;

public class QuoteView implements QuoteViewInterface{
    private StringProperty text;
    private int id;
    private int sourceId;
    private ObservableList<TagViewInterface> tagList;

    public QuoteView() {
        this.tagList = FXCollections.observableArrayList();
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
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
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

    @Override
    public int getSourceId() {
        return this.sourceId;
    }

    @Override
    public void addTag(TagViewInterface tag) {
        this.tagList.add(tag);
    }

    @Override
    public void removeTag(TagViewInterface tag) {
        this.tagList.remove(tag);
    }

    @Override
    public ObservableList<TagViewInterface> getTagList() {
        return this.tagList;
    }
}
