package org.grails.xfire.aegis.type.basic;

import groovy.lang.GroovyObject;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.codehaus.xfire.XFireRuntimeException;
import org.grails.xfire.ServiceFactoryBean;
import org.grails.xfire.aegis.type.Type;
import org.grails.xfire.aegis.type.TypeCreator;
import org.grails.xfire.aegis.type.TypeMapping;

public class BeanTypeInfo
{
    private Map mappedName2typeName = new HashMap();

    private Map mappedName2pdName = new HashMap();

    private Map mappedName2type = new HashMap();

    private Class beanClass;

    private List attributes = new ArrayList();

    private List elements = new ArrayList();

    private PropertyDescriptor[] descriptors;

    private TypeMapping typeMapping;

    private volatile boolean initialized;

    private String defaultNamespace;

    private int minOccurs = 0;

    private boolean nillable = true;

    private boolean isExtension = false;
    
    private List xmlTransients = new ArrayList();    

    /**
     * extensibleElements means adding xs:any to WSDL Complex Type Definition
     */
    private boolean extensibleElements = true;

    /**
     * extensibleAttributes means adding xs:anyAttribute to WSDL Complex Type
     * Definition
     */
    private boolean extensibleAttributes = true;
    
    private boolean isGrailsDomainClass;

    public BeanTypeInfo(Class typeClass, String defaultNamespace)
    {
        this.beanClass = typeClass;
        this.isGrailsDomainClass = ServiceFactoryBean.getApp().isArtefactOfType(DomainClassArtefactHandler.TYPE, beanClass);
        
        if(this.isGrailsDomainClass) {
            try { 
                xmlTransients = (List) beanClass.getMethod("getXmlTransients").invoke(null);         
            }
            catch(NoSuchMethodException e) {/* do nothing */}
            catch(Exception e) { 
                throw new XFireRuntimeException("Couldn't create BeanTypeInfo.", e);         
            }
        }
        this.defaultNamespace = defaultNamespace;

        initializeProperties();
    }

    /**
     * Create a BeanTypeInfo class.
     * 
     * @param typeClass
     * @param defaultNamespace
     * @param initiallize
     *            If true attempt default property/xml mappings.
     */
    public BeanTypeInfo(Class typeClass, String defaultNamespace, boolean initialize)
    {
        this.beanClass = typeClass;
        this.defaultNamespace = defaultNamespace;

        initializeProperties();
        initialized = !initialize;
    }

    public String getDefaultNamespace()
    {
        return defaultNamespace;
    }

    public void initialize()
    {
        try
        {
            if (!initialized)
            {
                synchronized (this)
                {
                    if (!initialized)
                    {
                        for (int i = 0; i < descriptors.length; i++)
                        {
                            // Don't map the property unless there is a read property
                            if (isMapped(descriptors[i]))
                            {
                                mapProperty(descriptors[i]);
                            }
                        }
                        initialized = true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            if (e instanceof XFireRuntimeException)
                throw (XFireRuntimeException) e;
            throw new XFireRuntimeException("Couldn't create TypeInfo.", e);
        }
    }

	// TODO here's where to modify
    public boolean isMapped(PropertyDescriptor pd)
    {
    	// System.out.println(">>" + beanClass + "#" + pd.getName());
    	if(isGrailsDomainClass) {
    		if(pd.getName().equals("metaClass")) return false;
    		if(pd.getName().equals("metaMethods")) return false;
    		if(xmlTransients.contains(pd.getName())) return false;
    	} 
    	if (GroovyObject.class.isAssignableFrom(beanClass)){
    		if(pd.getName().equals("metaClass")) return false;
    		if(pd.getName().equals("metaMethods")) return false;    		
    	}
        if (pd.getReadMethod() == null)
            return false;

        return true;
    }

    protected void mapProperty(PropertyDescriptor pd)
    {
        String name = pd.getName();

        if (isAttribute(pd))
        {
            mapAttribute(name, createMappedName(pd));
        }
        else if (isElement(pd))
        {
            mapElement(name, createMappedName(pd));
        }
    }

    protected PropertyDescriptor[] getPropertyDescriptors()
    {
        return descriptors;
    }

    protected PropertyDescriptor getPropertyDescriptor(String name)
    {
        for (int i = 0; i < descriptors.length; i++)
        {
            if (descriptors[i].getName().equals(name))
                return descriptors[i];
        }

        return null;
    }

    /**
     * Get the type class for the field with the specified QName.
     */
    public Type getType(QName name)
    {    	
        // 1. Try a prexisting mapped type
        Type type = (Type) mappedName2type.get(name);

        // 2. Try to get the type by its name, if there is one
        if (type == null)
        {
            QName typeName = getMappedTypeName(name);
            if (typeName != null)
            {
                type = getTypeMapping().getType(typeName);

                if (type != null)
                    mapType(name, type);
            }
        }

        // 3. Create the type from the property descriptor and map it
        if (type == null)
        {
            PropertyDescriptor desc;
            try
            {
                desc = getPropertyDescriptorFromMappedName(name);
            }
            catch (Exception e)
            {
                if (e instanceof XFireRuntimeException)
                    throw (XFireRuntimeException) e;
                throw new XFireRuntimeException("Couldn't get properties.", e);
            }

            if (desc == null)
            {
                return null;
            }

            try
            {
                TypeMapping tm = getTypeMapping();
                TypeCreator tc = tm.getTypeCreator();
                type = tc.createType(desc);
            }
            catch (XFireRuntimeException e)
            {
                e.prepend("Couldn't create type for property " + desc.getName() + " on "
                        + getTypeClass());

                throw e;
            }

            // second part is possible workaround for XFIRE-586
            if (registerType(desc))
                getTypeMapping().register(type);

            mapType(name, type);
        }

        if (type == null)
            throw new XFireRuntimeException("Couldn't find type for property " + name);

        return type;
    }

    protected boolean registerType(PropertyDescriptor desc)
    {
        return true;
    }

    public void mapType(QName name, Type type)
    {
        mappedName2type.put(name, type);
    }

    private QName getMappedTypeName(QName name)
    {
        return (QName) mappedName2typeName.get(name);
    }

    public TypeMapping getTypeMapping()
    {
        return typeMapping;
    }

    public void setTypeMapping(TypeMapping typeMapping)
    {
        this.typeMapping = typeMapping;
    }

    /**
     * Specifies the name of the property as it shows up in the xml schema. This
     * method just returns <code>propertyDescriptor.getName();</code>
     * 
     * @param desc
     * @return
     */
    protected QName createMappedName(PropertyDescriptor desc)
    {
        return new QName(getDefaultNamespace(), desc.getName());
    }

    public void mapAttribute(String property, QName mappedName)
    {
        mappedName2pdName.put(mappedName, property);
        attributes.add(mappedName);
    }

    public void mapElement(String property, QName mappedName)
    {
        mappedName2pdName.put(mappedName, property);
        elements.add(mappedName);
    }

    /**
     * Specifies the SchemaType for a particular class.
     * 
     * @param mappedName
     * @param type
     */
    public void mapTypeName(QName mappedName, QName type)
    {
        mappedName2typeName.put(mappedName, type);
    }

    private void initializeProperties()
    {
        BeanInfo beanInfo = null;
        try
        {
            if (beanClass.isInterface() || beanClass.isPrimitive())
            {
                descriptors = getInterfacePropertyDescriptors(beanClass);
            }
            else if (beanClass == Object.class || beanClass == Throwable.class)
            {
            }
            else if (beanClass == Throwable.class)
            {
            }
            else if (Throwable.class.isAssignableFrom(beanClass))
            {
                beanInfo = Introspector.getBeanInfo(beanClass, Throwable.class);
            }
            else if (RuntimeException.class.isAssignableFrom(beanClass))
            {
                beanInfo = Introspector.getBeanInfo(beanClass, RuntimeException.class);
            }
            else if (Throwable.class.isAssignableFrom(beanClass))
            {
                beanInfo = Introspector.getBeanInfo(beanClass, Throwable.class);
            }
            else
            {
                beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
            }
        }
        catch (IntrospectionException e)
        {
            throw new XFireRuntimeException("Couldn't introspect interface.", e);
        }

        if (beanInfo != null) {
            descriptors = beanInfo.getPropertyDescriptors();
            if (isGrailsDomainClass) {
				try {
					Method getHasMany = beanClass.getMethod("getHasMany", new Class[] {});
					Map hasManyMap = (Map) getHasMany.invoke(null, new Object[] {});
					for (int i = 0; i < descriptors.length; i++) {
						if (descriptors[i].getPropertyType().getName().equals("java.util.Set") ||
							descriptors[i].getPropertyType().getName().equals("java.util.List")	) {
							Object clazz = hasManyMap.get(descriptors[i].getName());
							// this is a hack to deal with generics for domain class
							// using shortDescription as buffer
							descriptors[i].setShortDescription(clazz.toString().replaceFirst("class ", ""));
						} else {
							descriptors[i].setShortDescription("");
						}
					}
				} catch (Exception e) {
				}
			}
        }

        if (descriptors == null)
        {
            descriptors = new PropertyDescriptor[0];
        }
    }

    public PropertyDescriptor[] getInterfacePropertyDescriptors(Class clazz)
    {
        List pds = new ArrayList();
        
        getInterfacePropertyDescriptors(clazz, pds, new HashSet());
        
        return (PropertyDescriptor[]) pds.toArray(new PropertyDescriptor[pds.size()]);
    }
    
    public void getInterfacePropertyDescriptors(Class clazz, List pds, Set classes)
    {
        if (classes.contains(clazz)) return;
        
        classes.add(clazz);
        
        try
        {
            Class[] interfaces = clazz.getInterfaces();
            
            /**
             * add base interface information
             */
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (int j = 0; j < info.getPropertyDescriptors().length; j++)
            {
                PropertyDescriptor pd = info.getPropertyDescriptors()[j];
                if (!containsPropertyName(pds, pd.getName()))
                {
                    pds.add(pd);
                }
            }
            
            /**
             * add extended interface information
             */
            for (int i = 0; i < interfaces.length; i++)
            {
                getInterfacePropertyDescriptors(interfaces[i], pds, classes);
            }
        }
        catch (IntrospectionException e)
        {
        }
    }

    private boolean containsPropertyName(List pds, String name)
    {
        for (Iterator itr = pds.iterator(); itr.hasNext();) 
        {
            PropertyDescriptor pd = (PropertyDescriptor) itr.next();
            if (pd.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public PropertyDescriptor getPropertyDescriptorFromMappedName(QName name)
    {
        return getPropertyDescriptor(getPropertyNameFromMappedName(name));
    }

    protected boolean isAttribute(PropertyDescriptor desc)
    {
        return false;
    }

    protected boolean isElement(PropertyDescriptor desc)
    {
        return true;
    }

    protected boolean isSerializable(PropertyDescriptor desc)
    {
        return true;
    }

    protected Class getTypeClass()
    {
        return beanClass;
    }

    /**
     * Nillable is only allowed if the actual property is Nullable
     * 
     * @param name
     * @return
     */
    public boolean isNillable(QName name)
    {
        Type type = getType(name);
        if (!type.isNillable())
            return false;
        return nillable;
    }

    public int getMinOccurs(QName name)
    {
        return minOccurs;
    }

    public void setDefaultMinOccurs(int minOccurs)
    {
        this.minOccurs = minOccurs;
    }

    public void setDefaultNillable(boolean nillable)
    {
        this.nillable = nillable;
    }

    private String getPropertyNameFromMappedName(QName name)
    {
        return (String) mappedName2pdName.get(name);
    }

    public Iterator getAttributes()
    {
        return attributes.iterator();
    }

    public Iterator getElements()
    {
        return elements.iterator();
    }

    public boolean isExtensibleElements()
    {
        return extensibleElements;
    }

    public void setExtensibleElements(boolean futureProof)
    {
        this.extensibleElements = futureProof;
    }

    public boolean isExtensibleAttributes()
    {
        return extensibleAttributes;
    }

    public void setExtensibleAttributes(boolean extensibleAttributes)
    {
        this.extensibleAttributes = extensibleAttributes;
    }

    public void setExtension(boolean extension)
    {
        this.isExtension = extension;
    }

    public boolean isExtension()
    {
        return isExtension;
    }

}