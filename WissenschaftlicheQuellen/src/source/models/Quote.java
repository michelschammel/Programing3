package source.models;

import source.models.interfaces.QuoteInterface;
import source.models.interfaces.TagInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 07.11.2017.
 * Quote
 */
public class Quote implements QuoteInterface{
    private String title;
    private int id;
    private int sourceId;
    private List<TagInterface> tagList;

    public Quote(int id, String title, int sourceId) {
        this.tagList = new ArrayList<>();
        this.title = title;
        this.sourceId = sourceId;
        this.id = id;
    }

    @Override
    public void setTitle(String titel) {
        this.title = titel;
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
    public String getTitle() {
        return this.title;
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
