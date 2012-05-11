package jollobajano.pm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;


@Entity
public class CarbonCopy
{
	public enum Event
	{
		SUBMIT("Insänt"), 
		EXPIRING("Utgående"), 
		EXPIRED("Utgått");
		
		String text;
		Event(String text)
		{
			this.text = text;
		}
		
		public String getText()
		{
			return text;
		}
	}
	
	Long id;
	String mail;
	Set<Event> events = new HashSet<Event>();
	
	
	@CollectionOfElements(fetch=FetchType.EAGER)
	public Set<Event> getEvents()
	{
		return events;
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
	
	public void setEvents( Set<Event> events )
	{
		this.events = events;
	}

	
	public void setId( Long id )
	{
		this.id = id;
	}

	
	public void setMail( String mail )
	{
		this.mail = mail;
	}

}
