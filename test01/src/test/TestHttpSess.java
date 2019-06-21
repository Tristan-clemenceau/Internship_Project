package test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import servlet.Utilisateur;

/**
 * Servlet implementation class TestHttpSess
 */
@WebServlet("/TestHttpSess")
public class TestHttpSess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestHttpSess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html"); 
        PrintWriter out = response.getWriter(); 
        //Utilisateur test = new Utilisateur("yreazi","rezar","raez","reza","raez");
        
        HttpSession session=request.getSession(false);
        //HttpSession session=request.getSession();
        //session.setAttribute("util",test);
        //session.invalidate();
        
        if(session != null) {
        	out.print("<br/>ok session créer");
        	out.print("<br/>"+session.getId());
        	//out.print("<br/> "+test.getUsername());
        }else {
        	System.out.println("session off");
        }
        
        out.print("<br/>fin test ");
        out.close(); 
        
        //GET INFO USER WITH JSON ANSWER (NOT SECURED)
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
