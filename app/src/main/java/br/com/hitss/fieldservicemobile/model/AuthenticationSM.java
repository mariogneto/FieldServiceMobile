package br.com.hitss.fieldservicemobile.model;

/*import java.time.LocalDateTime;
import java.time.ZoneId;*/
import java.time.LocalDateTime;

public class AuthenticationSM {

	private String login;
	private String password;
	private String context;
	private String message;
	private Long expiration;
	private String jwt;
	//private Long expirationLocalDate;
	
	public AuthenticationSM() {
	}
	public AuthenticationSM(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getExpiration() {
		return expiration;
	}
	public void setExpiration(Long expiration) {
		//this.expirationLocalDate = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		this.expiration = expiration;
	}
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	/*public Long getExpirationLocalDate() {
		return expirationLocalDate;
	}

	public void setExpirationLocalDate(Long expirationLocalDate) {
		this.expirationLocalDate = expirationLocalDate;
	}*/
}
