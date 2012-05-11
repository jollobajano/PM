package jollobajano.pm.esb;

import java.io.File;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.addressing.eprs.LogicalEPR;
import org.jboss.soa.esb.listeners.gateway.RemoteFileMessageComposer;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;

public class CustomFtpMessageComposer extends
                        RemoteFileMessageComposer<File> {

	private static final Logger LOGGER = Logger.getLogger(CustomFtpMessageComposer.class);

    /* (non-Javadoc)
     * @see org.jboss.soa.esb.listeners.gateway.RemoteFileMessageComposer#compose(java.io.File)
     */
    @Override
    public Message compose(File inFile) throws MessageDeliverException {
		// Create the message in a normal way
        Message message = super.compose(inFile);
        
        // Set the default FaultTo
        LOGGER.debug("Before setting default faultTo");
		LogicalEPR defaultFaultToEpr = 
			new LogicalEPR("MyExceptionHandlingServiceCategory", "MyExceptionHandlingServiceName");
		message.getHeader().getCall().setFaultTo(defaultFaultToEpr);                        
		LOGGER.debug("After setting default faultTo");

        return message;
    }
}



