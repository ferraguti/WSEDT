package org.grails.xfire.aegis;

import javax.xml.namespace.QName;

import org.codehaus.xfire.soap.SoapConstants;

/**
 * Basic type conversion functionality for writing messages.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public abstract class AbstractMessageWriter
    implements MessageWriter
{
    /**
     * Create a LiteralWriter but without writing an element name.
     * 
     * @param writer
     */
    public AbstractMessageWriter()
    {
    }

    public void writeXsiType(QName type)
    {

        /*
         * Do not assume that the prefix supplied with the QName should be used
         * in this case.
         */
        String prefix = getPrefixForNamespace(type.getNamespaceURI(), type.getPrefix());
        String value;
        if (prefix != null && prefix.length() > 0)
        {
            StringBuffer sb = new StringBuffer(prefix.length() + 1 + type.getLocalPart().length());
            sb.append(prefix);
            sb.append(':');
            sb.append(type.getLocalPart());
            value = sb.toString();
        }
        else
        {
            value = type.getLocalPart();
        }
        getAttributeWriter("type", SoapConstants.XSI_NS).writeValue(value);
    }

    public void writeXsiNil()
    {
        MessageWriter attWriter = getAttributeWriter("nil", SoapConstants.XSI_NS);
        attWriter.writeValue("true");
        attWriter.close();
    }

    /**
     * @see org.grails.xfire.aegis.MessageWriter#writeValueAsInt(java.lang.Integer)
     */
    public void writeValueAsInt(Integer i)
    {
        writeValue(i.toString());
    }

    /**
     * @see org.grails.xfire.aegis.MessageWriter#writeValueAsDouble(java.lang.Double)
     */
    public void writeValueAsDouble(Double d)
    {
        writeValue(d.toString());
    }

    /**
     * @see org.grails.xfire.aegis.MessageWriter#writeValueAsCharacter(java.lang.Character)
     */
    public void writeValueAsCharacter(Character char1)
    {
        writeValue(char1.toString());
    }

    /**
     * @see org.grails.xfire.aegis.MessageWriter#writeValueAsLong(java.lang.Long)
     */
    public void writeValueAsLong(Long l)
    {
        writeValue(l.toString());
    }

    /**
     * @see org.grails.xfire.aegis.MessageWriter#writeValueAsFloat(java.lang.Float)
     */
    public void writeValueAsFloat(Float f)
    {
        writeValue(f.toString());
    }

    /**
     * @see org.grails.xfire.aegis.MessageWriter#writeValueAsBoolean(boolean)
     */
    public void writeValueAsBoolean(boolean b)
    {
        writeValue(b ? "true" : "false");
    }

    public void writeValueAsShort(Short s)
    {
        writeValue(s.toString());
    }
}
