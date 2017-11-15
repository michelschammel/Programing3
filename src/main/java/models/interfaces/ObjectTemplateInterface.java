package models.interfaces;

public interface ObjectTemplateInterface {
    void setAttributeName(String attributeName);

    void setAttributeClass(Class attributeClass);

    void setAttributeValue(Object attributeValue);

    String getAttributeName();

    Class getAttributeClass();

    Object getAttributeValue();
}
