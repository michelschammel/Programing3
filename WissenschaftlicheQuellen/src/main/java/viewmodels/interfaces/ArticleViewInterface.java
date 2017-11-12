package viewmodels.interfaces;

import javafx.beans.property.StringProperty;

public interface ArticleViewInterface extends SourceViewInterface{
    void setEdition(String edition);

    void setMagazine(String magazine);

    String getEdition();

    StringProperty getEditionProperty();

    String getMagazine();

    StringProperty getMagazineProperty();
}
