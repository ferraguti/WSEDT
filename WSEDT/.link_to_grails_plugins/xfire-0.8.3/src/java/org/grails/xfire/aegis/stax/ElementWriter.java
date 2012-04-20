package org.grails.xfire.aegis.stax;

import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.util.NamespaceHelper;
import org.grails.xfire.aegis.AbstractMessageWriter;
import org.grails.xfire.aegis.MessageWriter;

/**
 * LiteralWriter
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class ElementWriter
    extends AbstractMessageWriter
    implements MessageWriter
{
    private XMLStreamWriter writer;
    
    private String namespace;
    
    private String name;

    private String prefix;

    /**
     * Create a LiteralWriter but without writing an element name.
     * 
     * @param writer
     */
    public ElementWriter(XMLStreamWriter writer)
    {
        this.writer = writer;
    }
    
    public ElementWriter(XMLStreamWriter writer, String name, String namespace)
    {
        this(writer, name, namespace, null);
    }

    public ElementWriter(XMLStreamWriter streamWriter, QName name)
    {
        this(streamWriter, name.getLocalPart(), name.getNamespaceURI());
    }

    public ElementWriter(XMLStreamWriter writer, String name, String namespace, String prefix)
    {
        this.writer = writer;
        this.namespace = namespace;
        this.name = name;
        this.prefix = prefix;
        
        try
        {
            writeStartElement();
        }
        catch (XMLStreamException e)
        {
            throw new XFireRuntimeException("Error writing document.", e);
        }
    }
    
    /**
     * @param os
     * @throws XMLStreamException 
     */
    public ElementWriter(OutputStream os, String name, String namespace) 
        throws XMLStreamException
    {
        XMLOutputFactory ofactory = XMLOutputFactory.newInstance();
        this.writer = ofactory.createXMLStreamWriter(os);
        
        this.namespace = namespace;
        this.name = name;
        
        try
        {
            writeStartElement();
        }
        catch ( XMLStreamException e )
        {
            throw new XFireRuntimeException("Error writing document.", e);
        }
    }

    private void writeStartElement() 
        throws XMLStreamException
    {
        if (namespace != null)
        {
            boolean declare = false;

            String decPrefix = writer.getNamespaceContext().getPrefix(namespace);
                
            // If the user didn't specify a prefix, create one
            if (prefix == null && decPrefix == null)
            {
                declare = true;
                prefix = NamespaceHelper.getUniquePrefix(writer);
            }
            else if (prefix == null)
            {
                prefix = decPrefix;
            }
            else if (!prefix.equals(decPrefix))
            {
                declare = true;
            }
            
            writer.writeStartElement(prefix, name, namespace);
            
            if (declare)
            {
                writer.setPrefix(prefix, namespace);
                writer.writeNamespace(prefix, namespace);
            }
        }
        else
        {
            writer.writeStartElement(name);
        }
    }

    /**
     * @see org.grails.xfire.aegis.MessageWriter#writeValue(java.lang.Object)
     */
    public void writeValue(Object value)
    {
        try
        {
            if ( value != null )
                writer.writeCharacters( value.toString() );
        }
        catch ( XMLStreamException e )
        {
            throw new XFireRuntimeException("Error writing document.", e);
        }
    }
    
    /**
     * @see org.grails.xfire.aegis.MessageWriter#getWriter(java.lang.String)
     */
    public MessageWriter getElementWriter(String name)
    {
        return new ElementWriter(writer, name, namespace);
    }

    public MessageWriter getElementWriter(String name, String ns)
    {
        return new ElementWriter(writer, name, ns);
    }

    public MessageWriter getElementWriter(QName qname)
    {
        return new ElementWriter(writer, 
                                 qname.getLocalPart(), 
                                 qname.getNamespaceURI(), 
                                 qname.getPrefix());
    }

    public String getNamespace()
    {
        return namespace;
    }

    public void close()
    {
        try
        {
            writer.writeEndElement();
        }
        catch ( XMLStreamException e )
        {
            throw new XFireRuntimeException("Error writing document.", e);
        }
    }

    public void flush() throws XMLStreamException
    {
        writer.flush();
    }

    public XMLStreamWriter getXMLStreamWriter()
    {
        return writer;
    }

    public MessageWriter getAttributeWriter(String name)
    {
        return new AttributeWriter(writer, name, namespace);
    }

    public MessageWriter getAttributeWriter(String name, String namespace)
    {
        return new AttributeWriter(writer, name, namespace);
    }

    public MessageWriter getAttributeWriter(QName qname)
    {
        return new AttributeWriter(writer, qname.getLocalPart(), qname.getNamespaceURI());
    }

    public String getPrefixForNamespace( String namespace )
    {
        try
        {
            String prefix = writer.getPrefix(namespace);

            if (prefix == null )
            {
                prefix = NamespaceHelper.getUniquePrefix(writer);

                writer.setPrefix(prefix, namespace);
                writer.writeNamespace(prefix, namespace);
            }

            return prefix;
        }
        catch( XMLStreamException e )
        {
            throw new XFireRuntimeException("Error writing document.", e);
        }
    }

    public String getPrefixForNamespace(String namespace, String hint)
    {
        try
        {
            String prefix = writer.getPrefix(namespace);

            if(prefix == null)
            {
                String ns = writer.getNamespaceContext().getNamespaceURI(hint);
                if(ns == null)
                {
                    prefix = hint;
                }
                else
                if(ns.equals(namespace))
                {
                    return prefix;
                }
                else
                {
                    prefix = NamespaceHelper.getUniquePrefix(writer);
                }

                writer.setPrefix(prefix, namespace);
                writer.writeNamespace(prefix, namespace);
            }

            return prefix;
        }
        catch(XMLStreamException e)
        {
            throw new XFireRuntimeException("Error writing document.", e);
        }
    }
}
