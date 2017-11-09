package source.factories;

import source.Interfaces.BookInterface;
import source.Interfaces.SourceInterface;
import source.models.*;

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

    public static SourceInterface produceSource(String sourceType) {
        if (sourceType.equals(STANDARD)) {
            return new Source();
        } else if (sourceType.equals(BOOK)) {
            return new Book();
        } else if (sourceType.equals(ARTICLE)) {
            return new Article();
        }
        return null;
    }

    public static BookInterface produceBook() {
        return (Book)produceSource(BOOK);
    }
}
