package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class UtilisateurJson
 */
@WebServlet("/UtilisateurJson")
public class UtilisateurJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String statut,msg;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UtilisateurJson() {
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
	
	public void redirectService(String service,HttpServletResponse response,HttpSession session,String change_Val){
		System.out.println("Service :"+service);
		
		switch(service) {
		case "1":/*Username,password,email,date d'inscription*/
			try {
				this.defaultAnswer(response,session);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "2":
			try {
				this.changeInformation( change_Val, 3, response, session);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "3":
			try {
				this.changeInformation( change_Val, 1, response, session);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "4":
			try {
				/*FUNCTION POUR VERIFIER SI L4USERNAME EST DEJA PRIS*/
				if(this.isAlreadyInbase(change_Val)) {
					JSONObject utilInfo = new JSONObject();
					
					utilInfo.put("statut",this.statut);
					utilInfo.put("msg",this.msg);
					
					utilInfo.write(response.getWriter());
				}else {
					this.changeInformation( change_Val, 2, response, session);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "5"://ISCO
			try {
				this.isConnected(session, response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "6" ://SUPPRESSION
			try {
				this.deleteAccount(response,session);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "7" ://
			try {
				this.getInformation(response, session);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
	
	public void defaultAnswer(HttpServletResponse response,HttpSession session) throws JSONException, IOException {
		JSONObject utilInfo = new JSONObject();
		Utilisateur util = new Utilisateur();
		Cryptage decrypt = new Cryptage();
		
		Object test = session.getAttribute("utilisateur");
		util = (Utilisateur) test;
		
		utilInfo.put("statut",this.statut);
		utilInfo.put("msg",this.msg);
		utilInfo.put("date_User",util.getDate());
		utilInfo.put("username_User",decrypt.decrypt(util.getUsername(), 1));
		
		utilInfo.write(response.getWriter());
	}
	
	public void changeInformation(String newvalue,int state,HttpServletResponse response,HttpSession session) throws JSONException, IOException {
		Connection conn = null;
		JSONObject utilInfo = new JSONObject();
		Utilisateur util = new Utilisateur();
		Cryptage crypt = new Cryptage();
		
		Object test = session.getAttribute("utilisateur");
		util = (Utilisateur) test;
		
		String sql ="";
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
		switch(state) {
		case 1 :
			sql="UPDATE member SET Member_pass = '"+crypt.encrypt(newvalue, 2)+"' WHERE Member_id = "+util.getId();
			util.setPassword(crypt.encrypt(newvalue, 2));
			break;
		case 2 :
			sql="UPDATE member SET Member_username = '"+crypt.encrypt(newvalue, 1)+"' WHERE Member_id = "+util.getId();
			util.setUsername(crypt.encrypt(newvalue, 1));
			break;
		case 3 :
			sql="UPDATE member SET Member_mail = '"+crypt.encrypt(newvalue, 0)+"' WHERE Member_id = "+util.getId();
			util.setEmail(crypt.encrypt(newvalue, 0));
			break;
		}
		
		try {
		    conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
		    
		    Statement stmt= conn.createStatement();  
		    stmt.executeUpdate(sql);
		    
		    this.setStatut("SUCCESS");
		    this.setMsg("Changement éffectué");
		    
		    conn.close();
		} catch (SQLException ex) {
			this.setStatut("ERROR");
			this.setMsg("SQLException: " + ex.getMessage()+"\n SQLState: "+ex.getSQLState()+"\n VendorError: "+ex.getErrorCode());
		}
		
		utilInfo.put("statut",this.statut);
		utilInfo.put("msg",this.msg);
		
		utilInfo.write(response.getWriter());
		
	}
	
	public boolean isAlreadyInbase(String username) {
		Connection conn = null;
		Cryptage crypt = new Cryptage();
		int i=1;
		
		String sql ="SELECT * FROM member WHERE Member_username = '"+crypt.encrypt(username, 1)+"'";
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
		try {
		    conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
		    
		    PreparedStatement stmt= conn.prepareStatement(sql);  
		    ResultSet rs=stmt.executeQuery();
		    
		    if (rs != null) 
		    {
		      rs.last();    // moves cursor to the last row
		      i = rs.getRow(); // get row id 
		    }
		    
		    conn.close();
		    
		} catch (SQLException ex) {
			this.setStatut("ERROR");
			this.setMsg("SQLException: " + ex.getMessage()+"\n SQLState: "+ex.getSQLState()+"\n VendorError: "+ex.getErrorCode());
		}
		
		if(i>0) {
	    	this.statut ="ERROR";
	    	this.msg ="Cet username est déjà pris choisissez en un autre et recommencer.";
	    	return true;
	    }else {
	    	return false;
	    }
	}
	
	public void isConnected(HttpSession session, HttpServletResponse response) throws JSONException, IOException {
		JSONObject utilInfo = new JSONObject();
		
		this.setMsg("Requête reçue et traitée");
		this.setStatut("SUCCESS");
		
		if(session == null) {
			utilInfo.put("co",false);
		}else {
			utilInfo.put("co",true);
		}
		utilInfo.put("statut",this.statut);
		utilInfo.put("msg",this.msg);
		
		utilInfo.write(response.getWriter());
	}
	
	public void deleteAccount(HttpServletResponse response,HttpSession session) throws JSONException, IOException {
		Connection conn = null;
		JSONObject utilInfo = new JSONObject();
		Utilisateur util = new Utilisateur();
		Cryptage crypt = new Cryptage();
		
		Object test = session.getAttribute("utilisateur");
		util = (Utilisateur) test;
		
		String sql ="DELETE FROM member WHERE Member_id = "+Integer.valueOf(util.getId());
		try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
        	System.out.println("SQLException: " + ex.getMessage());
        }
		
		try {
		    conn = DriverManager.getConnection("jdbc:mysql://localhost/testing?user=root&password=&serverTimezone=UTC");
		    
		    Statement stmt= conn.createStatement();  
		    stmt.executeUpdate(sql);
		    
		    this.setStatut("SUCCESS");
		    this.setMsg("Compte supprimé");
		    
		    conn.close();
		} catch (SQLException ex) {
			this.setStatut("ERROR");
			this.setMsg("SQLException: " + ex.getMessage()+"\n SQLState: "+ex.getSQLState()+"\n VendorError: "+ex.getErrorCode());
		}
		
		
		
		utilInfo.put("statut",this.statut);
		utilInfo.put("msg",this.msg);
		
		utilInfo.write(response.getWriter());
	}
	
	public void getInformation(HttpServletResponse response,HttpSession session) throws JSONException, IOException {
		JSONObject utilInfo = new JSONObject();
		Utilisateur util = new Utilisateur();
		Cryptage decrypt = new Cryptage();
		
		Object test = session.getAttribute("utilisateur");
		util = (Utilisateur) test;
		
		utilInfo.put("username_User",decrypt.decrypt(util.getUsername(),1));
		utilInfo.put("email_User",decrypt.decrypt(util.getEmail(),0));
		utilInfo.put("password_User",decrypt.decrypt(util.getPassword(),2));
		utilInfo.put("id_User",util.getId());
		utilInfo.put("date_User",util.getDate());
		utilInfo.put("msg",this.msg);
		utilInfo.put("statut",this.statut);
		utilInfo.write(response.getWriter());
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UtilisateurJson utilJSON = new UtilisateurJson();
		
		String service = request.getParameter("service");
		String change_Val = request.getParameter("changement");
		
		response.setContentType("application/json");
		
		HttpSession session = request.getSession(false);
		
		if(service == null) {
			utilJSON.setMsg("Requete erronée");
			utilJSON.setStatut("ERROR");
		}else {
			utilJSON.setMsg("Chargement éffectué");
			utilJSON.setStatut("SUCCESS");
			utilJSON.redirectService(service,response,session,change_Val);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
