package utilities;

import enums.SourceStandardAttributes;
import enums.SupportedTypes;
import models.ObjectTemplate;
import models.interfaces.ObjectTemplateInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SourceReflection {

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

    public static void removeBasicAttributes(List<ObjectTemplateInterface> template) {
        for (SourceStandardAttributes attribute : SourceStandardAttributes.values()) {
            template.removeIf(templateRow -> templateRow.getAttributeName().equals(attribute.name()));
        }
    }


}
