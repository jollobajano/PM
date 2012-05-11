package jollobajano.pm.esb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.jboss.soa.esb.actions.annotation.OnException;
import org.jboss.soa.esb.actions.annotation.OnSuccess;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.configure.ConfigProperty;
import org.jboss.soa.esb.configure.ConfigProperty.Use;
import org.jboss.soa.esb.message.Message;

public class Doc2OdtAction extends BaseAction {

	String suffix = "odt";
	String fromName = "DocFilePath";
	File dir = null;
	Boolean deleteFile = true;



	@Process
	public Message process( Message message ) throws Exception {
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Executing " + this.getClass());
		File docFile = new File(message.getBody().get(fromName).toString());
		String docFileStr = docFile.getCanonicalPath();
		String fileName = docFile.getName();
		if (dir == null)
			dir = docFile.getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		java.lang.Process process = Runtime.getRuntime().exec(
				new String[] { "/usr/bin/libreoffice", "--headless", "--convert-to", suffix,
						message.getBody().get(fromName).toString(), "--outdir", dir.toString() });
		process.waitFor();
		String odtFilePath = new File(docFileStr.substring(0, docFileStr.lastIndexOf(".") + 1) + "odt")
				.getCanonicalPath();
		log.debug(odtFilePath);
		message.getBody().add("OdtFilePath", odtFilePath);
		message.getBody().add(getFileContents(odtFilePath));
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		return message;
	}



	@OnException
	@OnSuccess
	public void cleanUp( Message message ) throws Exception {
		if (deleteFile) {
			try {
				File odt = new File(message.getBody().get("OdtFilePath").toString());
				// odt.delete();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}



	byte[] getFileContents( String path ) {
		try {
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			FileInputStream stream = new FileInputStream(path);
			if (stream.available() > 0) {
				while (stream.available() > 0) {
					byte[] buffer = new byte[stream.available()];
					stream.read(buffer);
					result.write(buffer, 0, buffer.length);
				}
			}
			return result.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}



	@ConfigProperty(use = Use.OPTIONAL)
	public void setDeleteFile( Boolean deleteFile ) {
		this.deleteFile = deleteFile;
	}
}
