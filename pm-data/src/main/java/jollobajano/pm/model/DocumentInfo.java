package jollobajano.pm.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DocumentInfo implements Serializable, Comparable<DocumentInfo>
{

	public enum State
	{
		VALID, EXPIRING, EXPIRING_NOTIFIED, EXPIRED, EXPIRED_NOTIFIED;
	}

	private static final long serialVersionUID = 8023996808396935598L;
	
	public static final String ATTRIBUTE_NAME = DocumentInfo.class.getCanonicalName();

	Long id;
	State state = State.VALID;
	String audience;
	Date created;
	Date updated;
	Date expires;
	String title;
	String type;
	String chapter;
	String page;
	String author;
	String supervisor;
	String sender;
	Date submitted;
	List<Header> headers = new ArrayList<Header>();
	byte[] doc = new byte[0];
	byte[] pdf = new byte[0];


	public void add( Header header )
	{
		for (Header h : headers)
		{
			if (h.getName().equals(header.getName()))
			{
				h.setValue(header.getValue());
				return;
			}
		}
		headers.add(header);
		header.setDocumentInfo(this);
	}


	public int compareTo( DocumentInfo o )
	{
		return id - o.id > 0 ? 1 : id - o.id < 0 ? -1 : 0;
	}


	public String find( String name )
	{
		for (Header h : headers)
		{
			if (h.getName().equals(name))
			{
				return h.getValue();
			}
		}
		return null;
	}


	@Column(nullable=false)
	public String getAudience()
	{
		return audience;
	}


	@Column(nullable=false)
	public String getAuthor()
	{
		return author;
	}


	public String getChapter()
	{
		return chapter;
	}


	@Column(columnDefinition="DATE NOT NULL", nullable=false)
	public Date getCreated()
	{
		return created;
	}


	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=100000, nullable=false)
	public byte[] getDoc()
	{
		return doc;
	}


	@Column(columnDefinition="DATE NOT NULL", nullable=false)
	public Date getExpires()
	{
		return expires;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "DOCUMENTINFO_ID", nullable = false, updatable = false)
	public List<Header> getHeaders()
	{
		return headers;
	}



	@Id
	@GeneratedValue
	@Column(nullable=false)
	public Long getId()
	{
		return id;
	}


	public String getPage()
	{
		return page;
	}


	@Lob 
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1000000, nullable=false)
	public byte[] getPdf()
	{
		return pdf;
	}


	public String getSender()
	{
		return sender;
	}


	@Enumerated(EnumType.STRING)
	public State getState()
	{
		return state;
	}


	@Column(columnDefinition="DATE NOT NULL", nullable=false)
	public Date getSubmitted()
	{
		return submitted;
	}


	@Column(nullable=false)
	public String getSupervisor()
	{
		return supervisor;
	}


	@Column(unique = true, nullable=false)
	public String getTitle()
	{
		return title;
	}


	public String getType()
	{
		return type;
	}


	@Column(columnDefinition="DATE NOT NULL", nullable=false)
	public Date getUpdated()
	{
		return updated;
	}


	public void setAudience( String audience )
	{
		this.audience = audience;
	}


	public void setAuthor( String author )
	{
		this.author = author;
	}


	public void setChapter( String chapter )
	{
		this.chapter = chapter;
	}


	public void setCreated( Date created )
	{
		this.created = created;
	}


	public void setDoc( byte[] doc )
	{
		this.doc = doc;
	}


	public void setExpires( Date expires )
	{
		this.expires = expires;
	}


	public void setHeaders( List<Header> headers )
	{
		this.headers = headers;
	}


	public void setId( Long id )
	{
		this.id = id;
	}


	public void setPage( String page )
	{
		this.page = page;
	}


	public void setPdf( byte[] pdf )
	{
		this.pdf = pdf;
	}


	public void setSender( String sender )
	{
		this.sender = sender;
	}


	public void setState( State state )
	{
		this.state = state;
	}


	
	public void setSubmitted( Date submitted )
	{
		this.submitted = submitted;
	}


	
	public void setSupervisor( String supervisor )
	{
		this.supervisor = supervisor;
	}


	
	public void setTitle( String title )
	{
		this.title = title;
	}


	
	public void setType( String type )
	{
		this.type = type;
	}


	public void setUpdated( Date updated )
	{
		this.updated = updated;
	}


	
	public String toLongString()
	{
		return "DocumentInfo [id=" + id + ", state=" + state + ", audience=" + audience + ", created=" + created
				+ ", updated=" + updated + ", expires=" + expires + ", title=" + title + ", type=" + type
				+ ", chapter=" + chapter + ", page=" + page + ", author=" + author + ", supervisor=" + supervisor
				+ ", doc=" + doc + ", pdf=" + pdf + ", headers=" + headers + "]";
	}


	
	@Override
	public String toString()
	{
		return "DocumentInfo [id=" + id + ", state=" + state + ", audience=" + audience + ", created=" + created
				+ ", updated=" + updated + ", expires=" + expires + ", title=" + title + ", type=" + type
				+ ", chapter=" + chapter + ", page=" + page + ", author=" + author + ", supervisor=" + supervisor
				+ ", sender=" + sender + ", submitted=" + submitted + ", headers=" + headers + "]";
	}

}
