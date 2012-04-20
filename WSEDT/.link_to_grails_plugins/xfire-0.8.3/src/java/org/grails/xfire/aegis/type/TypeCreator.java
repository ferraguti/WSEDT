package org.grails.xfire.aegis.type;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public interface TypeCreator
{
    /**
     * Get the mapped name of a method parameter.
     * @param m
     * @param index
     * @return
     */
    QName getElementName(Method m, int index);
    
    Type createType(Method m, int index);
    Type createType(PropertyDescriptor pd);
    Type createType(Field f);
    Type createType(Class clazz);
    
    void setTypeMapping(TypeMapping typeMapping);

    TypeCreator getParent();
	void setParent(TypeCreator creator);
}
