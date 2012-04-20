package org.grails.xfire.aegis.type.basic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

/**
 * An ArrayType.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public class ArrayType
    extends Type
{
    private QName componentName;
    private static final Log logger = LogFactory.getLog(ArrayType.class);
    private long minOccurs = 0;
    private long maxOccurs = Long.MAX_VALUE;
    private boolean flat;
    
    public ArrayType()
    {
    }
    
    public Object readObject(MessageReader reader, MessageContext context)
        throws XFireFault
    {
        try
        {
            Collection values = readCollection(reader, context);
            
            return makeArray(getComponentType().getTypeClass(), values);
        }
        catch (IllegalArgumentException e)
        {
            throw new XFireRuntimeException("Illegal argument.", e);
        }
    }
    
    protected Collection createCollection()
    {
        return new ArrayList();
    }
    
    protected Collection readCollection(MessageReader reader, MessageContext context)
        throws XFireFault
    {
        Collection values = createCollection();
        
        while ( reader.hasMoreElementReaders() )
        {
            MessageReader creader = reader.getNextElementReader();
            Type compType = AegisBindingProvider.getReadType(creader.getXMLStreamReader(), context, getComponentType());
            
            if (creader.isXsiNil())
            {    
                values.add(null);
                creader.readToEnd();
            }
            else
            {
                values.add( compType.readObject(creader, context) );
            }

            // check max occurs
            int size = values.size();
            if (size > maxOccurs)
                throw new XFireFault("The number of elements in " + getSchemaType() + 
                                     " exceeds the maximum of " + maxOccurs, XFireFault.SENDER);
            
        }

        // check min occurs
        if (values.size() < minOccurs)
            throw new XFireFault("The number of elements in " + getSchemaType() + 
                                 " does not meet the minimum of " + minOccurs, XFireFault.SENDER);
        return values;
    }

    protected Object makeArray(Class arrayType, Collection values)
    {
        if (Integer.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Integer.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        else if (Long.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Long.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        else if (Short.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Short.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        else if (Double.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Double.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        else if (Float.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Float.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        else if (Byte.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Byte.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        else if (Boolean.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Boolean.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        else if (Character.TYPE.equals(arrayType))
        {
            Object[] objects = values.toArray();
            Object array = Array.newInstance(Character.TYPE, objects.length);
            for (int i = 0, n = objects.length; i < n; i++)
            {
                Array.set(array, i, objects[i]);
            }
            return array;
        }
        return values.toArray( (Object[]) Array.newInstance( getComponentType().getTypeClass(), 
                                                             values.size()) );
    }

    public void writeObject(Object values, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        if (values == null)
            return;

        Type type = getComponentType();

        String ns = null;
        if (type.isAbstract())
            ns = getSchemaType().getNamespaceURI();
        else
            ns = type.getSchemaType().getNamespaceURI();
        
        String name = type.getSchemaType().getLocalPart();

        if ( type == null )
            throw new XFireRuntimeException( "Couldn't find type for " + type.getTypeClass() + "." );

        Class arrayType = type.getTypeClass();
        
        if (Object.class.isAssignableFrom(arrayType))
        {
            Object[] objects = (Object[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(objects[i], writer, context, type, name, ns);
            }
        }
        else if (Integer.TYPE.equals(arrayType))
        {
            int[] objects = (int[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Integer(objects[i]), writer, context, type, name, ns);
            }
        }
        else if (Long.TYPE.equals(arrayType))
        {
            long[] objects = (long[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Long(objects[i]), writer, context, type, name, ns);
            }
        }
        else if (Short.TYPE.equals(arrayType))
        {
            short[] objects = (short[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Short(objects[i]), writer, context, type, name, ns);
            }
        }
        else if (Double.TYPE.equals(arrayType))
        {
            double[] objects = (double[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Double(objects[i]), writer, context, type, name, ns);
            }
        }
        else if (Float.TYPE.equals(arrayType))
        {
            float[] objects = (float[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Float(objects[i]), writer, context, type, name, ns);
            }
        }
        else if (Byte.TYPE.equals(arrayType))
        {
            byte[] objects = (byte[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Byte(objects[i]), writer, context, type, name, ns);
            }
        }
        else if (Boolean.TYPE.equals(arrayType))
        {
            boolean[] objects = (boolean[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Boolean(objects[i]), writer, context, type, name, ns);
            }
        }
        else if (Character.TYPE.equals(arrayType))
        {
            char[] objects = (char[]) values;
            for (int i = 0, n = objects.length; i < n; i++)
            {
                writeValue(new Character(objects[i]), writer, context, type, name, ns);
            }
        }        
    }

    protected void writeValue(Object value, 
                              MessageWriter writer, 
                              MessageContext context, 
                              Type type,
                              String name,
                              String ns) 
        throws XFireFault
    {
        type = AegisBindingProvider.getWriteType(context, value, type);
        MessageWriter cwriter;
        if (type.isWriteOuter()) {
        	cwriter = writer.getElementWriter(name, ns);
        } else {
        	cwriter = writer;
        }
        
        if (value==null && type.isNillable())
            cwriter.writeXsiNil();
        else
            type.writeObject( value, cwriter, context );

        cwriter.close();
    }
    
    public void writeSchema(Element root)
    {
        try
        {
            Element complex = new Element("complexType",
                                          SoapConstants.XSD_PREFIX,
                                          SoapConstants.XSD);
            complex.setAttribute(new Attribute("name", getSchemaType().getLocalPart()));
            root.addContent(complex);

            Element seq = new Element("sequence",
                                      SoapConstants.XSD_PREFIX,
                                      SoapConstants.XSD);
            complex.addContent(seq);
            
            Element element = new Element("element",
                                          SoapConstants.XSD_PREFIX,
                                          SoapConstants.XSD);
            seq.addContent(element);

            Type componentType = getComponentType();
            String prefix = NamespaceHelper.getUniquePrefix((Element) root.getParent(), 
                                                            componentType.getSchemaType().getNamespaceURI());

            String typeName = prefix + ":"
                    + componentType.getSchemaType().getLocalPart();

            element.setAttribute(new Attribute("name", componentType.getSchemaType().getLocalPart()));
            element.setAttribute(new Attribute("type", typeName));
            
            if (componentType.isNillable())
            {
                element.setAttribute(new Attribute("nillable", "true"));
            }

            element.setAttribute(new Attribute("minOccurs", new Long(getMinOccurs()).toString()));
            
            if (maxOccurs == Long.MAX_VALUE)
                element.setAttribute(new Attribute("maxOccurs", "unbounded"));
            else
                element.setAttribute(new Attribute("maxOccurs", new Long(getMaxOccurs()).toString()));
            
        }
        catch (IllegalArgumentException e)
        {
            throw new XFireRuntimeException("Illegal argument.", e);
        }
    }
    
    /**
     * We need to write a complex type schema for Beans, so return true.
     * 
     * @see org.grails.xfire.aegis.type.Type#isComplex()
     */
    public boolean isComplex()
    {
        return true;
    }
    
    public QName getComponentName()
    {
        return componentName;
    }
    
    public void setComponentName(QName componentName)
    {
        this.componentName = componentName;
    }

    /**
     * @see org.grails.xfire.aegis.type.Type#getDependencies()
     */
    public Set getDependencies()
    {
        Set deps = new HashSet();
        
        deps.add( getComponentType() );
        
        return deps;
    }
    
    /**
     * Get the <code>Type</code> of the elements in the array.
     * 
     * @return
     */
    public Type getComponentType()
    {
        Class compType = getTypeClass().getComponentType();
        
        Type type;
        
        if (componentName == null)
        {
            type = getTypeMapping().getType(compType);
        }
        else
        {
            type = getTypeMapping().getType(componentName);
            
            // We couldn't find the type the user specified. One is created below instead.
            if (type == null)
            {
                logger.debug("Couldn't find array component type " 
                             + componentName + ". Creating one instead.");
            }
        }
        
        if (type == null)
        {
            type = getTypeMapping().getTypeCreator().createType(compType);
            getTypeMapping().register(type);
        }
        
        return type;
    }

    public long getMaxOccurs()
    {
        return maxOccurs;
    }

    public void setMaxOccurs(long maxOccurs)
    {
        this.maxOccurs = maxOccurs;
    }

    public long getMinOccurs()
    {
        return minOccurs;
    }

    public void setMinOccurs(long minOccurs)
    {
        this.minOccurs = minOccurs;
    }

    public boolean isFlat()
    {
        return flat;
    }

    public void setFlat(boolean flat)
    {
        setWriteOuter(!flat);
        this.flat = flat;
    }
}
