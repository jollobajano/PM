package jollobajano.pm.esb;

import java.io.ByteArrayInputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentInfo;


public class DocAttachementBuilder implements AttachementBuilder
{
	
	public static final String MIME_TYPE = "application/msword";

	public MimeBodyPart getAttachement( DocumentInfo documentInfo )  throws Exception
	{
		// Part two is attachment
		MimeBodyPart messageBodyPart = new MimeBodyPart();

		
		DataSource source = 
			new ByteArrayDataSource(
					new ByteArrayInputStream(DAO.getInstance().getDoc(documentInfo)),
					MIME_TYPE);

		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(MimeUtility.encodeText(documentInfo.getTitle() + ".doc"));
		messageBodyPart.setHeader("Content-Type", "application/msword");
		return messageBodyPart;
	}

}
