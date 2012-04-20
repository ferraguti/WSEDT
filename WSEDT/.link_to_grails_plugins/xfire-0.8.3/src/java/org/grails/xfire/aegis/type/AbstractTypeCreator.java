package org.grails.xfire.aegis.type;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.xml.namespace.QName;

import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.util.NamespaceHelper;
import org.codehaus.xfire.util.ServiceUtils;
import org.grails.xfire.aegis.Holder;
import org.grails.xfire.aegis.type.basic.ArrayType;
import org.grails.xfire.aegis.type.basic.HolderType;
import org.grails.xfire.aegis.type.basic.ObjectType;
import org.grails.xfire.aegis.type.collection.CollectionType;
import org.grails.xfire.aegis.type.collection.MapType;

/**
 * @author Hani Suleiman Date: Jun 14, 2005 Time: 11:59:57 PM
 */
public abstract class AbstractTypeCreator
    implements TypeCreator
{
    protected TypeMapping tm;

    protected AbstractTypeCreator nextCreator;

    private Configuration typeConfiguration;

	private TypeCreator parent;
    
    public TypeMapping getTypeMapping()
    {
        return tm;
    }
    
    public TypeCreator getTopCreator() {
    	TypeCreator top = this;
    	TypeCreator next = top;
    	while (next != null) {
    		top = next;
    		next = top.getParent();
    	}
    	return top;
	}

    
    public TypeCreator getParent() {
		return parent;
	}

	public void setParent(TypeCreator parent) {
		this.parent = parent;
	}

	public void setTypeMapping(TypeMapping typeMapping)
    {
        this.tm = typeMapping;

        if (nextCreator != null)
            nextCreator.setTypeMapping(tm);
    }

    public void setNextCreator(AbstractTypeCreator creator)
    {
        this.nextCreator = creator;
        nextCreator.parent = this;
    }

    protected TypeClassInfo createClassInfo(Field f)
    {
        TypeClassInfo info = createBasicClassInfo(f.getType());
        info.setDescription("field " + f.getName() + " in  " + f.getDeclaringClass());
        return info;
    }

    public TypeClassInfo createBasicClassInfo(Class typeClass)
    {
        TypeClassInfo info = new TypeClassInfo();
        info.setDescription("class '" + typeClass.getName() + '\'');
        info.setTypeClass(typeClass);

        return info;
    }

    public Type createTypeForClass(TypeClassInfo info)
    {
        Class javaType = info.getTypeClass();
        Type result = null;
        boolean newType = true;
        
        if (info.getType() != null)
        {
            result = createUserType(info);
        }
        else if (isHolder(javaType))
        {
            result = createHolderType(info);
        }
        else if (isArray(javaType))
        {
            result = createArrayType(info);
        }
        else if (isMap(javaType))
        {
            result = createMapType(info);
        }
        else if (isCollection(javaType))
        {
            result = createCollectionType(info);
        }
        else if (isEnum(javaType))
        {
            result = createEnumType(info);
        }
        else
        {
            Type type = getTypeMapping().getType(javaType);
            if (type == null)
            {
                type = createDefaultType(info);
            }
            else
            {
                newType = false;
            }
            
            result = type;
        }

        
        if (newType && !getConfiguration().isDefaultNillable())
            result.setNillable(false);
        
        return result;
    }

    protected boolean isHolder(Class javaType)
    {
        return javaType.equals(Holder.class);
    }

    protected Type createHolderType(TypeClassInfo info)
    {
        if (info.getGenericType() == null)
        {
            throw new UnsupportedOperationException("To use holder types "
                    + "you must have an XML descriptor declaring the component type.");
        }

        Class heldCls = (Class) info.getGenericType();
        info.setTypeClass(heldCls);

        Type delegate = createType(heldCls);
        HolderType type = new HolderType(delegate);
        return type;
    }

    protected boolean isArray(Class javaType)
    {
        return javaType.isArray() && !javaType.equals(byte[].class);
    }

    protected Type createUserType(TypeClassInfo info)
    {
        try
        {
            Type type = (Type) info.getType().newInstance();

            QName name = info.getTypeName();
            if (name == null)
                name = createQName(info.getTypeClass());

            type.setSchemaType(name);
            type.setTypeClass(info.getTypeClass());
            type.setTypeMapping(getTypeMapping());

            return type;
        }
        catch (InstantiationException e)
        {
            throw new XFireRuntimeException("Couldn't instantiate type classs "
                    + info.getType().getName(), e);
        }
        catch (IllegalAccessException e)
        {
            throw new XFireRuntimeException("Couldn't access type classs "
                    + info.getType().getName(), e);
        }
    }

    protected Type createArrayType(TypeClassInfo info)
    {
        ArrayType type = new ArrayType();
        type.setTypeMapping(getTypeMapping());
        type.setTypeClass(info.getTypeClass());
        type.setSchemaType(createCollectionQName(info, type.getComponentType()));
        
        if (info.getMinOccurs() != -1) type.setMinOccurs(info.getMinOccurs());
        if (info.getMaxOccurs() != -1) type.setMaxOccurs(info.getMaxOccurs());
        
        type.setFlat(info.isFlat());
        
        return type;
    }
    
    protected QName createQName(Class javaType)
    {
        String clsName = javaType.getName();

        String ns = NamespaceHelper.makeNamespaceFromClassName(clsName, "http");
        String localName = ServiceUtils.makeServiceNameFromClassName(javaType);

        return new QName(ns, localName);
    }

    protected boolean isCollection(Class javaType)
    {
        return Collection.class.isAssignableFrom(javaType);
    }

    protected Type createCollectionTypeFromGeneric(TypeClassInfo info)
    {
        Type component = getOrCreateGenericType(info);
        
        CollectionType type = new CollectionType(component);
        type.setTypeMapping(getTypeMapping());

        QName name = info.getTypeName();
        if (name == null)
            name = createCollectionQName(info, component);
        
        type.setSchemaType(name);

        type.setTypeClass(info.getTypeClass());
        
        if (info.getMinOccurs() != -1) type.setMinOccurs(info.getMinOccurs());
        if (info.getMaxOccurs() != -1) type.setMaxOccurs(info.getMaxOccurs());
        
        type.setFlat(info.isFlat());
        
        return type;
    }

    protected Type getOrCreateGenericType(TypeClassInfo info)
    {
        return createObjectType();
    }

    protected Type getOrCreateMapKeyType(TypeClassInfo info)
    {
        return createObjectType();
    }

    private Type createObjectType()
    {
        ObjectType type = new ObjectType();
        type.setSchemaType(DefaultTypeMappingRegistry.XSD_ANY);
        type.setTypeClass(Object.class);
        type.setTypeMapping(getTypeMapping());
        return type;
    }

    protected Type getOrCreateMapValueType(TypeClassInfo info)
    {
        return createObjectType();
    }
    
    protected Type createMapType(TypeClassInfo info, Type keyType, Type valueType)
    {
        QName schemaType = createMapQName(info, keyType, valueType);
        MapType type = new MapType(schemaType, keyType, valueType);
        type.setTypeMapping(getTypeMapping());
        type.setTypeClass(info.getTypeClass());

        return type;
    }

    protected Type createMapType(TypeClassInfo info)
    {
        Type keyType = getOrCreateMapKeyType(info);
        Type valueType = getOrCreateMapValueType(info);

        return createMapType(info, keyType, valueType);
    }

    protected QName createMapQName(TypeClassInfo info, Type keyType, Type valueType)
    {
        String name = keyType.getSchemaType().getLocalPart() + '2'
                + valueType.getSchemaType().getLocalPart() + "Map";

        // TODO: Get namespace from XML?
        return new QName(tm.getEncodingStyleURI(), name);
    }

    protected boolean isMap(Class javaType)
    {
        return Map.class.isAssignableFrom(javaType);
    }

    public abstract TypeClassInfo createClassInfo(PropertyDescriptor pd);

    protected boolean isEnum(Class javaType)
    {
        return false;
    }

    public Type createEnumType(TypeClassInfo info)
    {
        return null;
    }

    public abstract Type createCollectionType(TypeClassInfo info);

    public abstract Type createDefaultType(TypeClassInfo info);

    protected QName createCollectionQName(TypeClassInfo info, Type type)
    {
        String ns;

        if (type.isComplex())
        {
            ns = type.getSchemaType().getNamespaceURI();
        }
        else
        {
            ns = tm.getEncodingStyleURI();
        }

        String first = type.getSchemaType().getLocalPart().substring(0, 1);
        String last = type.getSchemaType().getLocalPart().substring(1);
        String localName = "ArrayOf" + first.toUpperCase() + last;

        return new QName(ns, localName);
    }

    public abstract TypeClassInfo createClassInfo(Method m, int index);

    /**
     * Create a Type for a Method parameter.
     * 
     * @param m
     *            the method to create a type for
     * @param index
     *            The parameter index. If the index is less than zero, the
     *            return type is used.
     */
    public Type createType(Method m, int index)
    {
        TypeClassInfo info = createClassInfo(m, index);
        info.setDescription((index == -1 ? "return type" : "parameter " + index) + " of method "
                + m.getName() + " in " + m.getDeclaringClass());
        return createTypeForClass(info);
    }
    
    public QName getElementName(Method m, int index)
    {
        TypeClassInfo info = createClassInfo(m, index);
        
        return info.getMappedName();
    }

    /**
     * Create type information for a PropertyDescriptor.
     * 
     * @param pd
     *            the propertydescriptor
     */
    public Type createType(PropertyDescriptor pd)
    {
        TypeClassInfo info = createClassInfo(pd);
        info.setDescription("property " + pd.getName());
        return createTypeForClass(info);
    }

    /**
     * Create type information for a <code>Field</code>.
     * 
     * @param f
     *            the field to create a type from
     */
    public Type createType(Field f)
    {
        TypeClassInfo info = createClassInfo(f);
        info.setDescription("field " + f.getName() + " in " + f.getDeclaringClass());
        return createTypeForClass(info);
    }

    public Type createType(Class clazz)
    {
        TypeClassInfo info = createBasicClassInfo(clazz);
        info.setDescription(clazz.toString());
        return createTypeForClass(info);
    }

    public Configuration getConfiguration()
    {
        return typeConfiguration;
    }

    public void setConfiguration(Configuration typeConfiguration)
    {
        this.typeConfiguration = typeConfiguration;
    }
    
    public static class TypeClassInfo
    {
        Class typeClass;

        Object[] annotations;

        Object genericType;

        Object keyType;

        QName mappedName;

        QName typeName;

        Class type;

        String description;

        long minOccurs = -1;
        long maxOccurs = -1;
        boolean flat = false;
        
        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public Object[] getAnnotations()
        {
            return annotations;
        }

        public void setAnnotations(Object[] annotations)
        {
            this.annotations = annotations;
        }

        public Object getGenericType()
        {
            return genericType;
        }

        public void setGenericType(Object genericType)
        {
            this.genericType = genericType;
        }

        public Object getKeyType()
        {
            return keyType;
        }

        public void setKeyType(Object keyType)
        {
            this.keyType = keyType;
        }

        public Class getTypeClass()
        {
            return typeClass;
        }

        public void setTypeClass(Class typeClass)
        {
            this.typeClass = typeClass;
        }

        public QName getTypeName()
        {
            return typeName;
        }

        public void setTypeName(QName name)
        {
            this.typeName = name;
        }

        public Class getType()
        {
            return type;
        }

        public void setType(Class type)
        {
            this.type = type;
        }

        public QName getMappedName()
        {
            return mappedName;
        }

        public void setMappedName(QName mappedName)
        {
            this.mappedName = mappedName;
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
            this.flat = flat;
        }
    }
}
