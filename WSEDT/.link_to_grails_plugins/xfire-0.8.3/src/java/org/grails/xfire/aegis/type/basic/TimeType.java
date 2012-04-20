package org.grails.xfire.aegis.type.basic;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.util.date.XsTimeFormat;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * Type for the Time class which serializes to an xs:time.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class TimeType
    extends Type
{
    private static XsTimeFormat format = new XsTimeFormat();
    
    public Object readObject(MessageReader reader, MessageContext context) throws XFireFault
    {
        String value = reader.getValue();
        
        if (value == null) return null;
        
        try
        {
            Calendar c = (Calendar) format.parseObject(value);
            return new Time(c.getTimeInMillis());
        }
        catch (ParseException e)
        {
            throw new XFireFault("Could not parse xs:dateTime: " + e.getMessage(), e, XFireFault.SENDER);
        }
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        Calendar c = Calendar.getInstance();
        c.setTime((Time) object);
        writer.writeValue(format.format(c));
    }
}
