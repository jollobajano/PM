package jollobajano.pm.esb;

import java.io.ByteArrayInputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentInfo;


public class PdfAttachementBuilder implements AttachementBuilder
{
	public static final String MIME_TYPE = "application/pdf";

	public MimeBodyPart getAttachement( DocumentInfo documentInfo )  throws Exception
	{
		// Part two is attachment
		MimeBodyPart messageBodyPart = new MimeBodyPart();

		
		DataSource source = 
			new ByteArrayDataSource(
					new ByteArrayInputStream(DAO.getInstance().getPdf(documentInfo)),
					MIME_TYPE);

		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(MimeUtility.encodeText(documentInfo.getTitle() + ".pdf"));
		messageBodyPart.setHeader("Content-Type", "application/pdf");
		return messageBodyPart;
	}
}
