package org.grails.xfire.aegis.type;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.codehaus.xfire.XFireRuntimeException;
import org.grails.xfire.ServiceFactoryBean;
import org.grails.xfire.aegis.type.basic.BeanType;
import org.grails.xfire.aegis.type.basic.BeanTypeInfo;

public class DefaultTypeCreator
    extends AbstractTypeCreator
{
    public DefaultTypeCreator()
    {
    }

    public DefaultTypeCreator(Configuration configuration)
    {
        setConfiguration(configuration);
    }

    public TypeClassInfo createClassInfo(Method m, int index)
    {
        TypeClassInfo info = new TypeClassInfo();

        if (index >= 0) {
            java.lang.reflect.Type t = m.getGenericParameterTypes()[index];
            if(t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)t;
                info.setGenericType((Class)pt.getActualTypeArguments()[0]);
            }
            info.setTypeClass(m.getParameterTypes()[index]);
        } else {
            // System.out.println(m);
            java.lang.reflect.Type t = m.getGenericReturnType();
            if(t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType)t;
                info.setGenericType((Class)pt.getActualTypeArguments()[0]);
            }
            info.setTypeClass(m.getReturnType());
        }

        return info;
    }

    public TypeClassInfo createClassInfo(PropertyDescriptor pd)
    {
        TypeClassInfo info = createBasicClassInfo(pd.getPropertyType());
        try {
            Class c = ServiceFactoryBean.getApp().getClassLoader().loadClass(pd.getShortDescription());
            if (ServiceFactoryBean.getApp().isArtefactOfType(DomainClassArtefactHandler.TYPE, c)) {
                if ((pd.getPropertyType().getName().equals("java.util.Set") ||
                    pd.getPropertyType().getName().equals("java.util.List")	)
                        && !pd.getShortDescription().equals("")) {
                    info.setGenericType(c);
                    return info;
                }
            }
        } catch (Exception e) {
            // do nothing, it's not a Grails domain class
        }
        
        Method m = pd.getReadMethod();
        java.lang.reflect.Type t = m.getGenericReturnType();
        if(t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType)t;
            info.setGenericType((Class)pt.getActualTypeArguments()[0]);
        }        
        return info;
    }

    public Type createCollectionType(TypeClassInfo info)
    {
        System.out.println("=================");
        System.out.println(info.description);
        System.out.println(info.flat);
        System.out.println(info.genericType);
        System.out.println(info.keyType);
        System.out.println(info.mappedName);
        System.out.println(info.type);
        System.out.println(info.typeClass);
        System.out.println(info.typeName);
        System.out.println("=================");
        if (info.getGenericType() == null)
        {
            throw new XFireRuntimeException("Cannot create mapping for "
                    + info.getTypeClass().getName() + ", unspecified component type for "
                    + info.getDescription());
        }

        return createCollectionTypeFromGeneric(info);
    }

    public Type createDefaultType(TypeClassInfo info)
    {
        BeanType type = new BeanType();
        type.setSchemaType(createQName(info.getTypeClass()));
        type.setTypeClass(info.getTypeClass());
        type.setTypeMapping(getTypeMapping());

        BeanTypeInfo typeInfo = type.getTypeInfo();
        typeInfo.setDefaultMinOccurs(getConfiguration().getDefaultMinOccurs());
        typeInfo.setExtensibleAttributes(getConfiguration().isDefaultExtensibleAttributes());
        typeInfo.setExtensibleElements(getConfiguration().isDefaultExtensibleElements());

        return type;
    }
}
