package org.grails.xfire.aegis.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.util.NamespaceHelper;
import org.grails.xfire.aegis.AbstractMessageWriter;
import org.grails.xfire.aegis.MessageWriter;

public class AttributeWriter
    extends AbstractMessageWriter
{
    private XMLStreamWriter writer;
    private String namespace;
    private String name;
    private String prefix;
    
    public AttributeWriter(XMLStreamWriter writer, 
                           String name, 
                           String namespace)
    {
        this.writer = writer;
        this.name = name;
        this.namespace = namespace;

        try
        {
            if (namespace != null && namespace.length() > 0)
                prefix = NamespaceHelper.getUniquePrefix(writer, namespace, true);
            else
                prefix = "";
        }
        catch (XMLStreamException e)
        {
            throw new XFireRuntimeException("Couldn't write to stream.");
        }
    }
    
    public void writeValue(Object value)
    {
        try
        {
            writer.writeAttribute(prefix, namespace, name, value.toString());
        }
        catch (XMLStreamException e)
        {
            throw new XFireRuntimeException("Error writing document.", e);
        }
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