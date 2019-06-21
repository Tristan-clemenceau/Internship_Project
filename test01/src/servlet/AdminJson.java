package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class AdminJson
 */
@WebServlet("/AdminJson")
public class AdminJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String msg,statut; 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminJson() {
    	this.statut = "DEFAULT";
    	this.msg = "DEFAULT";
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

	public void redirectService(String service,String title,String content,Utilisateur util) {
		switch(service) {
			case "1" ://alert
				this.setAlert(title,content,util);
				break;
			case "2" ://Ticket
				break;
			case "3" ://User
				break;
			case "4" ://get Data
				break;
		}
	}
	
	public void setAlert(String title,String content,Utilisateur util) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
		int i = 0;
		
		Connection conn = null;
    	PreparedStatement ps;
    	
		try {
			ps = conn.prepareStatement("INSERT INTO alert (Alert_Id, Alert_Redacteur, Alert_Title, Alert_Content, Alert_Date) VALUES (NULL,?, ?, ?, ?)");
		
			ps.setInt(1,Integer.valueOf(util.getId()));
		    ps.setString(2,title);  
		    ps.setString(3,content);  
		    ps.setString(4,dateFormat.format(date));  
		    		          
		    i=ps.executeUpdate();
		    
		    conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	    if(i>0) {
	    	this.statut ="SUCCESS";
	    	this.msg ="You are successfully registered";
	    }else {
	    	this.statut ="ERROR";
	    	this.msg ="You are not successfully registered";
	    }
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminJson admin = new AdminJson();
		JSONObject jsonObjGlobal = new JSONObject();
        JSONArray jsonArray =new JSONArray();
        
        Utilisateur util = new Utilisateur();
		Object objSession = new Object();
		
		String service = request.getParameter("service");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		
		response.setContentType("application/json");
		
		HttpSession session = request.getSession(false);
		
		if(session == null && service == null) {
			admin.setMsg("Requete erronée");
			admin.setStatut("ERROR");
		}else {
			objSession = session.getAttribute("utilisateur");
			util = (Utilisateur) objSession;
			this.redirectService(service, title, content,util);
			
		}
		
		jsonObjGlobal.put("statut", admin.getStatut());
		jsonObjGlobal.put("msg", admin.getMsg());
		
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
