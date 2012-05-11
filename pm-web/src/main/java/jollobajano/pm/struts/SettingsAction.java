package jollobajano.pm.struts;

import jollobajano.pm.model.CarbonCopy;
import jollobajano.pm.model.DocumentCollection;
import jollobajano.pm.model.User;
import jollobajano.pm.model.CarbonCopy.Event;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * 
 * @author <a href="mailto:info@jollobajano.com">Mats Nyberg</a>
 * 
 */
@Results({ @Result(name = "success", location = "settings-success.jsp"),
		@Result(name = "logout", location = "/Logout.html") })
public class SettingsAction extends AbstractActionBase {

	private static final long serialVersionUID = -6061078207519279476L;
	private static final Logger log = Logger.getLogger(SettingsAction.class);
	User user;
	CarbonCopy cc;
	DocumentCollection documentCollection;
	String userAddress = null;
	String ccAddress = null;
	String newUserAddress = null;
	String newCcAddress = null;
	Long delUser = null;
	Long delCc = null;



	public String execute() throws Exception {
		setupData();
		if (delUser != null) {
			dao.delete(User.class, delUser);
		}
		if (delCc != null) {
			dao.delete(CarbonCopy.class, delCc);
		}
		if (newUserAddress != null) {
			User newUser = new User();
			newUser.setDocumentCollection(user.getDocumentCollection());
			newUser.setMail(newUserAddress);
			documentCollection.add(newUser);
			dao.saveOrUpdate(documentCollection);
		}
		setupData();
		return SUCCESS;
	}



	void setupData() throws Exception {
		cc = new CarbonCopy();
		user = dao.getUser(principalProxy.getUserPrincipal().getName());
		documentCollection = dao.getDocumentCollectionByUserName(principalProxy.getUserPrincipal().getName());
		System.out.println("documentCollection.users: " + documentCollection.getUsers());
	}



	public Event[] getEventValues() {
		return Event.values();
	}



	public User getUser() {
		return user;
	}



	public DocumentCollection getDocumentCollection() {
		return documentCollection;
	}



	public void setUser( User user ) {
		this.user = user;
	}



	public void setDocumentCollection( DocumentCollection documentCollection ) {
		this.documentCollection = documentCollection;
	}



	public String getUserAddress() {
		return userAddress;
	}



	public void setUserAddress( String userAddress ) {
		this.userAddress = userAddress;
	}



	public String getCcAddress() {
		return ccAddress;
	}



	public void setCcAddress( String ccAddress ) {
		this.ccAddress = ccAddress;
	}



	public void setDelUser( Long delUser ) {
		this.delUser = delUser;
	}



	public void setDelCc( Long delCc ) {
		this.delCc = delCc;
	}



	public void setNewUserAddress( String newUserAddress ) {
		this.newUserAddress = newUserAddress;
	}



	public void setNewCcAddress( String newCcAddress ) {
		this.newCcAddress = newCcAddress;
	}



	public CarbonCopy getCc() {
		return cc;
	}



	public void setCc( CarbonCopy cc ) {
		this.cc = cc;
	}
}
