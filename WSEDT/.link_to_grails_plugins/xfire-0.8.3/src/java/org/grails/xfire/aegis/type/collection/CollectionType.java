package org.grails.xfire.aegis.type.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.fault.XFireFault;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;
import org.grails.xfire.aegis.type.basic.ArrayType;

public class CollectionType
    extends ArrayType
{
    private Type componentType;
    
    public CollectionType(Type componentType)
    {
        super();
        
        this.componentType = componentType;
    }

    public Object readObject(MessageReader reader, MessageContext context)
        throws XFireFault
    {
        try
        {
            return readCollection(reader, context);
        }
        catch (IllegalArgumentException e)
        {
            throw new XFireRuntimeException("Illegal argument.", e);
        }
    }

    protected Collection createCollection()
    {
        Collection values = null;
        
        if (getTypeClass().isAssignableFrom(List.class))
        {
            values = new ArrayList();
        }
        else if (getTypeClass().isAssignableFrom(Set.class))
        {
            values = new HashSet();
        }
        else if (getTypeClass().isAssignableFrom(Vector.class))
        {
            values = new Vector();
        }
        else if (getTypeClass().isInterface()) 
        {
            values = new ArrayList();
        }
        else
        {
            try
            {
                values = (Collection) getTypeClass().newInstance();
            }
            catch (Exception e)
            {
                throw new XFireRuntimeException(
                    "Could not create map implementation: " + getTypeClass().getName(), e);
            }
        }
        
        return values;
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        if (object == null)
            return;
    
        try
        {
            Collection list = (Collection) object;

            Type type = getComponentType();

            if (type == null)
                throw new XFireRuntimeException("Couldn't find type.");

            for (Iterator itr = list.iterator(); itr.hasNext();)
            {
                String ns = null;
                if (type.isAbstract())
                    ns = getSchemaType().getNamespaceURI();
                else
                    ns = type.getSchemaType().getNamespaceURI();

                writeValue(itr.next(), writer, context, type, type.getSchemaType().getLocalPart(), ns);
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new XFireRuntimeException("Illegal argument.", e);
        }
    }    
    
    public Type getComponentType()
    {
        return componentType;
    }
}