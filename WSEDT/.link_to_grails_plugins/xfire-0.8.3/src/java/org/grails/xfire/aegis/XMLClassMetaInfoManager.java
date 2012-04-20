package org.grails.xfire.aegis;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.util.jdom.StaxBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * @author <a href="mailto:tsztelak@gmail.com">Tomasz Sztelak</a>
 * 
 */
public class XMLClassMetaInfoManager {

	private static final Log log = LogFactory.getLog(XMLClassMetaInfoManager.class);
	
	private Map documents = new HashMap();

	/**
	 * @param clazz
	 * @return
	 */
	public Document getDocument(Class clazz) {
		if (clazz == null)
			return null;
		Document doc = (Document) documents.get(clazz.getName());
		if (doc != null) {
			return doc;
		}
		String path = '/' + clazz.getName().replace('.', '/') + ".aegis.xml";
		InputStream is = clazz.getResourceAsStream(path);
		if (is == null) {
			log.debug("Mapping file : " + path + " not found.");
			return null;
		}
		log.debug("Found mapping file : " + path);
		try {
			doc = new StaxBuilder().build(is);
			documents.put(clazz.getName(), doc);
			return doc;
		} catch (XMLStreamException e) {
			log.error("Error loading file " + path, e);
		}
		return null;
	}
	
	
	/**
	 * @param clazz
	 * @param encodingStyleURI
	 * @return
	 */
	public Element findMapping(Class clazz, String encodingStyleURI)
    {
        Document doc = getDocument(clazz);
        if(doc == null) return null;
        
        Element mapping = getMatch(doc, "/mappings/mapping[@uri='" + encodingStyleURI + "']");
        if (mapping == null)
        {
            mapping = getMatch(doc, "/mappings/mapping[not(@uri)]");
        }
        
        return mapping;
    }
	
	public Element getMatch(Object doc, String xpath)
    {
        try
        {
            XPath path = XPath.newInstance(xpath);
            return (Element)path.selectSingleNode(doc);
        }
        catch(JDOMException e)
        {
            throw new XFireRuntimeException("Error evaluating xpath " + xpath, e);
        }
    }

	public Element getParamter(Element elem, int i){
		return getMatch(elem, "parameter[@index='" + i + "']");
	}
	
	public Element getProperty(Element mapping, String name){
		return  getMatch(mapping, "./property[@name='" + name + "']");	
	}

	public Element getComponent(Element mapping, String name){
		return  getMatch(mapping, "./component[@name='" + name + "']");	
	}
	
	public Element getMethod(Element mapping, String name){
		return  getMatch(mapping, "./method[@name='" + name + "']");	
	}
	
}
