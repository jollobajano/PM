package jollobajano.pm.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jollobajano.pm.model.DAO;
import jollobajano.pm.model.DocumentInfo;

/**
 * Servlet implementation class DocumentRelay
 * 
 * URL consists of
 * <p>
 * http://com/DocumentRelay/<doc type>/<document-ID>/<title>
 * <p>
 * eg: <br>
 * 
 * http://com/DocumentRelay/125/doc/Min%32Sommar.doc
 * <p>
 * 
 * 
 */
public class DocumentRelay extends HttpServlet
{

	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentRelay()
	{
		super();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) http://com/DocumentRelay/125/doc/Min%32Sommar.doc
	 *      <p>
	 */
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
			IOException
	{
		try
		{
			String[] parameters = request.getPathInfo().split("/");
			
			System.out.println("request.getPathInfo(): " + request.getPathInfo());

			Long id = Long.valueOf(parameters[1]);
			String type = parameters[2];

			DAO dao = DAO.getInstance();
			DocumentInfo documentInfo = dao.getDocumentInfo(id);

			if (type.equals("doc"))
			{
				
				byte[] doc = dao.getDoc(documentInfo);
				response.setContentType("application/msword");
				copyStreamContents(new ByteArrayInputStream(doc), response.getOutputStream());
				
			} else
			{
				
				byte[] pdf = dao.getPdf(documentInfo);
				response.setContentType("application/pdf");
				copyStreamContents(new ByteArrayInputStream(pdf), response.getOutputStream());
				
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	


	void copyStreamContents( InputStream stream, OutputStream result )
	{
		try
		{

			if (stream.available() > 0)
			{
				while (stream.available() > 0)
				{
					byte[] buffer = new byte[stream.available()];
					stream.read(buffer);
					result.write(buffer, 0, buffer.length);
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
