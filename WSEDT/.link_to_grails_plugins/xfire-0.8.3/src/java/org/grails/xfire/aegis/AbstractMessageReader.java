package org.grails.xfire.aegis;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.soap.SoapConstants;

/**
 * Basic type conversions for reading messages.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public abstract class AbstractMessageReader
    implements MessageReader
{
    private static final QName XSI_NIL = new QName(SoapConstants.XSI_NS, "nil", SoapConstants.XSI_PREFIX);
    
    public AbstractMessageReader()
    {
    }

    public void readToEnd()
    {
        readToEnd(this);
    }
    
    private void readToEnd(MessageReader childReader)
    {
        while (childReader.hasMoreElementReaders())
        {
            readToEnd(childReader.getNextElementReader());
        }
    }
    
    public boolean isXsiNil()
    {
        MessageReader nilReader = getAttributeReader(XSI_NIL);
        boolean nil = false;
        if (nilReader != null)
        {
            String value = nilReader.getValue();
            if (value != null && (value.equals("true") || value.equals("1")))
                return true;
        }
        
        return nil;
    }

    public boolean hasValue()
    {
        return getValue() != null;
    }

    /**
     * @see org.codehaus.xfire.aegis.MessageReader#getValueAsCharacter()
     */
    public char getValueAsCharacter()
    {
        if (getValue() == null) return 0;
        return getValue().charAt(0);
    }

    public int getValueAsInt()
    {
        if (getValue() == null) return 0;
        
        return Integer.parseInt( getValue() );
    }

	/**
	 * @see org.codehaus.xfire.aegis.MessageReader#getValueAsLong()
	 */
	public long getValueAsLong()
	{
        if (getValue() == null) return 0l;
        
        return Long.parseLong( getValue() );
	}
    
	/**
	 * @see org.codehaus.xfire.aegis.MessageReader#getValueAsDouble()
	 */
	public double getValueAsDouble()
	{
        if (getValue() == null) return 0d;
        
        return Double.parseDouble( getValue() );
	}

	/**
	 * @see org.codehaus.xfire.aegis.MessageReader#getValueAsFloat()
	 */
	public float getValueAsFloat()
	{
        if (getValue() == null) return 0f;
        
        return Float.parseFloat( getValue() );
	}

	/**
	 * @see org.codehaus.xfire.aegis.MessageReader#getValueAsBoolean()
	 */
	public boolean getValueAsBoolean()
	{
        String value = getValue();
        if (value == null) return false;
        
        if ("true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value))
            return true;

        if ("false".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value))
            return false;
        
        throw new XFireRuntimeException("Invalid boolean value: " + value);
	}

    public XMLStreamReader getXMLStreamReader()
    {
        throw new UnsupportedOperationException();
    }
}
