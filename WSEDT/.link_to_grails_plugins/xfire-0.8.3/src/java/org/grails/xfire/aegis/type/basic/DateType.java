package org.grails.xfire.aegis.type.basic;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.util.date.XsDateFormat;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * Type for the Date class which serializes as an xsd:date (no time information).
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class DateType
    extends Type
{
    private static XsDateFormat format = new XsDateFormat();
    
    public Object readObject(MessageReader reader, MessageContext context) throws XFireFault
    {
        String value = reader.getValue();
        
        if (value == null) return null;
        
        try
        {
            Calendar c = (Calendar) format.parseObject(value);
            return c.getTime();
        }
        catch (ParseException e)
        {
            throw new XFireFault("Could not parse xs:dat: " + e.getMessage(), e, XFireFault.SENDER);
        }
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        Calendar c = Calendar.getInstance();
        c.setTime((Date) object);
        writer.writeValue(format.format(c));
    }   
}
