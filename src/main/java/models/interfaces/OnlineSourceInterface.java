package models.interfaces;

/**
 * Created by Cedric on 07.11.2017.
 * Interface for OnlineSources
 */
public interface OnlineSourceInterface extends SourceInterface{
    void setPollingDate(String pollingDate);

    void setUrl(String url);

    String getPollingDate();

    String getUrl();
}
