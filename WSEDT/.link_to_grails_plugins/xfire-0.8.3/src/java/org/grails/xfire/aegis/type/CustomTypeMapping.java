package org.grails.xfire.aegis.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contains type mappings for java/qname pairs.
 * 
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 * @since Feb 21, 2004
 */
public class CustomTypeMapping
    implements TypeMapping
{
    private static final Log LOG = LogFactory.getLog(CustomTypeMapping.class);
    
	private Map class2Type;

    private Map xml2Type;

    private Map class2xml;
    
    private TypeMapping defaultTM;
    
    private String encodingStyleURI;
    
    private TypeCreator typeCreator;
    
    public CustomTypeMapping( TypeMapping defaultTM )
    {
        this();

        this.defaultTM = defaultTM;
    }
    
    public CustomTypeMapping()
    {
        class2Type = Collections.synchronizedMap(new HashMap());
        class2xml = Collections.synchronizedMap(new HashMap());
        xml2Type = Collections.synchronizedMap(new HashMap());
    }

	public boolean isRegistered(Class javaType)
	{
        boolean registered = class2Type.containsKey(javaType);
        
        if ( !registered && defaultTM != null )
            registered = defaultTM.isRegistered(javaType);
        
        return registered;
	}

    public boolean isRegistered(QName xmlType)
    {
        boolean registered = xml2Type.containsKey(xmlType);
        
        if ( !registered && defaultTM != null )
            registered = defaultTM.isRegistered(xmlType);
        
        return registered;
    }

	public void register(Class javaType, QName xmlType, Type type)
    {
        type.setSchemaType(xmlType);
        type.setTypeClass(javaType);

        register(type);
    }

    public void register(Type type)
    {
        type.setTypeMapping(this);
        /*
         * -- prb@codehaus.org; changing this to only register the type for
         * actions that it supports, and it could be none.
         */
        if (type.getTypeClass() != null) {
            class2xml.put( type.getTypeClass(), type.getSchemaType() ); 
            class2Type.put( type.getTypeClass(), type );
        }
        if (type.getSchemaType() != null) {
            xml2Type.put( type.getSchemaType(), type );
        }
        if (type.getTypeClass() == null && type.getSchemaType() == null) {
            LOG.warn("The type " + type.getClass().getName()
                     + " supports neither serialization (non-null TypeClass) nor deserialization (non-null SchemaType).");
        }
    }

	public void removeType(Type type)
	{
        if (!xml2Type.containsKey(type.getSchemaType()))
        {
           defaultTM.removeType(type);
        }
        else
        {
            xml2Type.remove(type.getSchemaType());
            class2Type.remove(type.getTypeClass());
            class2xml.remove(type.getTypeClass());
        }
	}

    /**
     * @see org.grails.xfire.aegis.type.TypeMapping#getType(java.lang.Class)
     */
    public Type getType(Class javaType)
    {
        Type type = (Type) class2Type.get( javaType );

        if ( type == null && defaultTM != null )
        {
            type = defaultTM.getType( javaType );
        }
        
        return type;
    }

    /**
     * @see org.grails.xfire.aegis.type.TypeMapping#getType(javax.xml.namespace.QName)
     */
    public Type getType(QName xmlType)
    {
        Type type = (Type) xml2Type.get( xmlType );

        if ( type == null && defaultTM != null )
            type = defaultTM.getType( xmlType );
        
        return type;
    }

    /**
     * @see org.grails.xfire.aegis.type.TypeMapping#getTypeQName(java.lang.Class)
     */
    public QName getTypeQName(Class clazz)
    {
        QName qname = (QName) class2xml.get( clazz );

        if ( qname == null && defaultTM != null )
            qname = defaultTM.getTypeQName( clazz );
        
        return qname;
    }

    public String getEncodingStyleURI()
    {
        return encodingStyleURI;
    }
    
    public void setEncodingStyleURI( String encodingStyleURI )
    {
        this.encodingStyleURI = encodingStyleURI;
    }

    public TypeCreator getTypeCreator()
    {
        return typeCreator;
    }

    public void setTypeCreator(TypeCreator typeCreator)
    {
        this.typeCreator = typeCreator;
        
        typeCreator.setTypeMapping(this);
    }
    
    public TypeMapping getParent()
    {
        return defaultTM;
    }
}
