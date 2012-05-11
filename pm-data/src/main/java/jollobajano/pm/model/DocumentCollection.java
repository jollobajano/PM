package jollobajano.pm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class DocumentCollection
{
	public static final String ATTRIBUTE_NAME = 
		DocumentCollection.class.getCanonicalName().toLowerCase();
	
	Long id;
	List<User> users = new ArrayList<User>();
	List<DocumentInfo> documents = new ArrayList<DocumentInfo>();
	List<CarbonCopy> carbonCopies = new ArrayList<CarbonCopy>();
	
	
	public void add( DocumentInfo documentInfo )
	{
		
		for (DocumentInfo document : new ArrayList<DocumentInfo>(documents))
		{
			if (documentInfo.getTitle().equals(document.getTitle()))
			{
				documents.remove(document);
			}
		}

		documents.add(documentInfo);
	}
	
	
	public void add( User user )
	{
		users.add(user);
		user.setDocumentCollection(this);
	}
	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENTCOLLECTION_ID", nullable = false, updatable = false)
	public List<CarbonCopy> getCarbonCopies()
	{
		return carbonCopies;
	}


	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENTCOLLECTION_ID", nullable = false, updatable = false)
	public List<DocumentInfo> getDocuments()
	{
		return documents;
	}
	
	@Id
	@GeneratedValue
	public Long getId()
	{
		return id;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENTCOLLECTION_ID", nullable = false, updatable = false)
	public List<User> getUsers()
	{
		return users;
	}
	
	public void setCarbonCopies( List<CarbonCopy> carbonCopies )
	{
		this.carbonCopies = carbonCopies;
	}
	
	public void setDocuments( List<DocumentInfo> documents )
	{
		this.documents = documents;
	}


	
	public void setId( Long id )
	{
		this.id = id;
	}


	
	public void setUsers( List<User> users )
	{
		this.users = users;
	}
}
