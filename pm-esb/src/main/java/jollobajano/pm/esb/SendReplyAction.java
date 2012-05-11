package jollobajano.pm.esb;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import jollobajano.pm.model.DocumentInfo;

import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.configure.ConfigProperty;
import org.jboss.soa.esb.configure.ConfigProperty.Use;
import org.jboss.soa.esb.message.Message;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SendReplyAction extends BaseAction
{

	private static Configuration cfg = new Configuration();

	protected String template;
	protected String attachementBuilderClass;
	String subject = "[PM] ${documentinfo.title}";
	String staticMail = null;


	@Process
	public Message process( Message message ) throws Exception
	{

		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Executing " + this.getClass());

		try
		{
			DocumentInfo documentInfo = (DocumentInfo) message.getBody().get();
			Map<String, Object> model = createModel(message);
			String contents = evalResource(template, model);
			String sendTo = staticMail != null ? staticMail : documentInfo.find("MailReplyTo") != null ? 
					documentInfo.find("MailReplyTo") : documentInfo.find("MailFrom");
			sendMessage(sendTo, message, contents);
		} catch (Throwable ex)
		{
			ex.printStackTrace();
			throw new Exception(ex.getMessage(), ex);
		}

		log.debug("Reply mail sent");
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

		return message;
	}


	Map<String, Object> createModel( Message message )
	{
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("documentinfo", message.getBody().get());

		Map<String, Object> messageMap = new HashMap<String, Object>();
		for (String name : message.getBody().getNames())
		{
			messageMap.put(name, message.getBody().get(name));
		}
		root.put("message", messageMap);
		return root;
	}


	String evalString( String templateStr, Map<String, Object> model ) throws Exception
	{
		Template template = new Template("name", new StringReader(templateStr), cfg);
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		OutputStreamWriter wr = new OutputStreamWriter(resultStream);
		template.process(model, wr);

		return resultStream.toString();
	}


	String evalResource( String templatePath, Map<String, Object> model ) throws Exception
	{
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream resource = classLoader.getResourceAsStream(templatePath);
		
		if(resource == null)
			throw new FileNotFoundException(templatePath);
		
		Template template = new Template("name", new InputStreamReader(resource), cfg);
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		OutputStreamWriter wr = new OutputStreamWriter(resultStream);
		template.process(model, wr);

		return resultStream.toString();
	}


	String eval( String templatePath, Map<String, Object> model ) throws Exception
	{
		try
		{
			cfg.setClassForTemplateLoading(getClass(), "");
			cfg.setLocale(new Locale("SE", "sv"));
			Template template = cfg.getTemplate(templatePath);

			ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
			OutputStreamWriter wr = new OutputStreamWriter(resultStream);
			template.process(model, wr);

			return resultStream.toString();
		} catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
	}


	void sendMessage( String to, Message message, String textContents ) throws Exception
	{

		DocumentInfo documentinfo = (DocumentInfo) message.getBody().get();
		Session session = null;

		log.debug("DocumentInfo: " + documentinfo);

		try
		{

			session = (Session) PortableRemoteObject.narrow(new InitialContext().lookup("java:Mail"), Session.class);

		} catch (javax.naming.NamingException e)
		{
			e.printStackTrace();
			throw e;
		}

		MimeMessage mailMessage = new MimeMessage(session);
		mailMessage.setFrom(new InternetAddress("mats@dialysen.se"));
		mailMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		mailMessage.setSubject(evalString(subject, createModel(message)));

		// GregorianCalendar calendar = new GregorianCalendar();
		String date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(new GregorianCalendar().getTime());

		mailMessage.setHeader("Date", date);

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();

		// Fill the message
		//messageBodyPart.setText(textContents);
		messageBodyPart.setContent(textContents, "text/plain; charset=iso-8859-1");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachment
		if (attachementBuilderClass != null)
		{
			AttachementBuilder attachementBuilder = (AttachementBuilder) Class.forName(attachementBuilderClass)
					.newInstance();
			multipart.addBodyPart(attachementBuilder.getAttachement(documentinfo));
		}
		// Put parts in message
		mailMessage.setContent(multipart);

		// Send the message
		Transport.send(mailMessage);
	}


	@ConfigProperty
	public void setTemplate( String template )
	{
		this.template = template;
	}


	@ConfigProperty(use = Use.OPTIONAL)
	public void setAttachementBuilderClass( String attachementBuilderClass )
	{
		this.attachementBuilderClass = attachementBuilderClass;
	}


	@ConfigProperty(use = Use.OPTIONAL)
	public void setSubject( String subject )
	{
		this.subject = subject;
	}


	@ConfigProperty(use = Use.OPTIONAL)
	public void setStaticMail( String staticMail )
	{
		this.staticMail = staticMail;
	}

}
