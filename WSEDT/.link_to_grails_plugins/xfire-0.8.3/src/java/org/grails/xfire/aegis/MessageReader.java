package org.grails.xfire.aegis;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

/**
 * A MessageReader. You must call getNextChildReader() until hasMoreChildReaders()
 * returns false.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public interface MessageReader
{
    public String getValue();

    public boolean isXsiNil();
    
    public int getValueAsInt();

    public long getValueAsLong();

    public double getValueAsDouble();

    public float getValueAsFloat();
    
    public boolean getValueAsBoolean();

    public char getValueAsCharacter();
    
    public MessageReader getAttributeReader( QName qName );

    public boolean hasMoreAttributeReaders();
    
    public MessageReader getNextAttributeReader();
    
    public boolean hasMoreElementReaders();
    
    public MessageReader getNextElementReader();
    
    public QName getName();
    
    /**
     * Get the local name of the element this reader represents.
     * @return Local Name
     */
    public String getLocalName();

    /**
     * @return Namespace
     */
    public String getNamespace();

    public String getNamespaceForPrefix( String prefix );

    public XMLStreamReader getXMLStreamReader();

    public void readToEnd();
}
