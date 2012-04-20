package org.grails.xfire;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.codehaus.xfire.DefaultXFire;
import org.codehaus.xfire.XFire;
import org.codehaus.xfire.transport.http.XFireServlet;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * An servlet which exposes XFire services via Spring.
 * 
 * @author Jason Carreira <jcarreira@eplus.com>
 */
public class XFireSpringServlet extends XFireServlet
{
    private String xfireBeanName = "xfire";

    private XFire xfire;

    public void init(ServletConfig servletConfig)
        throws ServletException
    {
        ApplicationContext appContext = WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletConfig.getServletContext());
        String configBeanName = servletConfig.getInitParameter("XFireBeanName");

        xfireBeanName = ((configBeanName != null) && (!"".equals(configBeanName.trim()))) ? configBeanName
                : xfireBeanName;
        try {
        	xfire = (XFire) appContext.getBean(xfireBeanName, XFire.class);
        } catch (BeanNotOfRequiredTypeException e) {
        	DefaultXFire dXFire = (DefaultXFire) appContext.getBean("xfire");
        	xfire = (XFire) dXFire;
		}
        super.init(servletConfig);
    }

    public XFire createXFire()
    {
        return xfire;
    }
}
