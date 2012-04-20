package org.grails.xfire.aegis.type;

import org.codehaus.xfire.util.ClassLoaderUtils;

/**
 * @author <a href="mailto:tsztelak@gmail.com">Tomasz Sztelak</a>
 * Creates XMLTypeCreate for given jvm version ( support for Holder type )
 */
public class TypeCreatorFactory {
	
	
	static boolean isJDK5andAbove()
    {
        String v = System.getProperty("java.class.version", "44.0");
        return ("49.0".compareTo(v) <= 0);
    }
	
	public static AbstractTypeCreator  getTypeCreator(){
		
		if(isJDK5andAbove()){
			Class java5TypeCreator;
			try {
				java5TypeCreator = ClassLoaderUtils.loadClass("org.grails.xfire.aegis.type.java5.XMLTypeCreator", TypeCreatorFactory.class);
				return (AbstractTypeCreator ) java5TypeCreator.newInstance();
			} catch (ClassNotFoundException e) {
				// ignore all errors
			} catch (InstantiationException e) {
				
			} catch (IllegalAccessException e) {
				
			}
			
		}
		return new XMLTypeCreator();
		
	}
}
