package org.grails.xfire;

import groovy.lang.GroovyObject;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.service.invoker.BeanInvoker;

public class GroovyBeanInvoker extends BeanInvoker {

	public Object invoke(Method method, Object[] args, MessageContext context) throws XFireFault {
		Object serviceObject = getServiceObject(context);
		if (serviceObject instanceof GroovyObject) {
			GroovyObject pogo = (GroovyObject) serviceObject;
			try {
				return pogo.invokeMethod(method.getName(), args);
			} catch (UndeclaredThrowableException e) {			
				Throwable t = e.getUndeclaredThrowable();
				throw XFireFault.createFault(t);
			} catch (Exception e) {
				throw new XFireFault(e.getMessage(), e.getCause(), XFireFault.RECEIVER); 				
			}
		} else {
			return super.invoke(method, args, context);
		}
	}

	public GroovyBeanInvoker(Object serviceBean) {
		super(serviceBean);
	}

}
