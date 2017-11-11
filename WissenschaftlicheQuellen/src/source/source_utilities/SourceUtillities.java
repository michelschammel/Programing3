package source.source_utilities;

public abstract class SourceUtillities {
    /**
     * Return the wrapper class of an primitive type
     * @param primitiveType the primitive type
     * @return wrapper class
     */
    public static Class getWrapperclassFromPrimitve(Class primitiveType) {
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

    public static Class getPrimitiveFromWrapperClass(Class wrapperClass) {
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
}
