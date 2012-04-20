package org.grails.xfire.aegis.jdom;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.xfire.util.NamespaceHelper;
import org.grails.xfire.aegis.AbstractMessageWriter;
import org.grails.xfire.aegis.MessageWriter;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMWriter
    extends AbstractMessageWriter
{
    private Element element;
    
    public JDOMWriter(Element element)
    {
        this.element = element;
    }
    
    public void writeValue(Object value)
    {
        element.addContent(value.toString());
    }

    public void writeValue(Object value, String ns, String attr)
    {
        String prefix = NamespaceHelper.getUniquePrefix(element, ns);
        
        element.setAttribute(new Attribute(attr, value.toString(), Namespace.getNamespace(prefix, ns)));
    }

    public MessageWriter getElementWriter(String name)
    {
        return getElementWriter(name, element.getNamespaceURI());
    }

    public MessageWriter getElementWriter(String name, String namespace)
    {
        String prefix = NamespaceHelper.getUniquePrefix(element, namespace);
        
        Element child = new Element(name, Namespace.getNamespace(prefix, namespace));
        element.addContent(child);
        
        return new JDOMWriter(child);
    }

    public MessageWriter getElementWriter(QName qname)
    {
        return getElementWriter(qname.getLocalPart(), qname.getNamespaceURI());
    }

    public String getPrefixForNamespace( String namespace )
    {
        return NamespaceHelper.getUniquePrefix(element, namespace);
    }

    public XMLStreamWriter getXMLStreamWriter()
    {
        throw new UnsupportedOperationException("Stream writing not supported from a JDOMWriter.");
    }

    public String getPrefixForNamespace(String namespace, String hint)
    {
        // todo: this goes for the option of ignoring the hint - we should probably at least attempt to honour it
        return NamespaceHelper.getUniquePrefix(element, namespace);
    }

    public MessageWriter getAttributeWriter(String name)
    {
        Attribute att = new Attribute(name, "", element.getNamespace());
        element.setAttribute(att);
        return new AttributeWriter(att);
    }

    public MessageWriter getAttributeWriter(String name, String namespace)
    {
        Attribute att;
        if (namespace != null && namespace.length() > 0)
        {
            String prefix = NamespaceHelper.getUniquePrefix(element, namespace);
            att = new Attribute(name, "", Namespace.getNamespace(prefix, namespace));
        }
        else
        {
            att = new Attribute(name, "");
        }

        element.setAttribute(att);
        return new AttributeWriter(att);
    }

    public MessageWriter getAttributeWriter(QName qname)
    {
        return getAttributeWriter(qname.getLocalPart(), qname.getNamespaceURI());
    }

    public void close()
    {
    }
}
