package jollobajano.pm.struts;

import jollobajano.pm.model.DAO;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.PrincipalAware;
import org.apache.struts2.interceptor.PrincipalProxy;

import com.opensymphony.xwork2.ActionSupport;


public abstract class AbstractActionBase extends ActionSupport implements PrincipalAware
{
	private static final long serialVersionUID = 6474126925262695995L;

	static final Logger log = Logger.getLogger("PM");
	
	
	PrincipalProxy principalProxy;
	DAO dao = DAO.getInstance();


	@Override
	public void setPrincipalProxy( PrincipalProxy principalProxy )
	{
		this.principalProxy = principalProxy;
	}

}
