package jollobajano.pm.esb;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;


public class Debuging extends BaseAction {
	
	
	public Debuging(ConfigTree config) throws Exception {}

	
	
	public Message process(Message message) throws Exception {
		
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Call: " + message.getHeader().getCall().getTo());
		log.debug("Names in context");
		for(String name : message.getContext().getContextKeys()) {
			log.debug(name + ": " + message.getContext().getContext(name));			
		}
		log.debug("Properties");
		for(String name : message.getProperties().getNames()) {
			log.debug(name + ": " + message.getProperties().getProperty(name));			
		}
		log.debug("Names in body");
		for(String name : message.getBody().getNames()) {
			log.debug(name + ": " + message.getBody().get(name));			
		}
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		
		return message;
	}
	
}
