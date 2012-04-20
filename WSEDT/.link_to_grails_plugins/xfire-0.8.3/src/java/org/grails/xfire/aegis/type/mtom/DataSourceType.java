package org.grails.xfire.aegis.type.mtom;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.attachments.Attachment;
import org.codehaus.xfire.attachments.SimpleAttachment;

public class DataSourceType extends AbstractXOPType
{
    protected Object readAttachment(Attachment att, MessageContext context)
    {
        return att.getDataHandler().getDataSource();
    }
    
    protected Attachment createAttachment(Object object, String id)
    {
        DataSource source = (DataSource) object;
        
        DataHandler handler = new DataHandler(source);
        SimpleAttachment att = new SimpleAttachment(id, handler);
        att.setXOP(true);
        return att;
    }
    
    protected String getContentType(Object object, MessageContext context)
    {
        return ((DataSource) object).getContentType();
    }
}
