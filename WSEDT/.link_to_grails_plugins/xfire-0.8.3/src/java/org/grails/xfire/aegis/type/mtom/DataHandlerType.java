package org.grails.xfire.aegis.type.mtom;

import javax.activation.DataHandler;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.attachments.Attachment;
import org.codehaus.xfire.attachments.SimpleAttachment;

public class DataHandlerType extends AbstractXOPType
{
    protected Object readAttachment(Attachment att, MessageContext context)
    {
        return att.getDataHandler();
    }
    
    protected Attachment createAttachment(Object object, String id)
    {
        DataHandler handler = (DataHandler) object;
        
        SimpleAttachment att = new SimpleAttachment(id, handler);
        att.setXOP(true);
        
        return att;
    }
    
    protected String getContentType(Object object, MessageContext context)
    {
        return ((DataHandler) object).getContentType();
    }
}
