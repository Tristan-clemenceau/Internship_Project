package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String msg,statut;
    private Utilisateur utilisateur;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        this.msg = "default";
        this.statut = "default";
    }
    
    private void isAlreadyInBase(String username,String password,Connection conn,HttpServletRequest request) {
    	int i = 1;
		try {
			PreparedStatement stmt= conn.prepareStatement("SELECT * FROM member WHERE Member_username = '"+username+"' AND Member_pass = '"+password+"'");  
		    ResultSet rs=stmt.executeQuery();
		    
		    /*VERIFICATION OF ROW NUMBER*/
		    if (rs != null) 
		    {
		      rs.last();    // moves cursor to the last row
		      i = rs.getRow(); // get row id 
		    }
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(i>0) {
	    	this.statut ="SUCCESS";
	    	this.msg ="Password and username correct";
	    	this.setUtil(username,password,conn,request);
	    }else {
	    	this.statut ="ERROR";
	    	this.msg ="Invalid username or password";
	    }
	    
	    
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
	
	public void setUtil(String username,String password,Connection conn,HttpServletRequest request) {
		this.utilisateur = new Utilisateur();
    	PreparedStatement stmt;
    	ResultSet rs;
		try {
			stmt = conn.prepareStatement("SELECT Member_id,Member_date_register,Member_mail FROM member WHERE Member_username = '"+username+"' AND Member_pass = '"+password+"'");
			rs=stmt.executeQuery();
			
			while(rs.next()) {
				this.utilisateur.setId(String.valueOf(rs.getInt(1)));
				this.utilisateur.setDate(rs.getString(2));
				this.utilisateur.setEmail(rs.getString(3));
			}
			this.utilisateur.setLog(true);
			this.utilisateur.setUsername(username);
			this.utilisateur.setPassword(password);
			
			HttpSession session = request.getSession();
	    	session.setAttribute("utilisateur", this.utilisateur);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Cryptage test = new Cryptage();
		Connection conn = null;
		Login login = new Login();
		
		response.setContentType("application/json");

		JSONObject jsonObj = new JSONObject();
		
	    try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
	      
	    String usernameInput=request.getParameter("connexionInputUsername");  
		String passwordInput=request.getParameter("connexionInputPassword");
		
		usernameInput = test.encrypt(usernameInput, 1);
		passwordInput = test.encrypt(passwordInput, 2);
		
		try {
		    conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
		    
		    login.isAlreadyInBase(usernameInput, passwordInput, conn,request);
		    
		    conn.close();
		} catch (SQLException ex) {
			login.setStatut("ERROR");
			login.setMsg("SQLException: " + ex.getMessage()+"\n SQLState: "+ex.getSQLState()+"\n VendorError: "+ex.getErrorCode());
		}
		
		jsonObj.put("statut",login.getStatut());
		jsonObj.put("msg",login.getMsg());

		jsonObj.write(response.getWriter());
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
