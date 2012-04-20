package org.grails.xfire.aegis.type.collection;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.soap.SoapConstants;
import org.codehaus.xfire.util.NamespaceHelper;
import org.grails.xfire.aegis.AegisBindingProvider;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;
import org.jdom.Attribute;
import org.jdom.Element;

public class MapType
    extends Type
{
    private Type keyType;
    private Type valueType;
    private QName keyName;
    private QName valueName;
    private QName entryName;
    
    public MapType(QName schemaType, Type keyType, Type valueType)
    {
        super();
        
        this.keyType = keyType;
        this.valueType = valueType;
        
        setSchemaType(schemaType);
        setKeyName(new QName(schemaType.getNamespaceURI(), "key"));
        setValueName(new QName(schemaType.getNamespaceURI(), "value"));
        setEntryName(new QName(schemaType.getNamespaceURI(), "entry"));
    }

    public Object readObject(MessageReader reader, MessageContext context)
        throws XFireFault
    {
        Map map = instantiateMap();
        try
        {
            Type keyType = getKeyType();
            Type valueType = getValueType();

            Object key = null;
            Object value = null;
            
            while (reader.hasMoreElementReaders())
            {
                MessageReader entryReader = reader.getNextElementReader();
                
                if (entryReader.getName().equals(getEntryName()))
                {
                    while (entryReader.hasMoreElementReaders())
                    {
                        MessageReader evReader = entryReader.getNextElementReader();
                       
                        if (evReader.getName().equals(getKeyName()))
                        {
                            key = keyType.readObject(evReader, context);
                        }
                        else if (evReader.getName().equals(getValueName()))
                        {
                            value = valueType.readObject(evReader, context);
                        }
                        else
                        {
                            readToEnd(evReader);
                        }
                    }
                    
                    map.put(key, value);
                }
                else
                {
                    readToEnd(entryReader);
                }
            }
            
            return map;
        }
        catch (IllegalArgumentException e)
        {
            throw new XFireRuntimeException("Illegal argument.", e);
        }
    }

    private void readToEnd(MessageReader childReader)
    {
        while (childReader.hasMoreElementReaders())
        {
            readToEnd(childReader.getNextElementReader());
        }
    }
    
    /**
     * Creates a map instance. If the type class is a <code>Map</code> or extends
     * the <code>Map</code> interface a <code>HashMap</code> is created. Otherwise
     * the map classs (i.e. LinkedHashMap) is instantiated using the default constructor.
     * 
     * @return
     */
    protected Map instantiateMap()
    {
        Map map = null;
        
        if (getTypeClass().equals(Map.class))
        {
            map = new HashMap();
        }
        else if (getTypeClass().equals(Hashtable.class))
        {
            map = new Hashtable();
        }
        else if(getTypeClass().isInterface())
        {
            map = new HashMap();
        }
        else
        {
            try
            {
                map = (Map) getTypeClass().newInstance();
            }
            catch (Exception e)
            {
                throw new XFireRuntimeException(
                    "Could not create map implementation: " + getTypeClass().getName(), e);
            }
        }
        
        return map;
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        if (object == null)
            return;
    
        try
        {
            Map map = (Map) object;

            Type keyType = getKeyType();
            Type valueType = getValueType();
            
            for (Iterator itr = map.entrySet().iterator(); itr.hasNext();)
            {
                Map.Entry entry = (Map.Entry) itr.next();
                
                writeEntry(writer, context, keyType, valueType, entry);
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new XFireRuntimeException("Illegal argument.", e);
        }
    }

    private void writeEntry(MessageWriter writer, MessageContext context, Type keyType, Type valueType, Map.Entry entry)
        throws XFireFault
    {
        keyType = AegisBindingProvider.getWriteType(context, entry.getKey(), keyType);
        valueType = AegisBindingProvider.getWriteType(context, entry.getValue(), valueType);
        
        MessageWriter entryWriter = writer.getElementWriter(getEntryName());

        MessageWriter keyWriter = entryWriter.getElementWriter(getKeyName());
        keyType.writeObject(entry.getKey(), keyWriter, context);
        keyWriter.close();
        
        MessageWriter valueWriter = entryWriter.getElementWriter(getValueName());
        valueType.writeObject(entry.getValue(), valueWriter, context);
        valueWriter.close();
        
        entryWriter.close();
    }    

    public void writeSchema(Element root)
    {
        Element complex = new Element("complexType", SoapConstants.XSD_PREFIX, SoapConstants.XSD);
        complex.setAttribute(new Attribute("name", getSchemaType().getLocalPart()));
        root.addContent(complex);

        Element seq = new Element("sequence", SoapConstants.XSD_PREFIX, SoapConstants.XSD);
        complex.addContent(seq);

        Type keyType = getKeyType();
        Type valueType = getValueType();

        Element element = new Element("element", SoapConstants.XSD_PREFIX, SoapConstants.XSD);
        seq.addContent(element);

        element.setAttribute(new Attribute("name", getEntryName().getLocalPart()));
        element.setAttribute(new Attribute("minOccurs", "0"));
        element.setAttribute(new Attribute("maxOccurs", "unbounded"));
        
        Element evComplex = new Element("complexType", SoapConstants.XSD_PREFIX, SoapConstants.XSD);
        element.addContent(evComplex);
        
        Element evseq = new Element("sequence", SoapConstants.XSD_PREFIX, SoapConstants.XSD);
        evComplex.addContent(evseq);
        
        createElement(root, evseq, getKeyName(), keyType);
        createElement(root, evseq, getValueName(), valueType);
    }

    /**
     * Creates a element in a sequence for the key type and the value type.
     */
    private void createElement(Element root, Element seq, QName name, Type type)
    {
        Element element = new Element("element", SoapConstants.XSD_PREFIX, SoapConstants.XSD);
        seq.addContent(element);

        String prefix = NamespaceHelper.getUniquePrefix((Element) root.getParent(), 
                                                            type.getSchemaType().getNamespaceURI());
        String typeName = prefix + ":" + type.getSchemaType().getLocalPart();
                                                        
        element.setAttribute(new Attribute("name", name.getLocalPart()));
        element.setAttribute(new Attribute("type", typeName));

        element.setAttribute(new Attribute("minOccurs", "0"));
        element.setAttribute(new Attribute("maxOccurs", "1"));
    }
    
    public Set getDependencies()
    {
        HashSet deps = new HashSet();
        deps.add(getKeyType());
        deps.add(getValueType());
        return deps;
    }

    public Type getKeyType()
    {
        return keyType;
    }

    public Type getValueType()
    {
        return valueType;
    }

    public boolean isComplex()
    {
        return true;
    }

    public QName getKeyName()
    {
        return keyName;
    }

    public void setKeyName(QName keyName)
    {
        this.keyName = keyName;
    }

    public QName getValueName()
    {
        return valueName;
    }

    public void setValueName(QName valueName)
    {
        this.valueName = valueName;
    }

    public QName getEntryName()
    {
        return entryName;
    }

    public void setEntryName(QName entryName)
    {
        this.entryName = entryName;
    }
}