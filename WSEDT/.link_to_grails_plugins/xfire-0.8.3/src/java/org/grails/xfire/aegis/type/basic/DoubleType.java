package org.grails.xfire.aegis.type.basic;

import org.codehaus.xfire.MessageContext;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class DoubleType
    extends Type
{    
    public Object readObject(MessageReader reader, MessageContext context)
    {
        return new Double( reader.getValueAsDouble() );
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        if (object instanceof Double)
        {
            writer.writeValueAsDouble( (Double) object );
        }
        else
        {
            writer.writeValueAsDouble(new Double(((Number)object).doubleValue()));
        }
    }
}
