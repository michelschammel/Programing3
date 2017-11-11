package source.models.interfaces;

/**
 * Created by Cedric on 07.11.2017.
 * Interface for Others
 */
public interface OthersInterface extends SourceInterface{
    void setPublisher(String publisher);

    void setEdition(String edition);

    String getPublisher();

    String getEdition();
}
