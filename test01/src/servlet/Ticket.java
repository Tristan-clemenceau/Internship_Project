package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

import org.json.JSONObject;

/**
 * Servlet implementation class Ticket
 */
@WebServlet("/Ticket")
public class Ticket extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String statut,msg;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Ticket() {
    	this.msg="DEFAULT";
        this.statut = "DEFAULT";
    }
    
	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void ajoutTicket(HttpSession session,String ticket_enfant,String ticket_adulte,String ticket_vieux) {
		Connection conn = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Utilisateur util = new Utilisateur();
		
		Object objSession = session.getAttribute("utilisateur");
		util = (Utilisateur) objSession;
		
		int i = 0;
		
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
    	PreparedStatement ps;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
			ps = conn.prepareStatement("INSERT INTO ticket (Ticket_id, Ticket_Buyer, Ticket_Date, Ticket_type) VALUES (NULL,?, ?, ?)");
		    
		    for(int cpt = 0; cpt<Integer.valueOf(ticket_enfant);cpt++) {
		    	ps.setInt(1,Integer.valueOf(util.getId()));
			    ps.setString(2,dateFormat.format(date));
			    ps.setString(3,"Enfant");  
			    
			    i = ps.executeUpdate();
		    }
		    for(int cpt = 0; cpt<Integer.valueOf(ticket_adulte);cpt++) {
		    	ps.setInt(1,Integer.valueOf(util.getId()));
			    ps.setString(2,dateFormat.format(date));
			    ps.setString(3,"Adulte");  
			    
			    i = ps.executeUpdate();
		    }
		    for(int cpt = 0; cpt<Integer.valueOf(ticket_vieux);cpt++) {
		    	ps.setInt(1,Integer.valueOf(util.getId()));
			    ps.setString(2,dateFormat.format(date));
			    ps.setString(3,"Personnes agées");  
			    
			    i = ps.executeUpdate();
		    }
		    conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	    if(i>0) {
	    	this.statut ="SUCCESS";
	    	this.msg ="Les tickets ont bien été reservés";
	    }else {
	    	this.statut ="ERROR";
	    	this.msg ="Impossible de reserver les tickets";
	    }
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Ticket ticket = new Ticket();
		JSONObject utilInfo = new JSONObject();
		
		String ticket_enfant = request.getParameter("ticket_enfant");
		String ticket_adulte = request.getParameter("ticket_adulte");
		String ticket_vieux = request.getParameter("ticket_vieux");
		
		response.setContentType("application/json");
		
		HttpSession session = request.getSession(false);
		
		if(ticket_enfant == null && ticket_adulte == null && ticket_vieux == null) {
			ticket.setMsg("Requete erronée");
			ticket.setStatut("ERROR");
		}else {
			ticket.ajoutTicket(session,ticket_enfant,ticket_adulte,ticket_vieux);
		}
		
		utilInfo.put("statut", ticket.getStatut());
		utilInfo.put("msg", ticket.getMsg());
		
		utilInfo.write(response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
