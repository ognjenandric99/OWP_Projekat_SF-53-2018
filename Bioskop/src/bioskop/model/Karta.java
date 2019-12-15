package bioskop.model;

import java.util.Date;

public class Karta {
	private int id;
	private String idProjekcije;
	private String oznakaSedista;
	private Date datumProdaje;
	private String usernameKorisnika;
	public Karta(int id, String idProjekcije, String oznakaSedista,
			Date datumProdaje, String usernameKorisnika) {
		super();
		this.id = id;
		this.idProjekcije = idProjekcije;
		this.oznakaSedista = oznakaSedista;
		this.datumProdaje = datumProdaje;
		this.usernameKorisnika = usernameKorisnika;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdProjekcije() {
		return idProjekcije;
	}
	public void setIdProjekcije(String idProjekcije) {
		this.idProjekcije = idProjekcije;
	}
	public String getOznakaSedista() {
		return oznakaSedista;
	}
	public void setOznakaSedista(String oznakaSedista) {
		this.oznakaSedista = oznakaSedista;
	}
	public Date getDatumProdaje() {
		return datumProdaje;
	}
	public void setDatumProdaje(Date datumProdaje) {
		this.datumProdaje = datumProdaje;
	}
	public String getUsernameKorisnika() {
		return usernameKorisnika;
	}
	public void setUsernameKorisnika(String usernameKorisnika) {
		this.usernameKorisnika = usernameKorisnika;
	}
	
	
}
