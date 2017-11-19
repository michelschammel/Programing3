package factories;

import models.interfaces.SourceInterface;
import models.*;

/**
 * Created by Cedric on 09.11.2017.
 * factory to produce different types of sources
 */
public abstract class SourceFactory {

    public static final String SOURCE = "Source";
    public static final String BOOK = "Book";
    public static final String ARTICLE = "Article";
    public static final String OTHERS = "Others";
    public static final String SCIENTIFIC_WORK = "ScientificWork";
    public static final String ONLINE_SOURCE = "Onlinesource";

    public static SourceInterface produceSource(String sourceType) {
        switch (sourceType) {
            case SOURCE:
                return new Source();
            case BOOK:
                return new Book();
            case ARTICLE:
                return new Article();
            case OTHERS:
                return new Others();
            case SCIENTIFIC_WORK:
                return new ScientificWork();
            case ONLINE_SOURCE:
                return new Onlinesource();
        }
        return null;
    }
}
