package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class RemiseAzero
 */
@WebServlet("/RemiseAzero")
public class RemiseAzero extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemiseAzero() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*VERIFICATION DEFAULT*/
		HttpSession session=request.getSession(false);
		
		if(session == null) {
			System.out.println("[RAZ]:0");
			response.sendRedirect(request.getContextPath() + "/Index");
			//RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/Index");
		    //dispatcher.forward(request, response);
		}else {
			System.out.println("[RAZ]:1");
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/Index");
			//RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/Index");
		    //dispatcher.forward(request, response);
		}
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
