package servlet;

import java.util.Base64;

public class Cryptage {
	final static int MAIL = 1, USERNAME = 5, PASSWORD = 10;
	
	public String encrypt(String message,int type) {
		
		String encodedString = Base64.getEncoder().encodeToString(message.getBytes());
		
		switch(type) {
		case 0:
			type = MAIL;
		    break;
		case 1:
			type = USERNAME;
		    break;
		case 2:
			type = PASSWORD;
		    break;
		default:
		    type = 0;
		}
		for(int cpt = 0; cpt < type ;cpt++) {
			encodedString = Base64.getEncoder().encodeToString(encodedString.getBytes());
		}
		return encodedString;
	}
	
	public String decrypt(String message,int type) {
		
		byte[] decodedBytes = Base64.getDecoder().decode(message);
		
		switch(type) {
		case 0:
			type = MAIL;
		    break;
		case 1:
			type = USERNAME;
		    break;
		case 2:
			type = PASSWORD;
		    break;
		default:
		    type = 0;
		}
		
		for(int cpt = 0; cpt < type ;cpt++) {
			decodedBytes = Base64.getDecoder().decode(decodedBytes);
		}
		return new String(decodedBytes);
	}
	
	/*Exemple of uses
	 *  String yo = "test";
		String result= null;
		result = test.encrypt(yo,0);
		System.out.println(result);
		result = test.decrypt(result,0);
		System.out.println(result);
	 * */
}
