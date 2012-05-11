package jollobajano.pm.esb;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class MyMailListenerAction extends BaseAction
{
    
  protected ConfigTree	_config;
	  
  public MyMailListenerAction(ConfigTree config) { _config = config; } 

  
  public Message displayMessage(Message message) throws Exception{
		
		  return message; 
        		
	}
    
	
}
