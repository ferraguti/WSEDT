package org.grails.xfire.aegis.type.basic;

import org.codehaus.xfire.MessageContext;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * SimpleSerializer
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class IntType
    extends Type
{
    public Object readObject(MessageReader reader, MessageContext context)
    {
        return new Integer( reader.getValueAsInt() );
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        if (object instanceof Integer)
        {
            writer.writeValueAsInt( (Integer) object );
        }
        else
        {
            writer.writeValueAsInt(new Integer(((Number)object).intValue()));
        }
    }
}
