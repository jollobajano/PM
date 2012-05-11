package jollobajano.pm.esb;

import org.jboss.soa.esb.configure.ConfigProperty;
import org.jboss.soa.esb.configure.ConfigProperty.Use;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;

import com.sun.mail.imap.protocol.BODY;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentInfo;

public class MarkDocumentInfo extends BaseAction
{

	DocumentInfo.State state = null;
	String dataLocation = Body.DEFAULT_LOCATION;


	@Process
	public Message process( Message message ) throws Exception
	{
		Object data = message.getBody().get(dataLocation);
		if (data != null && data instanceof DocumentInfo)
		{
			DocumentInfo documentInfo = (DocumentInfo) data;
			documentInfo.setState(state);

			try
			{
				DAO.getInstance().updateDocumentInfo(documentInfo);
			} catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
			message.getBody().add(dataLocation, documentInfo);
		}
		return message;
	}


	@ConfigProperty
	public void setState( DocumentInfo.State state )
	{
		this.state = state;
	}


	@ConfigProperty(use = Use.OPTIONAL)
	public void setDataLocation( String dataLocation )
	{
		this.dataLocation = dataLocation;
	}
}
