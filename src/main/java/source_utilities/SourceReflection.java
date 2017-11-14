package source_utilities;

import enums.SupportedTypes;
import models.SourceTemplate;
import models.interfaces.SourceTemplateInterface;
import utilities.SourceUtillities;

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
    public static List<SourceTemplateInterface> getTemplate(Object source) {
        List<SourceTemplateInterface> sourceTemplate = new ArrayList<>();
        SourceTemplateInterface templateRow;
        try {
            Class sourceClass = source.getClass();
            Object fieldValue;
            for (Field field : sourceClass.getDeclaredFields()) {
                templateRow = new SourceTemplate();
                Class fieldClass;

                fieldClass = SourceUtillities.getWrapperclassFromPrimitve(field.getType());

                if (isTypeSupported(fieldClass)) {
                    //set attribute name
                    templateRow.setAttributeName(field.getName());

                    //get the value of the attribute
                    fieldValue = getObjectValue(field.getName(), source);
                    //add field class
                    fieldClass = specialCase(fieldClass);
                    templateRow.setAttributeClass(fieldClass);
                    //add attribute value
                    templateRow.setAttributeValue(fieldValue);
                    sourceTemplate.add(templateRow);
                }
            }

            //Test to see what the list contains
            for (SourceTemplateInterface o : sourceTemplate) {
                System.out.println("[" + o.getAttributeName() + "][" + o.getAttributeClass() + "][" + o.getAttributeValue() + "]");
            }
            //convert the list back do a normal 2 dimensional array
            return sourceTemplate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getObjectValue(String attribute, Object source) {
        Object value = null;
        String getterMethodName;
        Method method;
        attribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
        getterMethodName = "get" + attribute;
        try {
            method = source.getClass().getDeclaredMethod(getterMethodName);
            value = method.invoke(source);
            System.out.println(getterMethodName + " | " + value);
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


}
