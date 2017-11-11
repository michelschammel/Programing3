package source.source_utilities;

import com.sun.istack.internal.NotNull;
import source.enums.SupportedTypes;
import source.models.SourceTemplate;
import source.models.interfaces.SourceInterface;
import source.models.interfaces.SourceTemplateInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class SourceReflection {

    /**
     * This methode creates a 2 dimensional Array
     * The Array has 3 Columns, the first row contains the name of the attribute, the second contains the third.
     * The third row is null and gets filled by the User. These are later used to set the attribute values of a object
     * @return object template
     */
    public static List<SourceTemplateInterface> getSourceTemplate(SourceInterface source) {
        List<SourceTemplateInterface> sourceTemplate = new ArrayList<>();
        SourceTemplateInterface templateRow;
        try {
            Class sourceClass = source.getClass();
            Object fieldValue;
            for (Field field : sourceClass.getDeclaredFields()) {
                templateRow = new SourceTemplate();
                Class fieldClass;
                //set attribute type, but if the getType() returns a primitive type call getWrapperClass
//                if (field.getType().isPrimitive()) {
//                    fieldClass = SourceTemplate.getWrapperclassFromPrimitve(field.getType());
//                    fieldClass = S
//                } else {
//                    fieldClass = field.getType();
//                }

                fieldClass = SourceUtillities.getWrapperclassFromPrimitve(field.getType());


                if (isTypeSupported(fieldClass)) {
                    //set attribute name
                    templateRow.setAttributeName(field.getName());

                    //get the value of the attribute
                    fieldValue = getObjectValue(field.getName(), source);
                    //add field class
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

    private static void test(){

    }

    private static Object getObjectValue(String attribute, SourceInterface source) {
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


}
