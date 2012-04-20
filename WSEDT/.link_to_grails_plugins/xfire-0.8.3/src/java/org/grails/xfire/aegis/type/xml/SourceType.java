package org.grails.xfire.aegis.type.xml;

import javanet.staxutils.ContentHandlerToXMLStreamWriter;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.util.STAXUtils;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.stax.ElementWriter;
import org.grails.xfire.aegis.type.Type;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Reads and writes <code>javax.xml.transform.Source</code> types.
 * <p>
 * The XML stream is converted DOMSource and sent off.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 * @see javanet.staxutils.StAXSource
 * @see javax.xml.stream.XMLInputFactory
 * @see org.codehaus.xfire.util.STAXUtils
 */
public class SourceType
    extends Type
{
    public SourceType()
    {
       setTypeClass(Source.class);
       setWriteOuter(false);
    }
    
    public Object readObject(MessageReader mreader, MessageContext context)
        throws XFireFault
    {
        DocumentType dt = (DocumentType) getTypeMapping().getType(Document.class);
        
        return new DOMSource((Document) dt.readObject(mreader, context));
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        try
        {
            if (object == null) return;
            
            write((Source) object, ((ElementWriter) writer).getXMLStreamWriter());
        }
        catch (XMLStreamException e)
        {
            throw new XFireFault("Could not write xml.", e, XFireFault.SENDER);
        }
    }

    protected void write(Source object, XMLStreamWriter writer)
        throws FactoryConfigurationError, XMLStreamException, XFireFault
    {
        if (object == null) return;

        if (object instanceof DOMSource)
        {
            DOMSource ds = (DOMSource) object;
            
            Element element = null;
            if (ds.getNode() instanceof Element)
            {
                element = (Element) ds.getNode();
            }
            else if (ds.getNode() instanceof Document)
            {
                element = ((Document) ds.getNode()).getDocumentElement();
            }
            else
            {
                throw new XFireFault("Node type " + ds.getNode().getClass() + 
                                     " was not understood.", XFireFault.RECEIVER);
            }
           
            STAXUtils.writeElement(element, writer, false);
        }
        else if (object instanceof SAXSource)
        {
            SAXSource source = (SAXSource) object;
      
            try
            {
                XMLReader xmlReader = source.getXMLReader();
                if (xmlReader == null)
                    xmlReader = createXMLReader();
                
                xmlReader.setContentHandler(new FilteringContentHandlerToXMLStreamWriter(writer));
                
                xmlReader.parse(source.getInputSource());
            }
            catch (Exception e)
            {
                throw new XFireFault("Could not send xml.", e, XFireFault.RECEIVER);
            }
        }
        else if (object instanceof StreamSource)
        {
            StreamSource ss = (StreamSource) object;
            XMLStreamReader reader = STAXUtils.createXMLStreamReader(ss.getInputStream(), null,null);
            STAXUtils.copy(reader, writer);
        }
    }
    
    protected XMLReader createXMLReader()
        throws SAXException
    {
        // In JDK 1.4, the xml reader factory does not look for META-INF
        // services
        // If the org.xml.sax.driver system property is not defined, and
        // exception will be thrown.
        // In these cases, default to xerces parser
        try
        {
            return XMLReaderFactory.createXMLReader();
        }
        catch (Exception e)
        {
            return XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        }
    }
    
    class FilteringContentHandlerToXMLStreamWriter extends ContentHandlerToXMLStreamWriter
    {
        public FilteringContentHandlerToXMLStreamWriter(XMLStreamWriter xmlStreamWriter)
        {
            super(xmlStreamWriter);
        }

        public void startDocument() throws SAXException
        {
        }

        public void endDocument() throws SAXException
        {
        }
    }
}
