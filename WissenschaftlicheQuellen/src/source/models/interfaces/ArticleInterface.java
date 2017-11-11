package source.models.interfaces;

/**
 * Created by Cedric on 07.11.2017.
 * interface for article
 */
public interface ArticleInterface extends SourceInterface {
    void setEdition(String edition);

    void setMagazine(String magazine);

    String getEdition();

    String getMagazine();
}
