package source.factories;

import source.Interfaces.SourceInterface;

import java.lang.reflect.Field;
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
            for (Field field : sourceClass.getDeclaredFields()) {
                templateRow = new ArrayList<>();
                //set attribute name
                templateRow.add(field.getName());
                //set attribute type, but if the getType() returns a primitive type call getWrapperClass
                if (field.getType().isPrimitive()) {
                    templateRow.add(getWrapperClass(field.getType()));
                } else {
                    templateRow.add(field.getType());
                }
                //this field is null and should be set later by the user
                templateRow.add(null);
                sourceTemplate.add(templateRow);
            }
            return sourceTemplate.stream().map(row -> row.toArray(new Object[0])).toArray(Object[][]::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void test(Object o){

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
