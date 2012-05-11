package jollobajano.pm.esb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jollobajano.pm.model.DocumentInfo;

import org.apache.log4j.Logger;
import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;

public class BaseAction
{
	static final Logger log = Logger.getLogger("PM");

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


	void deleteFile( DocumentInfo persisted, String header )
	{
		try
		{
			String path = persisted.find(header);
			File file = path == null ? null : new File(path);

			log.debug("Delete File " + file);

			if (file.exists())
				file.delete();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
