package source.viewmodels.interfaces;

import javafx.beans.property.StringProperty;

public interface ScientificWorkViewInterface extends SourceViewInterface {
    void setPublisher(String publisher);

    void setInstitution(String institution);

    String getPublisher();

    StringProperty getPublisherProperty();

    String getInstitution();

    StringProperty getInstitutionProperty();
}
