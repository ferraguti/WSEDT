package org.grails.xfire.aegis.type.basic;

import org.codehaus.xfire.MessageContext;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;


/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class FloatType
    extends Type
{
 
    public Object readObject(MessageReader reader, MessageContext context)
    {
        return new Float( reader.getValueAsFloat() );
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        if (object instanceof Float)
        {
            writer.writeValueAsFloat((Float) object);
        }
        else
        {
            writer.writeValueAsFloat(new Float(((Number)object).floatValue()));
        }
    }
}
