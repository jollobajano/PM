package jollobajano.pm.esb;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class Noop extends BaseAction
{

	public Noop(ConfigTree config) throws Exception
	{
	}


	public Message process( Message message ) throws Exception
	{

		return message;
	}

}
