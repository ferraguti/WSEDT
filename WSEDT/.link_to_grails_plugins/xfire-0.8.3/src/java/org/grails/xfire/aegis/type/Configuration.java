package org.grails.xfire.aegis.type;

public class Configuration
{
    private boolean defaultExtensibleElements = false;

    private boolean defaultExtensibleAttributes = false;

    private boolean defaultNillable = true;

    private int defaultMinOccurs = 0;

    public boolean isDefaultExtensibleAttributes()
    {
        return defaultExtensibleAttributes;
    }

    public void setDefaultExtensibleAttributes(boolean defaultExtensibleAttributes)
    {
        this.defaultExtensibleAttributes = defaultExtensibleAttributes;
    }

    public boolean isDefaultExtensibleElements()
    {
        return defaultExtensibleElements;
    }

    public void setDefaultExtensibleElements(boolean defaultExtensibleElements)
    {
        this.defaultExtensibleElements = defaultExtensibleElements;
    }

    public int getDefaultMinOccurs()
    {
        return defaultMinOccurs;
    }

    public void setDefaultMinOccurs(int defaultMinOccurs)
    {
        this.defaultMinOccurs = defaultMinOccurs;
    }

    public boolean isDefaultNillable()
    {
        return defaultNillable;
    }

    public void setDefaultNillable(boolean defaultNillable)
    {
        this.defaultNillable = defaultNillable;
    }
}
