package source.factories;

import source.viewmodels.*;
import source.viewmodels.interfaces.SourceViewInterface;

public abstract class SourceViewFactory {
    public static final String SOURCE_VIEW = "standard";
    public static final String BOOK_VIEW  = "book";
    public static final String ARTICLE_VIEW  = "article";
    public static final String OTHER_VIEW  = "other";
    public static final String SCIENTIFIC_WORK_VIEW  = "scientific work";
    public static final String ONLINE_SOURCE_VIEW = "online source";

    public static SourceViewInterface produceSourceView(String sourceType) {
        switch (sourceType) {
            case SOURCE_VIEW:
                return new SourceView();
            case BOOK_VIEW:
                return new BookView();
            case ARTICLE_VIEW:
                return new ArticleView();
            case OTHER_VIEW:
                return new OthersView();
            case ONLINE_SOURCE_VIEW:
                return new OnlinesourceView();
            case SCIENTIFIC_WORK_VIEW:
                return new ScientificWorkView();
        }
        return null;
    }
}
