package jollobajano.pm.esb;

import org.jboss.soa.esb.actions.BeanConfiguredAction;
import org.jboss.soa.esb.message.Message;


public class RouteExpiredAction extends BaseAction implements BeanConfiguredAction
{

	// Category, Name pairs
	String expired = null;
	String expiring = null;
	
	public Message process( Message message ) throws Exception
	{
		
		return null;
	}
	
	public void setExpired( String expired )
	{
		this.expired = expired;
	}
	
	public void setExpiring( String expiring )
	{
		this.expiring = expiring;
	}
}
