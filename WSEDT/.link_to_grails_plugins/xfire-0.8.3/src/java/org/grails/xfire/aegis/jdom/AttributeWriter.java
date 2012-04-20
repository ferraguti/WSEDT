package org.grails.xfire.aegis.jdom;

import javax.xml.namespace.QName;

import org.grails.xfire.aegis.AbstractMessageWriter;
import org.grails.xfire.aegis.MessageWriter;
import org.jdom.Attribute;

public class AttributeWriter
    extends AbstractMessageWriter
{
    private Attribute att;

    public AttributeWriter(Attribute att)
    {
        this.att = att;
    }
    
    public void writeValue(Object value)
    {
        att.setValue(value.toString());
    }

    public MessageWriter getAttributeWriter(String name)
    {
        throw new IllegalStateException();
    }

    public MessageWriter getAttributeWriter(String name, String namespace)
    {
        throw new IllegalStateException();
    }

    public MessageWriter getAttributeWriter(QName qname)
    {
        throw new IllegalStateException();
    }

    public MessageWriter getElementWriter(String name)
    {
        throw new IllegalStateException();
    }

    public MessageWriter getElementWriter(String name, String namespace)
    {
        throw new IllegalStateException();
    }

    public MessageWriter getElementWriter(QName qname)
    {
        throw new IllegalStateException();
    }

    public String getPrefixForNamespace( String namespace )
    {
        throw new IllegalStateException();
    }

    public String getPrefixForNamespace(String namespace, String hint)
    {
        throw new IllegalStateException();
    }

    public void close()
    {
    }
}
