package org.grails.xfire.aegis.type.mtom;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.attachments.Attachment;
import org.codehaus.xfire.attachments.AttachmentUtil;
import org.codehaus.xfire.attachments.Attachments;
import org.codehaus.xfire.attachments.JavaMailAttachments;
import org.codehaus.xfire.fault.XFireFault;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>
 */
public abstract class AbstractXOPType
	extends Type
{
    public final static String XOP_NS = "http://www.w3.org/2004/08/xop/include";
    public final static String XML_MIME_NS = "http://www.w3.org/2004/11/xmlmime";
 
    public final static QName XOP_INCLUDE = new QName(XOP_NS, "Include");
    public final static QName XOP_HREF = new QName("href");
    public final static QName XML_MIME_TYPE = new QName(XML_MIME_NS, "mimeType");
    
    public AbstractXOPType()
    {
    }
    
    public Object readObject(MessageReader reader, MessageContext context)
    	throws XFireFault
    {
        Object o = null;
        while (reader.hasMoreElementReaders())
        {
            MessageReader child = reader.getNextElementReader();
            if (child.getName().equals(XOP_INCLUDE))
            {
                MessageReader mimeReader = child.getAttributeReader(XOP_HREF);
                String type = mimeReader.getValue();
                o = readInclude(type, child, context);
            }
            child.readToEnd();
        }
        
        return o;
    }
    
    public Object readInclude(String type, MessageReader reader, MessageContext context)
        throws XFireFault
    {
        String href = reader.getAttributeReader(XOP_HREF).getValue();
        
        Attachment att = AttachmentUtil.getAttachment(href, context.getInMessage());
        
        if (att == null)
        {
            throw new XFireFault("Could not find the attachment " + href, XFireFault.SENDER);
        }
        
        try
        {
            return readAttachment(att, context);
        }
        catch (IOException e)
        {
            throw new XFireFault("Could not read attachment", e, XFireFault.SENDER);
        }
    }

    protected abstract Object readAttachment(Attachment att, MessageContext context) throws IOException;
    
    public void writeObject(Object object, MessageWriter writer, MessageContext context) 
    	throws XFireFault
    {
        Attachments attachments = context.getOutMessage().getAttachments();
        if ( attachments == null )
        {
            attachments = new JavaMailAttachments();
            context.getOutMessage().setAttachments(attachments);
        }

        String id = AttachmentUtil.createContentID(getSchemaType().getNamespaceURI());
        
        Attachment att = createAttachment(object, id);
        
        attachments.addPart(att);
          
        String contentType = getContentType(object, context);
        if (contentType != null)
        {
            MessageWriter mt = writer.getAttributeWriter(XML_MIME_TYPE);
            mt.writeValue(contentType);
        }
        
        MessageWriter include = writer.getElementWriter(XOP_INCLUDE);
        MessageWriter href = include.getAttributeWriter(XOP_HREF);
        href.writeValue("cid:" + id);
        
        include.close();
    }

    protected abstract Attachment createAttachment(Object object, String id);
    
    protected abstract String getContentType(Object object, MessageContext context);
}
