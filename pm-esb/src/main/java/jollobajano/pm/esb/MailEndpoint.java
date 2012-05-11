package jollobajano.pm.esb;

import javax.mail.Address;
import javax.mail.Message.RecipientType;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.jboss.resource.adapter.mail.inflow.MailListener;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.listeners.jca.InflowGateway;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;

import javax.mail.Part;

public class MailEndpoint implements InflowGateway, MailListener
{
	
	private static final Logger log = Logger.getLogger("PM");

	private ServiceInvoker invoker;


	public void setServiceInvoker(final ServiceInvoker invoker)
	{
		this.invoker = invoker;
	}


	public void onMessage(final javax.mail.Message mailMessage)
	{
		MimeMessage mimeMessage = (MimeMessage) mailMessage;
		try
		{
			javax.mail.internet.MimeMultipart mmp = (javax.mail.internet.MimeMultipart) mailMessage.getContent();
			log.info("Recieved mail from: " + mimeMessage.getFrom()[0]);

			for (int i = 0, n = mmp.getCount(); i < n; i++)
			{
				javax.mail.Part part = mmp.getBodyPart(i);
				String disposition = part.getDisposition();
				javax.mail.internet.MimeBodyPart mbp = (javax.mail.internet.MimeBodyPart) part;

				if (mbp.isMimeType("application/msword"))
				{
					String fileName = javax.mail.internet.MimeUtility.decodeText(mbp.getFileName());
					log.info("    Attachement: " + fileName);
					sendMessage(fileName, getContentFromStream(mbp.getInputStream()), mimeMessage);
				}
			}
		} catch (javax.mail.MessagingException e)
		{
			e.printStackTrace();
		} catch (java.io.IOException ioe)
		{
			ioe.printStackTrace();
		}
	}


	byte[] getContentFromStream(java.io.InputStream stream) throws java.io.IOException
	{
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		if (stream.available() > 0)
		{
			while (stream.available() > 0)
			{
				byte[] buffer = new byte[stream.available()];
				stream.read(buffer);
				result.write(buffer, 0, buffer.length);
			}
		}
		return result.toByteArray();
	}


	public void sendMessage(final String fileName, final byte[] content, final MimeMessage mailMessage)
	{
		final Message message = MessageFactory.getInstance().getMessage();
		final Body body = message.getBody();
		
		System.out.println("message class: " + message.getClass());

		try
		{
			body.add(content);

			add(body, "FileName", fileName);
			add(body, "FileBaseName", fileName.substring(0, fileName.lastIndexOf('.')));
			add(body, "MessageID", mailMessage.getMessageID());
			add(body, "MailSubject", mailMessage.getSubject());
			add(body, "MailContentType", mailMessage.getContentType());
			add(body, "MailFrom", mailMessage.getFrom()[0]);
			add(body, "MailReplyTo", mailMessage.getReplyTo()[0]);

			addAddresses(body, "MailTo", mailMessage.getRecipients(RecipientType.TO));
			addAddresses(body, "MailCC", mailMessage.getRecipients(RecipientType.CC));

			add(body, "MailReceivedDate", mailMessage.getReceivedDate());
			add(body, "MailSentDate", mailMessage.getSentDate());

			add(body, "MailFolder", mailMessage.getFolder().getFullName());

			invoker.deliverAsync(message);
		} catch (final Exception ex)
		{
			// Handle error conditions
			System.err.println("Unexpected error: " + ex);
			ex.printStackTrace();
		}
	}


	private void add(final Body body, final String name, final Object value)
	{
		if (value != null)
		{
			body.add(name, value);
		}
	}


	private void addAddresses(final Body body, final String prefix, final Address[] addresses)
	{
		final int numAddresses = (addresses == null ? 0 : addresses.length);
		for (int count = 0; count < numAddresses; count++)
		{
			final Address address = addresses[count];
			body.add(prefix + (count + 1), address.toString());
		}
	}
}
