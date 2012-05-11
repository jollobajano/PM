package jollobajano.pm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Header implements Serializable
{

	Long id;
	String name;
	String value;
	DocumentInfo documentInfo;


	public Header()
	{
		super();
	}


	public Header(String name, String value)
	{
		this();
		this.name = name;
		this.value = value;
	}


    @ManyToOne
    @JoinColumn(name = "DOCUMENTINFO_ID", nullable = false, updatable=false, insertable=false)
	public DocumentInfo getDocumentInfo()
	{
		return documentInfo;
	}


	@Id
	@GeneratedValue
	public Long getId()
	{
		return id;
	}


	public String getName()
	{
		return name;
	}


	public String getValue()
	{
		return value;
	}


	public void setDocumentInfo( DocumentInfo documentInfo )
	{
		this.documentInfo = documentInfo;
	}


	public void setId( Long id )
	{
		this.id = id;
	}


	public void setName( String name )
	{
		this.name = name;
	}


	
	public void setValue( String value )
	{
		this.value = value;
	}


	
	@Override
	public String toString()
	{
		return "Header#" + id + "(" + name + "=" + value + ")";
	}
}
