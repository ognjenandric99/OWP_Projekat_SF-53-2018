package bioskop.model;

public class Sediste {
	private int idSale;
	private int redniBroj;
	public Sediste(int idSale, int redniBroj) {
		super();
		this.idSale = idSale;
		this.redniBroj = redniBroj;
	}
	public int getIdSale() {
		return idSale;
	}
	public void setIdSale(int idSale) {
		this.idSale = idSale;
	}
	public int getRedniBroj() {
		return redniBroj;
	}
	public void setRedniBroj(int redniBroj) {
		this.redniBroj = redniBroj;
	}
	
}
