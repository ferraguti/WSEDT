package org.grails.xfire.aegis;

import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.test.AbstractXFireTest;

/**
 * @author <a href="mailto:dan@envoisolutions.com">Dan Diephouse </a>
 * @since Oct 31, 2004
 */
public abstract class AbstractXFireAegisTest
    extends AbstractXFireTest
{
    protected void setUp()
        throws Exception
    {
        super.setUp();

        setServiceFactory(new ObjectServiceFactory(getTransportManager(),
                new AegisBindingProvider()));
    }
}
