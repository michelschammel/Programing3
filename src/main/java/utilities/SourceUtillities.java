package utilities;

import enums.SourceStandardAttributes;
import enums.SupportedTypes;
import javafx.scene.layout.GridPane;
import models.ObjectTemplate;
import models.interfaces.ObjectTemplateInterface;
import models.interfaces.QuoteInterface;
import models.interfaces.SourceInterface;
import models.interfaces.TagInterface;
import viewmodels.QuoteView;
import viewmodels.TagView;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.SourceViewInterface;
import viewmodels.interfaces.TagViewInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SourceUtillities {
    private static final String VIEW = "View";
    //This string contains the package for all viewmodels
    private static final String VIEW_MODELS_PACKAGE = "viewmodels.";
    private static final String MODELS_PACKAGE = "models.";

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

    /**
     * This Mehtod generates a sourcetemplate that contains for every attribute the name, class and value
     * of the provided source
     * @return object template
     */
    public static List<ObjectTemplateInterface> getTemplate(Object object) {
        List<ObjectTemplateInterface> sourceTemplate = new ArrayList<>();
        ObjectTemplateInterface templateRow;
        try {
            Class sourceClass = object.getClass();
            Object fieldValue;
            for (Field field : sourceClass.getDeclaredFields()) {
                templateRow = new ObjectTemplate();
                Class fieldClass;

                fieldClass = SourceUtillities.getWrapperclassFromPrimitve(field.getType());

                if (isTypeSupported(fieldClass)) {
                    //set attribute name
                    templateRow.setAttributeName(field.getName());

                    //get the value of the attribute
                    fieldValue = getObjectValue(field.getName(), object);
                    //add field class
                    fieldClass = specialCase(fieldClass);
                    templateRow.setAttributeClass(fieldClass);
                    //add attribute value
                    templateRow.setAttributeValue(fieldValue);
                    sourceTemplate.add(templateRow);
                }
            }
            return sourceTemplate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GridPane getUIGridPane(SourceViewInterface source) {
        List<ObjectTemplateInterface> template = getTemplate(source);
        removeBasicAttributes(template);
        GridPane gridPane = new GridPane();
        gridPane.addColumn(0);
        gridPane.addColumn(1);

        return null;
    }

    private static void moveListItem(List<ObjectTemplateInterface> list, String itemToMove, int moveItemTo) {
        if (moveItemTo <= list.size()) {
            int itemIndex = -1;
            for (ObjectTemplateInterface listRow : list) {
                if (listRow.getAttributeName().equals(itemToMove)) {
                    itemIndex = list.indexOf(listRow);
                }
            }

            if (itemIndex >= 0 && itemIndex != moveItemTo) {

            }
        }
    }

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


    public static void setObjectValue(String attribute, Object object, Object value) {
        String setterMethodName;
        Method method;
        attribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
        setterMethodName = "set" + attribute;
        try {
            method = object.getClass().getDeclaredMethod(setterMethodName, value.getClass());
            method.invoke(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void removeBasicAttributes(List<ObjectTemplateInterface> template) {
        for (SourceStandardAttributes attribute : SourceStandardAttributes.values()) {
            template.removeIf(templateRow -> templateRow.getAttributeName().equals(attribute.name()));
        }
    }

    /**
     * Return the wrapper class of an primitive type
     * @param primitiveType the primitive type
     * @return wrapper class
     */
    private static Class getWrapperclassFromPrimitve(Class primitiveType) {
        if (primitiveType.isPrimitive()) {
            String primitiveName = primitiveType.getName();
            switch (primitiveName) {
                case "int":
                    return Integer.class;
                case "double":
                    return Double.class;
                case "boolean":
                    return Boolean.class;
                case "byte":
                    return Byte.class;
                case "short":
                    return Short.class;
                case "long":
                    return Long.class;
                case "float":
                    return Float.class;
                case "char":
                    return Character.class;
            }
        }
        return primitiveType;
    }

    private static Class getPrimitiveFromWrapperClass(Class wrapperClass) {
        if (!wrapperClass.isPrimitive()) {
            String wrapperName = wrapperClass.getSimpleName();
            switch (wrapperName) {
                case "Integer":
                    return int.class;
                case "Double":
                    return double.class;
                case "Boolean":
                    return boolean.class;
                case "Byte":
                    return byte.class;
                case "Short":
                    return short.class;
                case "Long":
                    return long.class;
                case "Float":
                    return float.class;
                case "Character":
                    return char.class;
            }
        }
        return wrapperClass;
    }

    private static Object getObjectValue(String attribute, Object object) {
        Object value = null;
        String getterMethodName;
        Method method;
        attribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
        getterMethodName = "get" + attribute;
        try {
            method = object.getClass().getDeclaredMethod(getterMethodName);
            value = method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * @param type is to check if it is supported
     * @return true -> supported, false -> not supported
     */
    private static boolean isTypeSupported(Class type) {
        boolean isSupported = false;
        //Iterate over the complete list
        for (SupportedTypes supportedTypes : SupportedTypes.values()) {
            if (type.getSimpleName().equals(supportedTypes.name())) {
                isSupported = true;
            }
        }
        return isSupported;
    }

    private static Class specialCase(Class type) {
        String s = type.getSimpleName();
        if (s.equals("StringProperty")) {
            return String.class;
        }
        return type;
    }

    private static void setValues(Object sourceView, Object source) {
        List<ObjectTemplateInterface> sourceTemplate = getTemplate(source);
        String methodName;
        String attributeName;
        Method method;
        if (sourceTemplate != null) {
            try {
                for (ObjectTemplateInterface templateRow : sourceTemplate) {
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
            quote = new models.Quote();
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
