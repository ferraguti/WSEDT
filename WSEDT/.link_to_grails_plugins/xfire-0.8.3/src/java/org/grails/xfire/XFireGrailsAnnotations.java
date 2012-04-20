package org.grails.xfire;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;

import org.codehaus.groovy.grails.commons.GrailsClassUtils;
import org.codehaus.xfire.annotations.*;
import org.codehaus.xfire.annotations.soap.SOAPBindingAnnotation;

public class XFireGrailsAnnotations implements WebAnnotations {

	private boolean shouldBeExcluded(Method method) {
		if(method.getName().startsWith("super$")) return true;
		if(method.getName().startsWith("this$"))  return true;
		if(method.isSynthetic()) return true;
		String excludeMethods[] = {
				"getMetaClass","setMetaClass", 
				"getProperty", "setProperty",
				"invokeMethod", "isTransactional",
				"getTransactional", "setTransactional", 
				"getMetaMethods", "setMetaMethods"};
		for (int i = 0; i < excludeMethods.length; i++) {
			if(method.equals(excludeMethods[i])) return true;
		}
		return false;
	}

	public Collection getFaultHandlers(Class clazz) {
		return Collections.EMPTY_LIST;
	}

	public HandlerChainAnnotation getHandlerChainAnnotation(Class aClass) {
		return null;
	}

	public Collection getInHandlers(Class clazz) {
        InHandlers inHandlers = (InHandlers) clazz.getAnnotation(InHandlers.class);

        if (inHandlers == null)  {
            return Collections.EMPTY_LIST;
        }

        List<String> handlers = new ArrayList<String>();
        Collections.addAll(handlers,inHandlers.handlers());
        return handlers;
	}

	public Collection getOutHandlers(Class clazz) {
        OutHandlers outHandlers = (OutHandlers) clazz.getAnnotation(OutHandlers.class);

        if (outHandlers == null)  {
            return Collections.EMPTY_LIST;
        }

        List<String> handlers = new ArrayList<String>();
        Collections.addAll(handlers,outHandlers.handlers());
        return handlers ;
	}

	public SOAPBindingAnnotation getSOAPBindingAnnotation(Class aClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getServiceProperties(Class clazz) {
		return Collections.EMPTY_MAP;
	}

	private boolean isExposed(Class aClass, String name) {
		List exposeList = (List)GrailsClassUtils.getStaticPropertyValue(aClass, "expose");
		if(exposeList == null) return false;
		return exposeList.contains(name);
	}

	private boolean isExcluded(Class aClass, String name) {
		List excludeList = (List)GrailsClassUtils.getStaticPropertyValue(aClass, "exclude");
		if(excludeList == null) return false;
		return excludeList.contains(name);
	}

	private boolean isHeader(Class aClass, String name) {
		List headerList = (List)GrailsClassUtils.getStaticPropertyValue(aClass, "header");
		if(headerList == null) return false;
		return headerList.contains(name);
	}

	public WebMethodAnnotation getWebMethodAnnotation(Method method) {

		Class aClass = method.getDeclaringClass();

		WebMethodAnnotation wmAnnotation = new WebMethodAnnotation();
		wmAnnotation.setOperationName(method.getName());
		wmAnnotation.setExclude(false);

		try {
			if (method.getName().startsWith("get")) {
				String propName = GrailsClassUtils.getPropertyForGetter(method
						.getName());
				String setterName = GrailsClassUtils.getSetterName(propName);
				Method setterMethod = aClass.getMethod(setterName,
						new Class[] { method.getReturnType() });
				if (setterMethod != null) {
					wmAnnotation.setExclude(true);
				}
			} else if (method.getName().startsWith("is")) {
				String propName = GrailsClassUtils.getPropertyName(method
						.getName().replaceFirst("is", ""));
				String setterName = GrailsClassUtils.getSetterName(propName);
				Method setterMethod = aClass.getMethod(setterName,
						new Class[] { method.getReturnType() });
				if (setterMethod != null) {
					wmAnnotation.setExclude(true);
				}
			} else if (method.getName().startsWith("set")) {
				String propName = GrailsClassUtils.getPropertyName(method
						.getName().replaceFirst("set", ""));
				String getterName = GrailsClassUtils.getGetterName(propName);
				if (method.getParameterTypes().length == 1) {
					Class firstParam = method.getParameterTypes()[0];
					if (firstParam.getName().equals("boolean")) {
						getterName = getterName.replaceFirst("get", "is");
					}
					Method getterMethod = aClass.getMethod(getterName,
							new Class[] {});
					if (getterMethod.getReturnType() == firstParam) {
						wmAnnotation.setExclude(true);
					}
				}
			}
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}

		if (shouldBeExcluded(method)) {
			wmAnnotation.setExclude(true);
		}
		if (isExcluded(aClass, method.getName())) {
			wmAnnotation.setExclude(true);
		}
		return wmAnnotation;
	}

	public WebParamAnnotation getWebParamAnnotation(Method method, int parameter) {
		Annotation[] anns = method.getParameterAnnotations()[parameter];
		WebParamAnnotation wp = new WebParamAnnotation();
		WebParam webParam = null;
		for (int i = 0; i < anns.length; i++) {
			if(anns[i] instanceof WebParam) {
				webParam = (WebParam)anns[i];
				break;
			}
		}
		if(anns.length == 0 || webParam == null) {
			wp.setName("in"+ String.valueOf(parameter));
		} else {
			wp.setHeader(webParam.header());
			wp.setMode(webParam.mode().ordinal());
			wp.setName(webParam.name());
			wp.setPartName(webParam.partName());
			wp.setTargetNamespace(webParam.targetNamespace());
		}
		return wp;
	}

	public WebResultAnnotation getWebResultAnnotation(Method method) {
		WebResult wr = (WebResult) method.getAnnotation(WebResult.class);
		WebResultAnnotation wrAnnotation = new WebResultAnnotation();
        if(wr == null) {
		    return null;
	    } else {
	        wrAnnotation.setPartName(wr.partName());
	        wrAnnotation.setHeader(wr.header());
	        wrAnnotation.setTargetNamespace(wr.targetNamespace());
	        wrAnnotation.setName(wr.name());
	        return wrAnnotation;
/*	        
	        if(wr.partName().equals("")) {
	            wrAnnotation.
	        } else {
	            
	        }	        
	        if(wr.header()==false) {
	            
	        } else {
	            
	        }
	        if(wr.targetNamespace().equals("")) {
	            
	        } else {
	            
	        }
	        if(wr.name().equals("")) {
	            
	        } else {
	            
	        }
*/	        
	    }
	}

	private String genNamespace(Class aClass) {
		String packageName = "DefaultNamespace";
		if(aClass.getPackage()!=null) {
			String s[] = aClass.getPackage().getName().split("\\.");
			packageName="";
			for(int i = s.length-1;i>=0;i--) {
				packageName = packageName + s[i];
				if(i!=0) packageName = packageName + ".";
			}
		}
		return "http://" + packageName;
	}

	public WebServiceAnnotation getWebServiceAnnotation(Class aClass) {
		WebService ws = (WebService) aClass.getAnnotation(WebService.class);
		WebServiceAnnotation wsAnnotation = new WebServiceAnnotation();
		String serviceName = GrailsClassUtils.getPropertyName(aClass).replaceFirst("Service", "");
		if(ws == null) {
			wsAnnotation.setTargetNamespace(genNamespace(aClass));
			wsAnnotation.setName(serviceName);
			wsAnnotation.setServiceName(serviceName);
		} else {
			if(ws.targetNamespace().equals("")) {
				wsAnnotation.setTargetNamespace(genNamespace(aClass));
			} else {
				wsAnnotation.setTargetNamespace(ws.targetNamespace());
			}
			if(ws.name().equals("")) {
				wsAnnotation.setName(serviceName);
			} else {
				wsAnnotation.setName(ws.name());
			}
			if(ws.serviceName().equals("")) {
				wsAnnotation.setServiceName(serviceName);
			} else {
				wsAnnotation.setServiceName(ws.serviceName());
			}
			wsAnnotation.setPortName(ws.portName());
			wsAnnotation.setEndpointInterface(ws.endpointInterface());
			wsAnnotation.setWsdlLocation(ws.wsdlLocation());
		}
		return wsAnnotation;
	}

	public boolean hasHandlerChainAnnotation(Class aClass) {
		return false;
	}

	public boolean hasOnewayAnnotation(Method method) {
		return false;
	}

	public boolean hasSOAPBindingAnnotation(Class aClass) {
		return false;
	}

	public boolean hasWebMethodAnnotation(Method method) {
		if(Modifier.isPublic(method.getModifiers()) == false) return false;
		if(Modifier.isStatic(method.getModifiers())) return false;
		return true;
	}

	public boolean hasWebParamAnnotation(Method method, int parameter) {
		return true;
	}

	public boolean hasWebResultAnnotation(Method method) {
		return (WebResult) method.getAnnotation(WebResult.class) != null;
	}

	public boolean hasWebServiceAnnotation(Class aClass) {
		return isExposed(aClass, "xfire");
	}
}
