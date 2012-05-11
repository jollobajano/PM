package jollobajano.pm.esb;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentInfo;
import jollobajano.pm.model.HibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jboss.soa.esb.listeners.ScheduledEventMessageComposer;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.schedule.SchedulingException;

public class CheckExpiringEntriesListener implements ScheduledEventMessageComposer {
	
	Long margin = null;

	public void initialize(ConfigTree config) throws ConfigurationException {
		//log.debug("** initialize: " + config);
		margin = Long.valueOf(config.getRequiredAttribute("margin"));
	}


	public void uninitialize() {
	}


	public Message composeMessage() throws SchedulingException {

		Calendar calendar = Calendar.getInstance(new Locale("sv", "SE"));
		Date today = new Date(calendar.getTimeInMillis());
		Date triggerDate = new Date(calendar.getTimeInMillis() - margin * 1000 * 3600 *24);
		List<DocumentInfo> info = null;
		
		try {
			info = DAO.getInstance().checkForExpiringEntries(triggerDate, today);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SchedulingException(e.getMessage(), e);
		}
		
		if(info != null && info.size() > 0) {
			Message myMessage = MessageFactory.getInstance().getMessage(MessageType.JBOSS_XML);
			myMessage.getBody().add(info);
			return myMessage;
		}

		return null;
	}


	public Message onProcessingComplete(Message message) throws SchedulingException {
		return message;
	}
}
