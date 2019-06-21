package servlet;

public class Utilisateur {
	private String username,password,email,id,date;
	private boolean log = false;
	static final String DEFAULT = "UNDEFINED";
	
	public Utilisateur() {
		this.username = DEFAULT;
		this.password = DEFAULT;
		this.email = DEFAULT;
		this.id = DEFAULT;
		this.date = DEFAULT;
		this.log = false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}
	
	@Override
	public String toString() {
		return "Username :"+this.username+"\t Id:"+this.id+"\t pass:"+this.password+"\t email:"+this.email+"\t date:"+this.date+"\t log:"+this.log;
	}
	
}
