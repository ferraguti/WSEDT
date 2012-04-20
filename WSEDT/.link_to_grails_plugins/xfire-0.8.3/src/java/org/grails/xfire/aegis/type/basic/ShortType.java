package org.grails.xfire.aegis.type.basic;

import org.codehaus.xfire.MessageContext;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class ShortType
    extends Type
{
    public Object readObject(MessageReader reader, MessageContext context)
    {
        return new Short( reader.getValue() );
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        if (object instanceof Short)
        {
            writer.writeValueAsShort( (Short) object );
        }
        else
        {
            writer.writeValueAsShort(new Short(((Number)object).shortValue()));
        }
    }
}
