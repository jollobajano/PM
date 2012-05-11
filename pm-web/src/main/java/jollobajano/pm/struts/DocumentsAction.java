package jollobajano.pm.struts;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.PrincipalAware;
import org.apache.struts2.interceptor.PrincipalProxy;

import com.opensymphony.xwork2.ActionSupport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentInfo;
import jollobajano.pm.model.User;

/**
 * Acts as a controller to handle actions related to registering a user.
 * 
 * @author bruce phillips
 * 
 */
@Results({
	@Result(name="logout", location="/Logout.html")
})
public class DocumentsAction extends AbstractActionBase
{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(DocumentsAction.class.getName());

	List<DocumentInfo> documents;
	Long delete = null;

	Long nowMillis = Calendar.getInstance(getLocale()).getTimeInMillis();
	Date now = new Date(nowMillis);
	Date caution = new Date(nowMillis + 21 * 24 * 3600);


	public String execute() throws Exception
	{

		log.info("In execute method of class List");
		
		if (delete != null)
		{
			DocumentInfo doc = dao.getDocumentInfo(delete);
			if (doc != null)
			{
				dao.deleteDocumentInfo(doc);
			}
		}

		String userName = principalProxy.getUserPrincipal().getName();
		log.debug("userName " + userName);
		User user = dao.getUser(userName);
		log.debug("user.Name " + user.getMail());
		documents = dao.listDocumentInfo(user);

		Collections.sort(documents, new Comparator<DocumentInfo>()
		{

			// N.B. documents are alreadey sorted by title
			@Override
			public int compare( DocumentInfo o1, DocumentInfo o2 )
			{
				return o2.getState().ordinal() - o1.getState().ordinal();
			}
		});

		return SUCCESS;

	}


	boolean expired( DocumentInfo documentInfo )
	{
		return !documentInfo.getExpires().after(now);
	}


	boolean expiring( DocumentInfo documentInfo )
	{
		return !(documentInfo.getExpires().after(caution) && expired(documentInfo));
	}


	public String encoded( String unencoded )
	{
		try
		{
			String encoded = URLEncoder.encode(unencoded, "UTF-8");
			return encoded;
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}


	public void setDocuments( List<DocumentInfo> documents )
	{
		this.documents = documents;
	}


	public List<DocumentInfo> getDocuments()
	{
		return documents;
	}


	public void setDelete( Long delete )
	{
		this.delete = delete;
	}
}
