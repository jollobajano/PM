package jollobajano.pm.esb;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentInfo;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.schedule.ScheduledEventListener;
import org.jboss.soa.esb.schedule.SchedulingException;

public class DatabasePoller implements ScheduledEventListener
{

	static final Logger log = Logger.getLogger("PM");

	ServiceInvoker expiringInvoker = null;
	ServiceInvoker expiredInvoker = null;

	int margin = 1;


	public void initialize( ConfigTree config ) throws ConfigurationException
	{
		expiringInvoker = getServiceInvokerFromCompoundName(config.getRequiredAttribute("expiringService"));
		expiredInvoker = getServiceInvokerFromCompoundName(config.getRequiredAttribute("expiredService"));
		margin = Integer.valueOf(config.getAttribute("margin", "7"));
	}


	public void uninitialize()
	{

	}


	public void onSchedule() throws SchedulingException
	{
		Calendar calendar = Calendar.getInstance(new Locale("sv", "SE"));
		Date today = new Date(calendar.getTimeInMillis());
		Date triggerDate = new Date(calendar.getTimeInMillis() + margin * 1000 * 3600 * 24);

		try
		{
			List<DocumentInfo> info = DAO.getInstance().checkForExpiringEntries(triggerDate, today);

			log.debug("ExpiringEntries: " + info);

			if (info != null && info.size() > 0)
			{
				for (DocumentInfo expiring : info)
				{
					if (expiring.getExpires().compareTo(today) <= 0
							&& expiring.getState().ordinal() < DocumentInfo.State.EXPIRED.ordinal())
					{
						expiring.setState(DocumentInfo.State.EXPIRED);
						DAO.getInstance().updateDocumentInfo(expiring);
						log.debug("Expired: " + expiring);
						expiredInvoker.deliverAsync(newMessage(expiring));
						
					} else if (expiring.getState().ordinal() < DocumentInfo.State.EXPIRING.ordinal())
					{
						expiring.setState(DocumentInfo.State.EXPIRING);
						DAO.getInstance().updateDocumentInfo(expiring);
						log.debug("Expiring: " + expiring);
						expiringInvoker.deliverAsync(newMessage(expiring));
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new SchedulingException(e.getMessage(), e);
		}
	}


	Message newMessage( DocumentInfo documentInfo )
	{
		Message message = MessageFactory.getInstance().getMessage(MessageType.JAVA_SERIALIZED);
		message.getBody().add(documentInfo);
		return message;
	}


	ServiceInvoker getServiceInvokerFromCompoundName( String compound ) throws ConfigurationException
	{
	    char separator = ':';
	    String category = compound.substring(0, compound.indexOf(separator));
	    String name = compound.substring(compound.indexOf(separator) + 1);
	    try
		{
		    ServiceInvoker invoker = new ServiceInvoker(category, name);
		    return invoker;
		} catch (MessageDeliverException e)
		{
		    e.printStackTrace();
		    throw new ConfigurationException(e.getMessage(), e);
		}
	}
}
