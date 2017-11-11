package source.models.interfaces;

public interface SourceTemplateInterface {
    void setAttributeName(String attributeName);

    void setAttributeClass(Class attributeClass);

    void setAttributeValue(Object attributeValue);

    String getAttributeName();

    Class getAttributeClass();

    Object getAttributeValue();
}
