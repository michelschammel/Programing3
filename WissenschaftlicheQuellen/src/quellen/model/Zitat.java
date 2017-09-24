package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Zitat {
    
    private final StringProperty text;
    private ObservableList<Tag> tag;
    private int quellenId;
    private int zitatId;
    
    public Zitat (String text) {
        this.text = new SimpleStringProperty(text);
        this.tag = FXCollections.observableArrayList();
    }

    public Zitat (String text, int quellenId, int zitatId) {
        this.text = new SimpleStringProperty(text);
        this.quellenId = quellenId;
        this.tag = FXCollections.observableArrayList();
        this.zitatId = zitatId;
    }

    public Zitat (Zitat zitat) {
        this.text = new SimpleStringProperty(zitat.getText());
        this.quellenId = zitat.getQuellenId();
        this.tag = FXCollections.observableArrayList();
        this.zitatId = zitat.getZitatId();
        zitat.getTagList().forEach(tag -> this.tag.add(new Tag(tag.getText())));
    }

    public void addTag(Tag tag) {
        this.tag.add(tag);
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

    public void addTag(String tag) {
        this.tag.add(new Tag(tag));
    }

    public ObservableList<Tag> getTagList(){
        return this.tag;
    }

    public int getQuellenId() {
        return quellenId;
    }

    public void setQuellenId(int quellenId) {
        this.quellenId = quellenId;
    }

    public int getZitatId() {
        return zitatId;
    }
}
