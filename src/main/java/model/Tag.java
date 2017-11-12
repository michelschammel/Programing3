package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Cedric Schreiner
 */
public class Tag {
    private StringProperty text;
    private int tagId;

    public Tag(String text){
        this.text = new SimpleStringProperty(text);
    }

    public Tag(String text, int tagId) {
        this.text = new SimpleStringProperty(text);
        this.tagId = tagId;
    }

    public Tag(Tag tag) {
        this.text = new SimpleStringProperty(tag.getText());
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty textProperty() {
        return text;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
