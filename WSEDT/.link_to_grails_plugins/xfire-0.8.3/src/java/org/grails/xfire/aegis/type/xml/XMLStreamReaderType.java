package org.grails.xfire.aegis.type.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.util.STAXUtils;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.stax.ElementReader;
import org.grails.xfire.aegis.stax.ElementWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * Reads and writes <code>org.w3c.dom.Document</code> types. 
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class XMLStreamReaderType
    extends Type
{
    public XMLStreamReaderType()
    {
        setWriteOuter(false);
    }

    public Object readObject(MessageReader mreader, MessageContext context)
        throws XFireFault
    {
        return ((ElementReader) mreader).getXMLStreamReader();
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        XMLStreamReader reader = (XMLStreamReader) object;
        
        try
        {
            STAXUtils.copy(reader, ((ElementWriter) writer).getXMLStreamWriter());
            reader.close();
        }
        catch (XMLStreamException e)
        {
            throw new XFireFault("Could not write xml.", e, XFireFault.SENDER);
        }
    }
}
