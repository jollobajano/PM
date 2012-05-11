package jollobajano.pm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CollectionOfElements;

@Entity
public class User
{
	
	public static final String ATTRIBUTE_NAME = User.class.getCanonicalName().toLowerCase();

	Long id;
	String mail;
	String password;
	DocumentCollection documentCollection;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="DOCUMENTCOLLECTION_ID", nullable=false, insertable = false, updatable = false)	
    public DocumentCollection getDocumentCollection()
	{
		return documentCollection;
	}


	@Id
	@GeneratedValue
	public Long getId()
	{
		return id;
	}


	public String getMail()
	{
		return mail;
	}


	public String getPassword()
	{
		return password;
	}


	public void setDocumentCollection( DocumentCollection documentCollection )
	{
		this.documentCollection = documentCollection;
	}


	public void setId( Long id )
	{
		this.id = id;
	}


	public void setMail( String mail )
	{
		this.mail = mail;
	}


	
	public void setPassword( String password )
	{
		this.password = password;
	}
}
