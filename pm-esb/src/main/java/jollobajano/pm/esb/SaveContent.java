package jollobajano.pm.esb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jboss.soa.esb.actions.annotation.OnException;
import org.jboss.soa.esb.actions.annotation.OnSuccess;
import org.jboss.soa.esb.actions.annotation.Process;
import org.jboss.soa.esb.configure.ConfigProperty;
import org.jboss.soa.esb.message.Message;

public class SaveContent extends BaseAction
{

	File dir = null;


	@Process
	public Message process( Message message ) throws Exception
	{

		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		log.debug("Executing " + this.getClass());

		if (!dir.exists())
			dir.mkdirs();

		File outFile = File.createTempFile("PM-", ".doc", dir);

		try
		{
			FileOutputStream file = new FileOutputStream(outFile);
			file.write((byte[]) message.getBody().get());
			file.flush();
			file.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		message.getBody().add("FileName", outFile.getName());
		message.getBody().add("DocFilePath", outFile.getCanonicalPath());
		log.debug("FileName: " + outFile.getName());
		log.debug("DocFilePath: " + outFile.getCanonicalPath());
		log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

		return message;

	}


	@ConfigProperty
	public void setDir( File dir )
	{
		this.dir = dir;
	}
	

	@OnException
	@OnSuccess
	public void cleanUp( Message message ) throws Exception
	{
		File doc = new File( message.getBody().get("DocFilePath").toString() );
		doc.delete();
	}
}
