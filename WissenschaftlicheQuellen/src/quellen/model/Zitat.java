package quellen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Zitat {
    
    private final StringProperty text;
    private ObservableList<String> tag;
    
    public Zitat (String text) {
        this.text = new SimpleStringProperty(text);
        this.tag = FXCollections.observableArrayList();
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

}
