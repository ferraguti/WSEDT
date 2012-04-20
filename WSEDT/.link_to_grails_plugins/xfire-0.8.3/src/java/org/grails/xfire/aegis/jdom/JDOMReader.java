package org.grails.xfire.aegis.jdom;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.xfire.util.stax.JDOMStreamReader;
import org.grails.xfire.aegis.AbstractMessageReader;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.stax.AttributeReader;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMReader
    extends AbstractMessageReader
    implements MessageReader
{
    private Element element;
    private int currentChild = 0;
    private int currentAttribute = 0;
    private List elements;
    private QName qname;
    
    public JDOMReader(Element element)
    {
        this.element = element;
        this.elements = element.getChildren();
    }
    
    public String getValue()
    {
        return element.getValue();
    }

    public String getValue(String ns, String attr)
    {
        return element.getAttributeValue(attr, ns);
    }

    public boolean hasMoreElementReaders()
    {
        return (currentChild < elements.size());
    }

    public MessageReader getNextElementReader()
    {
        currentChild++;
        return new JDOMReader((Element) elements.get(currentChild-1));
    }

    public QName getName()
    {
        if (qname == null)
        {
            qname = new QName(element.getNamespaceURI(), 
                              element.getName(), 
                              element.getNamespacePrefix());
        }
        return qname;
    }

    public String getLocalName()
    {
        return element.getName();
    }

    public String getNamespace()
    {
        return element.getNamespaceURI();
    }

    public XMLStreamReader getXMLStreamReader()
    {
        return new JDOMStreamReader(element);
    }

    public boolean hasMoreAttributeReaders()
    {
        return (currentAttribute < element.getAttributes().size());
    }

    public MessageReader getAttributeReader( QName attName )
    {
        String value = element.getAttributeValue(attName.getLocalPart(),
                                                 Namespace.getNamespace(attName.getNamespaceURI()));
        return new AttributeReader(attName, value);
    }

    public MessageReader getNextAttributeReader()
    {
        Attribute att = (Attribute) element.getAttributes().get(currentAttribute);
        currentAttribute++;
        
        return new AttributeReader(new QName(att.getNamespaceURI(), att.getName()), att.getValue());
    }

    public String getNamespaceForPrefix( String prefix )
    {
        Namespace namespace = element.getNamespace( prefix );
        return null == namespace ? null : namespace.getURI();
    }
}
