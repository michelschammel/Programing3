package models;

import models.interfaces.QuoteInterface;
import models.interfaces.TagInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 07.11.2017.
 * Quote
 */
public class Quote implements QuoteInterface{
    private String text;
    private int id;
    private int sourceId;
    private List<TagInterface> tagList;

    public Quote(int id, String text, int sourceId) {
        this.tagList = new ArrayList<>();
        this.text = text;
        this.sourceId = sourceId;
        this.id = id;
    }

    public Quote() {
        this.tagList = new ArrayList<>();
    }

    @Override
    public void setText(String titel) {
        this.text = titel;
    }

    @Override
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setTagList(List<TagInterface> list) {
        this.tagList = list;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public int getSourceId() {
        return this.sourceId;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public List<TagInterface> getTagList() {
        return this.tagList;
    }

    @Override
    public void addTag(TagInterface tag) {
        this.tagList.add(tag);
    }

    @Override
    public void removeTag(TagInterface tag) {
        this.tagList.remove(tag);
    }
}
