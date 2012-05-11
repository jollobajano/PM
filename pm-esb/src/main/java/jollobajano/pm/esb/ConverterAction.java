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

public class ConverterAction extends BaseAction
{

	String convertTo = "odt";
	String fromName = "DocFilePath";
	String toName = "OdtFilePath";
	String addContentsTo = null;
	File saveToDir = null;
	boolean saveNewFile = false;


	@Process
	public Message process( Message message ) throws Exception
	{

		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Executing " + this.getClass());

		if (saveToDir == null)
			saveToDir = new File(message.getBody().get(fromName).toString()).getParentFile();

		if (!saveToDir.exists())
			saveToDir.mkdirs();

		java.lang.Process process = Runtime.getRuntime().exec(
				new String[] { "/usr/bin/libreoffice", "--headless", "--convert-to", convertTo,
						message.getBody().get(fromName).toString(), "--outdir", saveToDir.toString() });
		process.waitFor();

		String odtFileName = message.getBody().get("FileName").toString();
		odtFileName = odtFileName.substring(0, odtFileName.lastIndexOf(".") + 1) + "odt";
		String odtFilePath = new File(saveToDir, odtFileName).getCanonicalPath();
		log.debug(odtFilePath);
		message.getBody().add(toName, odtFilePath);
		
		if(addContentsTo != null)
			message.getBody().add(addContentsTo, getFileContents(odtFilePath));
		else
			message.getBody().add(getFileContents(odtFilePath));

		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		return message;
	}


	@OnException
	@OnSuccess
	public void cleanUp( Message message ) throws Exception
	{
		if (!saveNewFile)
		{
			File odt = new File(message.getBody().get(toName).toString());
			odt.delete();
		}
	}


	byte[] getFileContents( String path )
	{
		try
		{
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			FileInputStream stream = new FileInputStream(path);
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
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}


	@ConfigProperty(name="convert-to-suffix")
	public void setConvertTo( String convertTo )
	{
		this.convertTo = convertTo;
	}


	
	@ConfigProperty(name="messagename-from-filepath")
	public void setFromName( String fromName )
	{
		this.fromName = fromName;
	}


	
	@ConfigProperty(name="messagename-to-filepath")
	public void setToName( String toName )
	{
		this.toName = toName;
	}


	
	@ConfigProperty(name="message-add-contents-to", use=Use.OPTIONAL)
	public void setAddContentsTo( String addContentsTo )
	{
		this.addContentsTo = addContentsTo;
	}


	
	@ConfigProperty(name="save-to-dir", use=Use.OPTIONAL)
	public void setSaveToDir( File saveToDir )
	{
		this.saveToDir = saveToDir;
	}


	
	@ConfigProperty(name="delete-file-on-success", use=Use.OPTIONAL)
	public void setSaveNewFile( boolean saveNewFile )
	{
		this.saveNewFile = saveNewFile;
	}
}
