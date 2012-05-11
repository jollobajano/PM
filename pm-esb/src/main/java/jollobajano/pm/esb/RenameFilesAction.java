package jollobajano.pm.esb;

import java.io.File;
import java.io.IOException;

import jollobajano.pm.model.DocumentInfo;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class RenameFilesAction extends BaseAction
{

	public RenameFilesAction(ConfigTree config)
	{

	}


	public Message process(Message message) throws Exception
	{
		try
		{
			log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			log.debug("Executing " + this.getClass());

			DocumentInfo documentinfo = (DocumentInfo) message.getBody().get();
			rename(message, "DocFilePath", documentinfo.getTitle(), "doc");
			rename(message, "OdtFilePath", documentinfo.getTitle(), "odt");
			rename(message, "PdfFilePath", documentinfo.getTitle(), "pdf");

			log.debug("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return message;
	}


	void rename(Message message, String headername, String title, String extension)
	{
		try
		{
			String original = message.getBody().get(headername).toString();
			File from = new File(original);
			File directory = from.getParentFile();
			File newFile = new File(directory, title + "." + extension);
			from.renameTo(newFile);
			message.getBody().add(headername, newFile.getCanonicalFile());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
