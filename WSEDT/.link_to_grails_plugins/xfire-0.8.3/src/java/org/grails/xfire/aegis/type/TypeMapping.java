package org.grails.xfire.aegis.type;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 * @since Feb 18, 2004
 */
public interface TypeMapping
{
    /**
     * Checks whether or not type mapping between specified XML
     * type and Java type is registered.
     *
     * @param javaType Class of the Java type
     * @param xmlType Qualified name of the XML data type
     * @return boolean; <code>true</code> if type mapping between the
     *      specified XML type and Java type is registered;
     *      otherwise <code>false</code>
     */
    public boolean isRegistered(Class javaType);

    public boolean isRegistered(QName xmlType);

    public void register(Class javaType, QName xmlType, Type type);

    public void register(Type type);

    public void removeType(Type type);

    public Type getType(Class javaType);

    public Type getType(QName xmlType);

    public QName getTypeQName(Class clazz);

    public String getEncodingStyleURI();

    public void setEncodingStyleURI(String encodingStyleURI);
    
    public TypeCreator getTypeCreator();
}
