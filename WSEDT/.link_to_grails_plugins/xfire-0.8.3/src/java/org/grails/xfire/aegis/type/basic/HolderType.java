package org.grails.xfire.aegis.type.basic;

import javax.xml.namespace.QName;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.exchange.AbstractMessage;
import org.codehaus.xfire.fault.XFireFault;
import org.codehaus.xfire.service.MessagePartInfo;
import org.codehaus.xfire.service.binding.AbstractBinding;
import org.grails.xfire.aegis.AegisBindingProvider;
import org.grails.xfire.aegis.Holder;
import org.grails.xfire.aegis.MessageReader;
import org.grails.xfire.aegis.MessageWriter;
import org.grails.xfire.aegis.type.Type;
import org.grails.xfire.aegis.type.TypeMapping;
import org.jdom.Element;

/**
 * A holder type which makes multiple out parameters possible.
 * @author Dan Diephouse
 */
public class HolderType
    extends Type
{
    private Type delegate;
    
    public HolderType(Type delegate)
    {
        super();
    
        this.delegate = delegate;
        setTypeClass(org.grails.xfire.aegis.Holder.class);
    }
    
    public Type getDelegate()
    {
        return delegate;
    }

    public Object readObject(MessageReader reader, MessageContext context)
        throws XFireFault
    {
        Object o = delegate.readObject(reader, context);
        AbstractMessage msg;
        if (AbstractBinding.isClientModeOn(context))
            msg = context.getOutMessage();
        else
            msg = context.getInMessage();
        
        Object[] params = (Object[]) msg.getBody();
        MessagePartInfo part = (MessagePartInfo) 
            context.getProperty(AegisBindingProvider.CURRENT_MESSAGE_PART);

        setValue(params[part.getIndex()], o);
        
        return o;
    }

    protected void setValue(Object hObj, Object value)
    {
        Holder holder = (Holder) hObj;
        holder.setValue(value);
    }

    public void writeObject(Object object, MessageWriter writer, MessageContext context)
        throws XFireFault
    {
        Holder holder = (Holder) object;
        delegate.writeObject(holder.getValue(), writer, context);
    }

    public QName getSchemaType()
    {
        return delegate.getSchemaType();
    }

    public boolean isAbstract()
    {
        return delegate.isAbstract();
    }

    public boolean isComplex()
    {
        return delegate.isComplex();
    }

    public boolean isNillable()
    {
        return delegate.isNillable();
    }

    public boolean isWriteOuter()
    {
        return delegate.isWriteOuter();
    }

    public void setAbstract(boolean abstrct)
    {
        delegate.setAbstract(abstrct);
    }

    public void setNillable(boolean nillable)
    {
        delegate.setNillable(nillable);
    }

    public void setSchemaType(QName name)
    {
        delegate.setSchemaType(name);
    }

    public void setTypeMapping(TypeMapping typeMapping)
    {
        super.setTypeMapping(typeMapping);
        delegate.setTypeMapping(typeMapping);
    }

    public void setWriteOuter(boolean writeOuter)
    {
        delegate.setWriteOuter(writeOuter);
    }

    public void writeSchema(Element root)
    {
        delegate.writeSchema(root);
    }
}
