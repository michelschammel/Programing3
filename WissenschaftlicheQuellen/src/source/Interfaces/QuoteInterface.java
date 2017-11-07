package source.Interfaces;

import java.util.List;

/**
 * Created by Cedric on 07.11.2017.
 * Interface for Quote
 */
public interface QuoteInterface {
    void setTitel(String titel);

    void setSourceId (int sourceId);

    void setId(int id);

    void setTagList(List<TagInterface> list);

    String getTitel();

    int getSourceId();

    int getId();

    List<TagInterface> getTagList();

    void addTag(TagInterface tag);

    void removeTag(TagInterface tag);
}
