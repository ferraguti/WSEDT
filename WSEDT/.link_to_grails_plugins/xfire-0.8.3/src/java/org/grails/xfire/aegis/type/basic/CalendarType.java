package org.grails.xfire.aegis.type.basic;

import java.text.ParseException;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.util.date.XsDateTimeFormat;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * Type for the Calendar class.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class CalendarType
    extends Type
{
    private static XsDateTimeFormat format = new XsDateTimeFormat();
    
    public Object readObject(MessageReader reader, MessageContext context) throws XFireFault
    {
        String value = reader.getValue();
        
        if (value == null) return null;
        
        try
        {
            return format.parseObject(value);
        }
        catch (ParseException e)
        {
            throw new XFireFault("Could not parse xs:dateTime: " + e.getMessage(), e, XFireFault.SENDER);
        }
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        writer.writeValue(format.format(object));
    }
}
