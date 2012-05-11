package jollobajano.pm.esb;

import javax.mail.internet.MimeBodyPart;

import jollobajano.pm.model.DocumentInfo;

/**
 * Builds an MimeBodyPart from a java.sql.Blob or Clob obtained from the
 * database.
 * 
 * 
 * @author <a href="mailto:info@jollobajano.com">Mats Nyberg</a>
 * 
 */
public interface AttachementBuilder
{
	public MimeBodyPart getAttachement( DocumentInfo documentInfo ) throws Exception;
}
