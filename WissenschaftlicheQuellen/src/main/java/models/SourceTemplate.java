package models;

import models.interfaces.SourceTemplateInterface;

public class SourceTemplate implements SourceTemplateInterface{
    private String attributeName;
    private Class attributeClass;
    private Object attributeValue;

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Class getAttributeClass() {
        return attributeClass;
    }

    public void setAttributeClass(Class attributeClass) {
        this.attributeClass = attributeClass;
    }

    public Object getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(Object attributeValue) {
        this.attributeValue = attributeValue;
    }
}
