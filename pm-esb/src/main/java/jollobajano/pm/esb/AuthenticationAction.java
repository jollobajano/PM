package jollobajano.pm.esb;

import org.jboss.soa.esb.configure.ConfigProperty;
import org.jboss.soa.esb.configure.ConfigProperty.Use;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentCollection;
import jollobajano.pm.model.User;

/**
 * Just to make sure mail/message seems to come from a trusted source.
 * 
 * @author <a href="mailto:info@jollobajano.com">Mats Nyberg</a>
 * 
 */
public class AuthenticationAction extends BaseAction
{

	static final DAO dao = DAO.getInstance();

	String userNameSource;
	String userLocation;


	@Process
	public Message process( Message message ) throws Exception
	{
		try
		{
			String submitter = message.getBody().get(userNameSource).toString();
			submitter = submitter.indexOf('<') >= 0 ? 
					submitter.substring(submitter.indexOf('<') + 1, submitter.indexOf('>')):
						submitter;
			log.debug("Submitter: " + submitter);
			User user = dao.getUser(submitter);
			//DocumentCollection documentCollection = user.getDocumentCollection();

			if (user == null) // non-valid sender of mail
				return null;

			//if (userLocation == null)
			//	userLocation = User.ATTRIBUTE_NAME;

			//message.getBody().add(userLocation, user);
			//message.getBody().add(DocumentCollection.ATTRIBUTE_NAME, documentCollection);
			return message;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


	@ConfigProperty
	public void setUserNameSource( String userNameSource )
	{
		this.userNameSource = userNameSource;
	}


	@ConfigProperty(use = Use.OPTIONAL)
	public void setUserLocation( String userLocation )
	{
		this.userLocation = userLocation;
	}
}
