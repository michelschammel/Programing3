package source.source_utilities;

import source.models.interfaces.SourceInterface;
import source.models.interfaces.SourceTemplateInterface;
import source.utilities.SourceUtillities;
import source.viewmodels.interfaces.SourceViewInterface;

import java.lang.reflect.Method;
import java.util.List;

public abstract class SourceConverter {
    private static final String VIEW = "View";
    //This string contains the package for all viewmodels
    private static final String PACKAGE = "source.viewmodels.";

    public static SourceViewInterface convertSourceToSourceView(SourceInterface source) {
        Class sourceClass = source.getClass();
        String viewClassPath = PACKAGE + sourceClass.getSimpleName() + VIEW;
        SourceViewInterface sourceView = null;
        try {
            sourceView = (SourceViewInterface) Class.forName(viewClassPath).newInstance();
            setValues(sourceView, source);
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
}
