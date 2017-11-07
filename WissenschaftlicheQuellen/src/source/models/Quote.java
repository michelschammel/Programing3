package source.models;

import source.Interfaces.QuoteInterface;
import source.Interfaces.TagInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cedric on 07.11.2017.
 * Quote
 */
public class Quote implements QuoteInterface{
    private String titel;
    private int id;
    private int sourceId;
    private List<TagInterface> tagList;

    public Quote(String titel, int sourceId){
        this.tagList = new ArrayList<>();
        this.titel = titel;
        this.sourceId = sourceId;
    }

    public Quote(String titel, int sourceId, int id) {
        this.tagList = new ArrayList<>();
        this.titel = titel;
        this.sourceId = sourceId;
        this.id = id;
    }

    @Override
    public void setTitel(String titel) {
        this.titel = titel;
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
    public String getTitel() {
        return this.titel;
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
