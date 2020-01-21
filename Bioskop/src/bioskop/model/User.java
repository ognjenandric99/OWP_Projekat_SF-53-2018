package bioskop.model;

import java.util.Date;

public class User {
	private String ID;
	private String username;
	private String password;
	private UlogeUsera uloga;
	private Date datumRegistracije;
	private String status;
	
	public String getID() {
		return ID;
	}
	public void setID(String ID) {
		this.ID = ID;
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
	public UlogeUsera getUloga() {
		return uloga;
	}
	public void setUloga(UlogeUsera uloga) {
		this.uloga = uloga;
	}
	public Date getDatumRegistracije() {
		return datumRegistracije;
	}
	public void setDatumRegistracije(Date datumRegistracije) {
		this.datumRegistracije = datumRegistracije;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public User(String ID,String username, String password, UlogeUsera uloga, Date datumRegistracije, String status) {
		super();
		this.ID = ID;
		this.username = username;
		this.password = password;
		this.uloga = uloga;
		this.datumRegistracije = datumRegistracije;
		this.status = status;
	}
	
}
