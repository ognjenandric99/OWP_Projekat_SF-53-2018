package bioskop.model;

import java.util.ArrayList;

public class Sala {
	private int id;
	private String naziv;
	private ArrayList<TipProjekacije> tipoviProjekcija;
	
	public Sala(int id, String naziv, ArrayList<TipProjekacije> tipoviProjekcija) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.tipoviProjekcija = tipoviProjekcija;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public ArrayList<TipProjekacije> tipoviProjekcija() {
		return tipoviProjekcija;
	}

	public void setTipoviProjekacija(ArrayList<TipProjekacije> tipoviProjekacija) {
		this.tipoviProjekcija = tipoviProjekcija;
	}
	
	
	
	
}
