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
 * Servlet implementation class AlertJson
 */
@WebServlet("/AlertJson")
public class AlertJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String msg,statut;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlertJson() {
        super();
        // TODO Auto-generated constructor stub
    }

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	
	public void getAlertFromDB(JSONObject jsonObjGlobal,JSONArray jsonArray) {
		Connection conn = null;
		
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
			Statement stmt = conn.createStatement(); 
			ResultSet rs = stmt.executeQuery("SELECT * FROM alert"); 
			    
			while(rs.next()) {
				JSONObject jsonObjSecondaire = new JSONObject();
			
				jsonObjSecondaire.put("Alert_id",rs.getInt(1));
				jsonObjSecondaire.put("Alert_writer",rs.getInt(2));
				jsonObjSecondaire.put("Alert_title",rs.getString(3));
				jsonObjSecondaire.put("Alert_content",rs.getString(4));
				jsonObjSecondaire.put("Alert_date",rs.getString(5));
				
				jsonArray.put(jsonObjSecondaire);
			}
			this.setStatut("SUCCESS");
			this.setMsg("Cartes chargé correctement");
			
			jsonObjGlobal.put("rows", jsonArray);
			jsonObjGlobal.put("msg", this.msg);
			jsonObjGlobal.put("statut", this.statut);
			
			 conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.setStatut("ERROR");
			this.setMsg(e.toString());
		}
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject jsonObjGlobal = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        
        AlertJson alert = new AlertJson();
		
		response.setContentType("application/json");
		
		alert.getAlertFromDB(jsonObjGlobal, jsonArray);
		
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
