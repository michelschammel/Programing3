package source.viewmodels.interfaces;

import javafx.beans.property.StringProperty;

public interface OnlinesourceViewInterface extends SourceViewInterface {
    void setUrl(String url);

    void setPollingDate(String pollingDate);

    String getUrl();

    StringProperty getUrlProperty();

    String getPollingDate();

    StringProperty getPollingDateProperty();
}
