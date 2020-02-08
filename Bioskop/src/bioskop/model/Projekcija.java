package bioskop.model;

import java.util.Date;

public class Projekcija {
	private int id;
	private int idFilma;
	private String tipProjekcije;
	private int idSale;
	private Date datum;
	private double cenaKarte;
	private String usernameAdministratora;
	private String status;
	private int MaksimumKarata;
	private int ProdaneKarte;
	public Projekcija(int id, int idFilma, String tipProjekcije, int idSale,
			Date datum, double cenaKarte, String usernameAdministratora,String status,int MaksimumKarata,int ProdaneKarte) {
		super();
		this.id = id;
		this.idFilma = idFilma;
		this.tipProjekcije = tipProjekcije;
		this.idSale = idSale;
		this.datum = datum;
		this.cenaKarte = cenaKarte;
		this.usernameAdministratora = usernameAdministratora;
		this.status = status;
		this.MaksimumKarata = MaksimumKarata;
		this.ProdaneKarte = ProdaneKarte;
	}
	public int getProdaneKarte() {
		return ProdaneKarte;
	}
	public void setProdaneKarte(int ProdaneKarte) {
		this.ProdaneKarte = ProdaneKarte;
	}
	public int getMaksimumKarata() {
		return MaksimumKarata;
	}
	public void setMaksimumKarata(int MaksimumKarata) {
		this.MaksimumKarata = MaksimumKarata;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdFilma() {
		return idFilma;
	}
	public void setIdFilma(int idFilma) {
		this.idFilma = idFilma;
	}
	public String getTipProjekcije() {
		return tipProjekcije;
	}
	public void setTipProjekcije(String tipProjekcije) {
		this.tipProjekcije = tipProjekcije;
	}
	public int getIdSale() {
		return idSale;
	}
	public void setIdSale(int idSale) {
		this.idSale = idSale;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public double getCenaKarte() {
		return cenaKarte;
	}
	public void setCenaKarte(double cenaKarte) {
		this.cenaKarte = cenaKarte;
	}
	public String getUsernameAdministratora() {
		return usernameAdministratora;
	}
	public void setUsernameAdministratora(String usernameAdministratora) {
		this.usernameAdministratora = usernameAdministratora;
	}
	
	
}
