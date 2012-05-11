package jollobajano.pm.esb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentCollection;
import jollobajano.pm.model.DocumentInfo;
import jollobajano.pm.model.Header;

import org.hibernate.Hibernate;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.message.Message;


public class PersistDocumentInfo extends BaseAction
{

	@Process
	public Message process( Message message ) throws Exception
	{
		DAO dao = DAO.getInstance();

		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Executing " + this.getClass());

		try
		{
			DocumentInfo submitted = populatedDocumentInfo(message);
			DocumentCollection documentCollection = dao.getDocumentCollectionByUserName(submitted.getSender());

			log.debug("DocumentInfo about to be persisted: " + submitted);
			
			documentCollection.add(submitted);
			log.debug("Possibly deleting obsolete DocumentInfo");
			dao.deleteDocumentInfo(submitted.getTitle()); // dubletter
			log.debug("Persisting new DocumentInfo w/o Headers");
			dao.saveOrUpdate(documentCollection);
			// dao.saveDocumentInfo(submitted);

			getHeaders(message, submitted);

			log.debug("Persisting DocumentInfo w/ Headers");
			dao.saveOrUpdate(submitted);

			log.info("Persisted: " + submitted);
		} catch (Exception e)
		{
			e.printStackTrace();
			log.debug("Kastar exception vidare");
			throw e;
		}

		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		return message;
	}


	DocumentInfo populatedDocumentInfo( Message message ) throws FileNotFoundException, IOException
	{
		DocumentInfo submitted = (DocumentInfo) message.getBody().get();

		byte[] doc = getFileContents(message.getBody().get("DocFilePath").toString());
		byte[] pdf = getFileContents(message.getBody().get("PdfFilePath").toString());
		submitted.setDoc(doc);
		submitted.setPdf(pdf);
		// submitted.setDoc(Hibernate.createBlob(new FileInputStream(doc)));
		// submitted.setPdf(Hibernate.createBlob(new FileInputStream(pdf)));

		submitted.setSubmitted(new Date(Calendar.getInstance(new Locale("sv", "SE")).getTimeInMillis()));
		submitted.setSender(getSender(message));

		return submitted;
	}


	void getHeaders( Message message, DocumentInfo documentInfo )
	{
		for (String name : message.getBody().getNames())
		{
			String value = message.getBody().get(name).toString();
			if (value.length() < 200)
				documentInfo.add(new Header(name, value));
		}
	}


	String getSender( Message message )
	{
		String sender = message.getBody().get("MailFrom").toString();
		sender = sender.indexOf('<') >= 0 ? sender.substring(sender.indexOf('<') + 1, sender.indexOf('>')) : sender;
		return sender;
	}
}
