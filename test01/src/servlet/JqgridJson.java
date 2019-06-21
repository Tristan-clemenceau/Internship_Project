package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class JqgridJson
 */
@WebServlet("/JqgridJson")
public class JqgridJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JqgridJson() {
    }
	
	public void redirect(String service,Utilisateur util,JSONObject jsonObjGlobal,JSONArray jsonArray) {
		System.out.println("service :" +service);
		switch(service) {
			case "1"://NORMAL
				this.setResponseHistorique(util,jsonObjGlobal,jsonArray);
				break;
			case "2"://ADMIN
				//this.setResponseAdmin();
				break;
		}
	}
	
	public void setResponseHistorique(Utilisateur util,JSONObject jsonObjGlobal,JSONArray jsonArray) {
		Connection conn = null;
		
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
			Statement stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery("SELECT * FROM ticket WHERE Ticket_Buyer = "+Integer.valueOf(util.getId())); 
			    
			while(rs.next()) {
			    	//System.out.println(rs.getInt(1)+"---------"+rs.getInt(2)+"---------"+rs.getString(3)+"---------"+rs.getString(4));
				JSONObject jsonObjSecondaire = new JSONObject();
				
				jsonObjSecondaire.put("Ticket_id",rs.getInt(1));
				jsonObjSecondaire.put("Ticket_Buyer",rs.getInt(2));
				jsonObjSecondaire.put("Ticket_Date",rs.getString(3));
				jsonObjSecondaire.put("Ticket_type",rs.getString(4));
				
				jsonArray.put(jsonObjSecondaire);
			}
			
			jsonObjGlobal.put("rows", jsonArray);
			
			 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setResponseAdmin() {
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonObjGlobal = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        
		JqgridJson jqgrid = new JqgridJson();
		Utilisateur util = new Utilisateur();
		Object objSession = new Object();
		
		String service = request.getParameter("service");
		
		response.setContentType("application/json");
		
		HttpSession session = request.getSession(false);
		
		if(session == null && service == null) {
			System.out.println("ERREUR");
		}else {
			objSession = session.getAttribute("utilisateur");
			util = (Utilisateur) objSession;
			jqgrid.redirect(service,util,jsonObjGlobal,jsonArray);
			
		}
		jsonObjGlobal.write(response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
