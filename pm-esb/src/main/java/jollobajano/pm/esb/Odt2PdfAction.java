package jollobajano.pm.esb;

import java.io.File;

import org.jboss.soa.esb.actions.annotation.OnException;
import org.jboss.soa.esb.actions.annotation.OnSuccess;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.message.Message;

public class Odt2PdfAction extends BaseAction
{

	@Process
	public Message process( Message message ) throws Exception
	{

		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Executing " + this.getClass());
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

		File dir = new File(message.getBody().get("DocFilePath").toString()).getParentFile();

		if (!dir.exists())
			dir.mkdirs();

		java.lang.Process process = Runtime.getRuntime().exec(
				new String[] { "/usr/bin/libreoffice", "--headless", "--convert-to", "pdf",
						message.getBody().get("OdtFilePath").toString(), "--outdir", dir.toString() });
		process.waitFor();

		String pdfFileName = message.getBody().get("FileName").toString();
		pdfFileName = pdfFileName.substring(0, pdfFileName.lastIndexOf(".") + 1) + "pdf";
		message.getBody().add("PdfFileName", pdfFileName);

		String pdfPath = new File(dir, pdfFileName).getCanonicalPath();
		message.getBody().add("PdfFilePath", pdfPath);

		return message;
	}


	@OnException
	@OnSuccess
	public void cleanUp( Message message ) throws Exception
	{
		File pdf = new File( message.getBody().get("PdfFilePath").toString() );
		pdf.delete();
	}
}
