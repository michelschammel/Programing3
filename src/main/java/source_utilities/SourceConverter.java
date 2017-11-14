package source_utilities;

import models.Quote;
import models.interfaces.QuoteInterface;
import models.interfaces.SourceInterface;
import models.interfaces.SourceTemplateInterface;
import models.interfaces.TagInterface;
import utilities.SourceUtillities;
import viewmodels.QuoteView;
import viewmodels.TagView;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.SourceViewInterface;
import viewmodels.interfaces.TagViewInterface;

import java.lang.reflect.Method;
import java.util.List;

public abstract class SourceConverter {
    private static final String VIEW = "View";
    //This string contains the package for all viewmodels
    private static final String VIEW_MODELS_PACKAGE = "viewmodels.";
    private static final String MODELS_PACKAGE = "models.";

    public static SourceViewInterface convertSourceToSourceView(SourceInterface source) {
        Class sourceClass = source.getClass();
        String viewClassPath = VIEW_MODELS_PACKAGE + sourceClass.getSimpleName() + VIEW;
        SourceViewInterface sourceView = null;
        try {
            sourceView = (SourceViewInterface) Class.forName(viewClassPath).newInstance();
            setValues(sourceView, source);
            convertQuoteListToQuoteViewList(sourceView, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceView;
    }

    public static SourceInterface convertSourceViewToSource(SourceViewInterface sourceView) {
        Class sourceClass = sourceView.getClass();
        String sourceClassPath = MODELS_PACKAGE + sourceClass.getSimpleName();
        sourceClassPath = sourceClassPath.substring(0, sourceClassPath.length() - 4);
        SourceInterface source = null;
        try {
            source = (SourceInterface) Class.forName(sourceClassPath).newInstance();
            setValues(source, sourceView);
            convertSourceViewQuoteListToSourceQuoteList(source, sourceView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    private static void setValues(Object sourceView, Object source) {
        List<SourceTemplateInterface> sourceTemplate = SourceReflection.getTemplate(source);
        String methodName;
        String attributeName;
        Method method;
        if (sourceTemplate != null) {
            try {
                for (SourceTemplateInterface templateRow : sourceTemplate) {
                    attributeName = templateRow.getAttributeName();
                    //build the string for the set"Attribute" method name
                    methodName = "set" + Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);

                    //get the method with a fitting method name
                    method = sourceView.getClass().getDeclaredMethod(methodName, SourceUtillities.getPrimitiveFromWrapperClass(templateRow.getAttributeClass()));

                    //call the setter method and set the value
                    method.invoke(sourceView, templateRow.getAttributeValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copies all quotes from a Source to a SourceView
     * @param sourceView source where all quotes are added
     * @param source source where we get the quotes from
     */
    private static void convertQuoteListToQuoteViewList(SourceViewInterface sourceView, SourceInterface source) {
        QuoteViewInterface quoteView;
        for (QuoteInterface quote: source.getQuoteList()) {
            quoteView = new QuoteView();
            quoteView.setId(quote.getId());
            quoteView.setSourceId(quote.getSourceId());
            quoteView.setText(quote.getText());
            //add all tag from the quote to the quoteView
            convertSourceQuoteTagToSOurceQuoteViewTag(quoteView, quote);
            sourceView.addQuote(quoteView);
        }
    }

    /**
     * Copies all tags from a normal Quote to a QuoteView
     * @param quoteView quote where the tags are added
     * @param quote quote where we get the tags from
     */
    private static void convertSourceQuoteTagToSOurceQuoteViewTag(QuoteViewInterface quoteView, QuoteInterface quote) {
        TagViewInterface tagView;
        for (TagInterface tag : quote.getTagList()) {
            tagView = new TagView();
            tagView.setId(tag.getId());
            tagView.setText(tag.getText());
            quoteView.addTag(tagView);
        }
    }

    /**
     * Copies all quotes from a SourceView to a Source
     * @param source source where all quotes are added
     * @param sourceView source where we get the quotes from
     */
    private static void convertSourceViewQuoteListToSourceQuoteList(SourceInterface source, SourceViewInterface sourceView) {
        QuoteInterface quote;
        for (QuoteViewInterface quoteView : sourceView.getQuoteList()) {
            quote = new Quote();
            quote.setId(quoteView.getId());
            quote.setSourceId(quoteView.getSourceId());
            quote.setText(quoteView.getText());
            convertQuoteViewListToQuoteList(quote, quoteView);
            source.addQuote(quote);
        }
    }

    /**
     * Copies all tags from a QuoteView to a Quote
     * @param quote quote where the tags are added
     * @param quoteView quote where we get the tags from
     */
    private static void convertQuoteViewListToQuoteList(QuoteInterface quote, QuoteViewInterface quoteView) {
        TagInterface tag;
        for (TagViewInterface tagView : quoteView.getTagList()) {
            tag = new models.Tag();
            tag.setId(tagView.getId());
            tag.setText(tagView.getText());
            quote.addTag(tag);
        }
    }
}
