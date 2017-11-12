package models.interfaces;

/**
 * Created by Cedric on 07.11.2017.
 * Interface for ScientificWork
 */
public interface ScientificWorkInterface extends SourceInterface {
    void setPublisher(String publisher);

    void setInstitution(String institution);

    String getPublisher();

    String getInstitution();
}
