package source.factories;

import source.Interfaces.SourceInterface;
import source.enums.SupportedTypes;

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
    public static Object[][] getSourceTemplate(SourceInterface source) {
        List<List<Object>> sourceTemplate = new ArrayList<>();
        List<Object> templateRow;
        try {
            Class sourceClass = source.getClass();
            Object fieldValue;
            for (Field field : sourceClass.getDeclaredFields()) {
                templateRow = new ArrayList<>();
                Class fieldClass;
                //set attribute type, but if the getType() returns a primitive type call getWrapperClass
                if (field.getType().isPrimitive()) {
                    fieldClass = getWrapperClass(field.getType());
                } else {
                    fieldClass = field.getType();
                }
                if (isTypeSupported(fieldClass)) {
                    //set attribute name
                    templateRow.add(field.getName());

                    fieldValue = getObjectValue(field.getName(), source);

                    //add field class
                    templateRow.add(fieldClass);
                    //this field is null and should be set later by the user
                    templateRow.add(fieldValue);
                    sourceTemplate.add(templateRow);
                }
            }

            //Test to see what the list contains
            for (List<Object> o : sourceTemplate) {
                System.out.println("[" + o.get(0) + "][" + o.get(1) + "][" + o.get(2) + "]");
            }
            //convert the list back do a normal 2 dimensional array
            return sourceTemplate.stream().map(row -> row.toArray(new Object[0])).toArray(Object[][]::new);
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

    /**
     * Return the wrapper class of an primitive type
     * @param primitiveType the primitive type
     * @return wrapper class
     */
    private static Class<?> getWrapperClass(Class<?> primitiveType) {
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
        return null;
    }


}
