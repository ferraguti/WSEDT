package org.grails.xfire.aegis.type.basic;

import org.codehaus.xfire.MessageContext;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/** 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class LongType
    extends Type
{
    public Object readObject(MessageReader reader, MessageContext context)
    {
        return new Long( reader.getValueAsLong() );
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        if (object instanceof Long)
        {
            writer.writeValueAsLong( (Long) object );
        }
        else
        {
            writer.writeValueAsLong(new Long(((Number)object).longValue()));
        }
    }
}
