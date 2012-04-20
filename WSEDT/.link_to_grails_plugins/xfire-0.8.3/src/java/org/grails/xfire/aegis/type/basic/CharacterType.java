package org.grails.xfire.aegis.type.basic;

import org.codehaus.xfire.MessageContext;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * @author <a href="mailto:struman@nuparadigm.com">Sean Truman</a>
 */
public class CharacterType extends Type
{    
    public Object readObject(MessageReader reader, MessageContext context)
    {
        return new Character( reader.getValueAsCharacter() );
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
    {
        writer.writeValueAsCharacter((Character)object);
    }
    
}