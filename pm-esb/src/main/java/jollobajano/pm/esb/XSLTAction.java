package jollobajano.pm.esb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jollobajano.pm.model.Header;

import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.configure.ConfigProperty;
import org.jboss.soa.esb.configure.ConfigProperty.Use;
import org.jboss.soa.esb.message.Message;
import org.xml.sax.InputSource;

public class XSLTAction extends BaseAction {

	String xsl;
	String fileNameHeader;
	String headerName;



	@Process
	public Message process( Message message ) throws Exception {
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Executing " + this.getClass());
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xsl);
			InputSource inputSource = new InputSource(inputStream);
			SAXSource saxSource = new SAXSource(inputSource);
			Transformer transformer = TransformerFactory.newInstance().newTransformer(saxSource);
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			StreamResult streamResult = new StreamResult(byteOutputStream);
			InputStream xmlInputStream;
			if (fileNameHeader != null)
				xmlInputStream = new FileInputStream(message.getBody().get(fileNameHeader).toString());
			else if (headerName != null)
				xmlInputStream = new ByteArrayInputStream((byte[]) message.getBody().get(headerName));
			else
				xmlInputStream = new ByteArrayInputStream((byte[]) message.getBody().get());
			StreamSource xmlStreamSource = new StreamSource(xmlInputStream);
			for (String name : message.getBody().getNames()) {
				String value = message.getBody().get(name).toString();
				if (value.length() < 200)
					transformer.setParameter(name, value);
			}
			transformer.transform(xmlStreamSource, streamResult);
			if (fileNameHeader != null) {
				File in = new File(message.getBody().get(fileNameHeader).toString());
				File out = File.createTempFile("PM-", ".odt", in.getParentFile());
				new FileOutputStream(out).write(byteOutputStream.toByteArray());
				out.renameTo(in.getAbsoluteFile());
			} else if (headerName != null)
				message.getBody().add(headerName, byteOutputStream.toByteArray());
			else
				message.getBody().add(byteOutputStream.toByteArray());
			log.debug("XSLT result: " + byteOutputStream.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		return message;
	}



	@ConfigProperty
	public void setXsl( String xsl ) {
		this.xsl = xsl;
	}



	@ConfigProperty(use = Use.OPTIONAL)
	public void setFileNameHeader( String fileNameHeader ) {
		this.fileNameHeader = fileNameHeader;
	}



	@ConfigProperty(use = Use.OPTIONAL)
	public void setHeaderName( String headerName ) {
		this.headerName = headerName;
	}
}
