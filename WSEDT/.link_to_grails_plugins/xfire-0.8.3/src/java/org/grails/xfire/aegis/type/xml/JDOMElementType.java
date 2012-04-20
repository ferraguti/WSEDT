package org.grails.xfire.aegis.type.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.util.jdom.StaxBuilder;
import org.codehaus.xfire.util.jdom.StaxSerializer;
import org.codehaus.xfire.util.stax.JDOMStreamReader;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.stax.ElementReader;
import org.grails.xfire.aegis.stax.ElementWriter;
import org.grails.xfire.aegis.type.Type;
import org.jdom.Element;

/**
 * Reads and writes <code>org.w3c.dom.Document</code> types. 
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class JDOMElementType
    extends Type
{
   // private static final StaxBuilder builder = new StaxBuilder();
    private static final StaxSerializer serializer = new StaxSerializer();
    
    public JDOMElementType()
    {
        setWriteOuter(false);
    }

    public Object readObject(MessageReader mreader, MessageContext context)
        throws XFireFault
    {
        StaxBuilder builder = new StaxBuilder();
        try
        {
            XMLStreamReader reader = ((ElementReader) mreader).getXMLStreamReader();

            if (reader instanceof JDOMStreamReader)
            {
                return ((JDOMStreamReader) reader).getCurrentElement();
            }
            
            return builder.build(reader).getRootElement();
        }
        catch (XMLStreamException e)
        {
            throw new XFireFault("Could not parse xml.", e, XFireFault.SENDER);
        }
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        Element el = (Element) object;
        
        try
        {
            serializer.writeElement(el, ((ElementWriter) writer).getXMLStreamWriter());
        }
        catch (XMLStreamException e)
        {
            throw new XFireFault("Could not write xml.", e, XFireFault.SENDER);
        }
    }
}
