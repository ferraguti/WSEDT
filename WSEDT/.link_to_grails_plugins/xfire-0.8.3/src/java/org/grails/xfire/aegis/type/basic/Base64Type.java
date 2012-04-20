package org.grails.xfire.aegis.type.basic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.soap.SoapConstants;
import org.codehaus.xfire.util.Base64;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;
import org.grails.xfire.aegis.type.mtom.AbstractXOPType;
import org.grails.xfire.aegis.type.mtom.ByteArrayType;

/**
 * Converts back and forth to byte[] objects.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class Base64Type
    extends Type
{
    private static ByteArrayType optimizedType = new ByteArrayType();
    
    public Base64Type()
    {
        super();
    }

    public Object readObject(MessageReader mreader, MessageContext context)
        throws XFireFault
    {
        boolean mtomEnabled = Boolean.valueOf((String) context.getContextualProperty(SoapConstants.MTOM_ENABLED)).booleanValue();
        XMLStreamReader reader = mreader.getXMLStreamReader();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        try
        {
            int event = reader.next();
            while(!reader.isCharacters() && !reader.isEndElement() && !reader.isStartElement()) 
            {
                event = reader.next();
            }

            if (reader.isStartElement() &&
                    reader.getName().equals(AbstractXOPType.XOP_INCLUDE))
            {
                if (mtomEnabled)
                {
                    return optimizedType.readObject(mreader, context);
                }
                else
                {
                    throw new XFireFault("Unexpected element: " + reader.getName(), XFireFault.SENDER);
                }
            }
           
            if (reader.isEndElement()) 
            {
            	reader.next();
                return new byte[0];
            }
            
            int length = reader.getTextLength();
            
            char[] myBuffer = new char[length];
            for (int sourceStart = 0;; sourceStart += length)
            {
                int nCopied = reader.getTextCharacters(sourceStart, myBuffer, 0, length);
                
                Base64.decode(myBuffer, 0, nCopied, bos);
                
                if (nCopied < length)
                    break;
            }
            
            while (reader.getEventType() != XMLStreamReader.END_ELEMENT) reader.next();
            
            // Advance just past the end element
            reader.next();
            
            return bos.toByteArray();
        }
        catch (IOException e)
        {
            throw new XFireFault("Could not parse base64Binary data.", e, XFireFault.SENDER);
        }
        catch (XMLStreamException e)
        {
            throw new XFireFault("Could not parse base64Binary data.", e, XFireFault.SENDER);
        }
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        boolean mtomEnabled = Boolean.valueOf((String) context.getContextualProperty(SoapConstants.MTOM_ENABLED)).booleanValue();
        if (mtomEnabled)
        {
            optimizedType.writeObject(object, writer, context);
            return;
        }

        byte[] data = (byte[]) object;

        writer.writeValue( Base64.encode(data) );
    }
}
