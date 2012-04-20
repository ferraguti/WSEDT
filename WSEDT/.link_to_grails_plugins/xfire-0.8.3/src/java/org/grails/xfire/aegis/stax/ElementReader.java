package org.grails.xfire.aegis.stax;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.soap.SoapConstants;
import org.codehaus.xfire.util.STAXUtils;
import org.codehaus.xfire.util.stax.DepthXMLStreamReader;
import org.grails.xfire.aegis.AbstractMessageReader;
import org.grails.xfire.aegis.MessageReader;

/**
 * Reads literal encoded messages.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class ElementReader
    extends AbstractMessageReader
    implements MessageReader
{
    private static final Pattern QNAME_PATTERN = Pattern.compile("([^:]+):([^:]+)");

    private DepthXMLStreamReader root;

    private String value;

    private String localName;

    private QName name;

    private QName xsiType;

    private boolean hasCheckedChildren = false;

    private boolean hasChildren = false;
    
    private String namespace;

    private int depth;

    private int currentAttribute = 0;

    /**
     * @param root
     */
    public ElementReader(DepthXMLStreamReader root)
    {
        this.root = root;
        this.localName = root.getLocalName();
        this.name = root.getName();
        this.namespace = root.getNamespaceURI();

        extractXsiType();

        depth = root.getDepth();
    }

    public ElementReader(XMLStreamReader reader)
    {
        this(new DepthXMLStreamReader(reader));
    }

    /**
     * @param is
     * @throws XMLStreamException
     */
    public ElementReader(InputStream is) throws XMLStreamException
    {
        // XMLInputFactory factory = XMLInputFactory.newInstance();
        // XMLStreamReader xmlReader = factory.createXMLStreamReader(is);
        XMLStreamReader xmlReader = STAXUtils.createXMLStreamReader(is, null, null);

        xmlReader.nextTag();

        this.root = new DepthXMLStreamReader(xmlReader);
        this.localName = root.getLocalName();
        this.name = root.getName();
        this.namespace = root.getNamespaceURI();

        extractXsiType();

        depth = root.getDepth();
    }

    private void extractXsiType()
    {
        /*
         * We're making a conscious choice here -- garbage in == garbate out.
         */
        String xsiTypeQname = root.getAttributeValue(SoapConstants.XSI_NS, "type");
        if (xsiTypeQname != null)
        {
            Matcher m = QNAME_PATTERN.matcher(xsiTypeQname);
            if (m.matches())
            {
                NamespaceContext nc = root.getNamespaceContext();
                this.xsiType = new QName(nc.getNamespaceURI(m.group(1)), m.group(2), m.group(1));
            }
            else
            {
                this.xsiType = new QName(this.namespace, xsiTypeQname, "");
            }
        }
    }

    /**
     * @see org.grails.xfire.aegis.MessageReader#getValue()
     */
    public String getValue()
    {
        if (value == null) 
        {
            try
            {
                value = root.getElementText();

                while (checkHasMoreChildReaders()) {}
            }
            catch (XMLStreamException e)
            {
                throw new XFireRuntimeException("Could not read XML stream.", e);
            }
            
            if (value == null) {
            	value = "";
            }
        }
        
        return value.trim();
    }

    public String getValue(String ns, String attr)
    {
        return root.getAttributeValue(ns, attr);
    }

    public boolean hasMoreElementReaders()
    {
        // Check to see if we checked before,
        // so we don't mess up the stream position.
        if (!hasCheckedChildren)
            checkHasMoreChildReaders();

        return hasChildren;
    }

    private boolean checkHasMoreChildReaders()
    {
        try
        {
            int event = root.getEventType();
            while (root.hasNext())
            {
                switch (event)
                {
                case XMLStreamReader.START_ELEMENT:
                    if (root.getDepth() > depth)
                    {
                        hasCheckedChildren = true;
                        hasChildren = true;

                        return true;
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (root.getDepth() <= depth + 1)
                    {
                        hasCheckedChildren = true;
                        hasChildren = false;

                        if (root.hasNext())
                        {
                            root.next();
                        }
                        return false;
                    }
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    // We should never get here...
                    hasCheckedChildren = true;
                    hasChildren = false;
                    return false;
                default:
                    break;
                }

                if (root.hasNext())
                    event = root.next();
            }

            hasCheckedChildren = true;
            hasChildren = false;
            return false;
        }
        catch (XMLStreamException e)
        {
            throw new XFireRuntimeException("Error parsing document.", e);
        }
    }

    public MessageReader getNextElementReader()
    {
        if (!hasCheckedChildren)
            checkHasMoreChildReaders();

        if (!hasChildren)
            return null;

        hasCheckedChildren = false;

        return new ElementReader(root);
    }

    public QName getName()
    {
        return name;
    }

    public String getLocalName()
    {
        return localName;
    }

    public String getNamespace()
    {
        return namespace;
    }

    public QName getXsiType()
    {
        return xsiType;
    }

    public XMLStreamReader getXMLStreamReader()
    {
        return root;
    }

    public boolean hasMoreAttributeReaders()
    {
        if (!root.isStartElement())
            return false;

        return currentAttribute < root.getAttributeCount();
    }

    public MessageReader getAttributeReader(QName qName)
    {
        return new AttributeReader(qName, root.getAttributeValue(qName.getNamespaceURI(), qName
                .getLocalPart()));
    }

    public MessageReader getNextAttributeReader()
    {
        MessageReader reader = new AttributeReader(root.getAttributeName(currentAttribute), root
                .getAttributeValue(currentAttribute));
        currentAttribute++;

        return reader;
    }

    public String getNamespaceForPrefix(String prefix)
    {
        return root.getNamespaceURI(prefix);
    }
}
