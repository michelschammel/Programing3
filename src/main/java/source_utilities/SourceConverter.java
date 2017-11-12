package source_utilities;

import models.interfaces.QuoteInterface;
import models.interfaces.SourceInterface;
import models.interfaces.SourceTemplateInterface;
import utilities.SourceUtillities;
import viewmodels.QuoteView;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.SourceViewInterface;
import viewmodels.interfaces.TagViewInterface;

import java.lang.reflect.Method;
import java.util.List;

public abstract class SourceConverter {
    private static final String VIEW = "View";
    //This string contains the package for all viewmodels
    private static final String VIEW_PACKAGE = "viewmodels.";

    public static SourceViewInterface convertSourceToSourceView(SourceInterface source) {
        Class sourceClass = source.getClass();
        String viewClassPath = VIEW_PACKAGE + sourceClass.getSimpleName() + VIEW;
        SourceViewInterface sourceView = null;
        try {
            sourceView = (SourceViewInterface) Class.forName(viewClassPath).newInstance();
            setValues(sourceView, source);
            convertSourceQuoteListToSourceQuoteViewList(sourceView, source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceView;
    }

    private static void setValues(SourceViewInterface sourceView, SourceInterface source) {
        List<SourceTemplateInterface> sourceTemplate = SourceReflection.getSourceTemplate(source);
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

    private static void convertSourceQuoteListToSourceQuoteViewList(SourceViewInterface sourceView, SourceInterface source) {
        QuoteViewInterface quoteView;
        //TagViewInterface tagView;
        for (QuoteInterface quote: source.getQuoteList()) {
            quoteView = new QuoteView();
            quoteView.setId(quote.getId());
            quoteView.setSourceId(quote.getSourceId());
            quoteView.setText(quote.getText());
            sourceView.addQuote(quoteView);
        }
    }
}
