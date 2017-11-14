package models;

import models.interfaces.TagInterface;

/**
 * Created by Cedric on 07.11.2017.
 * Tag
 */
public class Tag implements TagInterface{
    private String text;
    private int id;

    public Tag(String text) {
        this.text = text;
    }

    public Tag() {
    }

    public Tag(String text, int id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
