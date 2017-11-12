package factories;

import models.interfaces.SourceInterface;
import models.*;

/**
 * Created by Cedric on 09.11.2017.
 * factory to produce different types of sources
 */
public abstract class SourceFactory {

    public static final String STANDARD = "standard";
    public static final String BOOK = "book";
    public static final String ARTICLE = "article";
    public static final String OTHER = "other";
    public static final String SCIENTIFIC_WORK = "scientific work";
    public static final String ONLINE_SOURCE = "online source";

    public static SourceInterface produceSource(String sourceType) {
        switch (sourceType) {
            case STANDARD:
                return new Source();
            case BOOK:
                return new Book();
            case ARTICLE:
                return new Article();
            case OTHER:
                return new Others();
            case SCIENTIFIC_WORK:
                return new ScientificWork();
            case ONLINE_SOURCE:
                return new Onlinesource();
        }
        return null;
    }
}
