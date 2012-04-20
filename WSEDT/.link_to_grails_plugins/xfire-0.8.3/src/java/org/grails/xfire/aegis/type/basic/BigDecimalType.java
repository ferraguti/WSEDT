package org.grails.xfire.aegis.type.basic;

import java.math.BigDecimal;

import org.codehaus.xfire.MessageContext;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * <code>Type</code> for a <code>BigDecimal</code>
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class BigDecimalType extends Type
{
    public BigDecimalType()
    {
        super();
    }

    public Object readObject( final MessageReader reader, final MessageContext context )
    {
        final String value = reader.getValue();

        return null == value ? null : new BigDecimal( value );
    }

    public void writeObject( final Object object, final MessageWriter writer, final MessageContext context )
    {
        writer.writeValue( object.toString() );
    }
}
