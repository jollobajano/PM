package jollobajano.pm.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String redirect;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
    }

	@Override
	public void init( ServletConfig config ) throws ServletException
	{
		super.init(config);
		redirect = config.getInitParameter("redirect");
		System.out.println("*************************************************");
		System.out.println("redirect: " + redirect);
		System.out.println("*************************************************");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("redirect: " + redirect);
		request.getSession().invalidate();
		request.getRequestDispatcher(redirect).include(request, response);
	}

}
