package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String statut,msg;
	private Utilisateur utilisateur;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
    	this.statut = "default";
    	this.msg = "default";
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

	private boolean isAlreadyInBase(String username,Connection conn) {
    	int i = 1;
		try {
			PreparedStatement stmt= conn.prepareStatement("SELECT * FROM member WHERE Member_username = '"+username+"'");  
		    ResultSet rs=stmt.executeQuery();
		    
		    /*VERIFICATION OF ROW NUMBER*/
		    if (rs != null) 
		    {
		      rs.last();    // moves cursor to the last row
		      i = rs.getRow(); // get row id 
		    }
		    
		    if(i > 0) {
		    	this.statut ="ERROR";
		    	this.msg ="This username is already taken";
		    }else {
		    	this.statut ="SUCCESS";
		    	this.msg ="Username available";
		    } 
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(i>0) {
	    	this.statut ="ERROR";
	    	this.msg ="This username is already taken";
	    	return true;
	    }else {
	    	this.statut ="SUCCESS";
	    	this.msg ="Username available";
	    	return false;
	    }
	    
	    
    }
    
    private void registerPeople(String username,String email,String password,Connection conn,HttpServletRequest request) {
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		int i = 0;
		
    	PreparedStatement ps;
		try {
			ps = conn.prepareStatement("INSERT INTO member (Member_id, Member_date_register, Member_pass, Member_username, Member_mail) VALUES (NULL,?,?,?,?)");
		
			ps.setString(1,dateFormat.format(date));
		    ps.setString(2,password);  
		    ps.setString(3,username);  
		    ps.setString(4,email);  
		    		          
		    i=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	    if(i>0) {
	    	this.statut ="SUCCESS";
	    	this.msg ="You are successfully registered";
	    	/*CREATION UTIL*/
	    	this.utilisateur = new Utilisateur();
	    	this.utilisateur.setDate(dateFormat.format(date));
	    	this.utilisateur.setEmail(email);
	    	this.utilisateur.setLog(true);
	    	this.utilisateur.setPassword(password);
	    	this.utilisateur.setUsername(username);
	    	this.utilisateur.setId(this.getID(username,password,conn));
	    	/*AJOUT UTIL DANS HTTPSESSION*/
	    	HttpSession session = request.getSession();
	    	session.setAttribute("utilisateur", this.utilisateur);
	    }else {
	    	this.statut ="ERROR";
	    	this.msg ="You are not successfully registered";
	    }
    }
    
    public String getID(String username,String password,Connection conn) {
    	String id ="";
    	PreparedStatement stmt;
    	ResultSet rs;
		try {
			stmt = conn.prepareStatement("SELECT Member_id FROM member WHERE Member_username = '"+username+"' AND Member_pass = '"+password+"'");
			rs=stmt.executeQuery();
			
			while(rs.next()) {
				id = String.valueOf(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    
    	return id;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Register register = new Register();
		Cryptage test = new Cryptage();
		Connection conn = null;
		
		response.setContentType("application/json");

		JSONObject jsonObj = new JSONObject();
		
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
		String username=request.getParameter("registerInputUsername");
		String email=request.getParameter("registerInputEmail");    
		String password=request.getParameter("registerInputPassword");
		
		username = test.encrypt(username, 1);
		email = test.encrypt(email, 0);
		password = test.encrypt(password, 2);
		
		try {
		    conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
		    
		    if(!register.isAlreadyInBase(username,conn)) {
		    	register.registerPeople(username,email,password,conn,request);
		    }
		    conn.close();
		} catch (SQLException ex) {
		   register.setStatut("ERROR");
		   register.setMsg("SQLException: " + ex.getMessage()+"\n SQLState: "+ex.getSQLState()+"\n VendorError: "+ex.getErrorCode());
		}

		jsonObj.put("statut",register.getStatut());
		jsonObj.put("msg",register.getMsg());

		jsonObj.write(response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
