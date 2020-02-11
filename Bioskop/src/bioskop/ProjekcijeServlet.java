package bioskop;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import bioskop.dao.FilmoviDAO;
import bioskop.dao.ProjekcijeDAO;
import bioskop.dao.SalaDAO;
import bioskop.dao.TipProjekcijeDAO;
import bioskop.model.Film;
import bioskop.model.Projekcija;
import bioskop.model.Sala;
import bioskop.model.TipProjekacije;

/**
 * Servlet implementation class ProjekcijeServlet
 */
public class ProjekcijeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjekcijeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @throws SQLException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private JSONObject proveraTerminaZaDodavanje(HttpServletRequest request){
    	JSONObject odg = new JSONObject();
    	boolean status = false;
    	String message = "Unexpected error";
    	String pocetakTermina = request.getParameter("pocetakTermina");
    	String cenaKarte = request.getParameter("cenaKarte");
    	String idSale = request.getParameter("ID_Sale");
    	String idFilma = request.getParameter("ID_Filma");
    	String idTipa = request.getParameter("ID_Tipa");
    	String Administrator = (String) request.getSession().getAttribute("username");
    	String ulogaAdmina = (String) request.getSession().getAttribute("uloga");
    	
    	try {
    		Film film = FilmoviDAO.get(Integer.valueOf(idFilma));
    		Sala sala  = SalaDAO.get(Integer.valueOf(idSale));
    		TipProjekacije tip = TipProjekcijeDAO.get(Integer.valueOf(idTipa));
    		boolean salaImaTajTip = false;
    		for (TipProjekacije t : sala.tipoviProjekcija()) {
				if(t.getId()==tip.getId()) {
					salaImaTajTip = true;
				}
			}
    		
    		
    		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    		Date pt = format.parse(pocetakTermina);	
    		Date ddatum = format.parse(pocetakTermina);
			Calendar c = Calendar.getInstance();
			c.setTime(ddatum);
			c.add(Calendar.MINUTE, film.getTrajanje());
			ddatum = c.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");       
			String krajTermina = df.format(ddatum);

    		boolean statusZauzeto = SalaDAO.slobodnaSalaUTerminu(idSale, pocetakTermina, krajTermina);
    		
    		
    		if(statusZauzeto && film!=null && sala!=null && salaImaTajTip) {
    			Projekcija projekcija = new Projekcija(1, Integer.valueOf(idFilma), tip.getNaziv(), Integer.valueOf(idSale), pt, Double.valueOf(cenaKarte), Administrator, "Active", SalaDAO.brojMaksimumSedistaSale(idSale), 0);
    			status = ProjekcijeDAO.dodajProjekciju(projekcija, krajTermina);
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	odg.put("status", status);
    	odg.put("message",message);
    	return odg;
    }
    private JSONObject ucitajProjekciju(HttpServletRequest request) {
    	JSONObject oodg = new JSONObject();
    	JSONObject odg = new JSONObject();
    	boolean status = false;
    	String message = "Unexpected error.";
    	try {
    		int id = Integer.valueOf(request.getParameter("idProjekcije"));
    		Projekcija p =  ProjekcijeDAO.get(id);
    		Sala s = SalaDAO.get(Integer.valueOf(p.getIdSale()));
    		Film f = FilmoviDAO.get(Integer.valueOf(p.getIdFilma()));
    		odg.put("id", p.getId());
    		odg.put("idFilma", p.getIdFilma());
    		odg.put("nazivFilma",f.getNaziv());
    		odg.put("tipProjekcije",p.getTipProjekcije());
    		odg.put("idSale",s.getId());
    		odg.put("nazivSale",s.getNaziv());
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");   
    		String date = df.format(p.getDatum());
    		odg.put("termin",date);
    		odg.put("cenaKarte",p.getCenaKarte());
    		odg.put("status",p.getStatus());
    		int brojKarata = p.getMaksimumKarata()-p.getProdaneKarte();
    		odg.put("brojKarata",brojKarata);
    		status = true;
    		message = "Ucitala se projekcija";
    		oodg.put("projekcija", odg);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	oodg.put("status", status);
    	oodg.put("message",message);
    	return odg;
    }
    private JSONObject ucitajProjekcijeZaDanas(HttpServletRequest request) {
    	JSONObject obj = new JSONObject();
    	boolean status = false;
    	ArrayList<JSONObject> listaProjekcija = new ArrayList<JSONObject>();
    	
    	Film film = null;
    	Sala sala = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String sredjenDatum = dateFormat.format(date);
    	ArrayList<Projekcija> lista = ProjekcijeDAO.ucitajZaDatum(request,sredjenDatum);
    	for (Projekcija projekcija : lista) {
			try {
				film = FilmoviDAO.get(projekcija.getIdFilma());
				sala = SalaDAO.get(projekcija.getIdSale());
				JSONObject t_p = new JSONObject();
				if(film!=null && sala!=null) {
					t_p.put("id_projekcije", projekcija.getId());
					t_p.put("id_filma", film.getId());
					t_p.put("naziv_filma", film.getNaziv());
					String datumProj = dateFormat1.format(projekcija.getDatum());
					t_p.put("terminProjekcije", datumProj);
					t_p.put("tip_projekcije", projekcija.getTipProjekcije());
					t_p.put("id_sale", sala.getId());
					t_p.put("naziv_sale", sala.getNaziv());
					t_p.put("cena", projekcija.getCenaKarte());
				}
				listaProjekcija.add(t_p);
				status = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	obj.put("status", status);
    	obj.put("listaProjekcija", listaProjekcija);
    	return obj;
    }
    private JSONObject obrisiProjekciju(HttpServletRequest request) {
    	JSONObject odg = new JSONObject();
    	boolean status = false;
    	String message = "Unexpected error!";
    	
    	try {
    		status = ProjekcijeDAO.obrisiProjekciju(request.getParameter("idProjekcije"));
    		if(status) {
    			message = "Uspesno ste obrisali projekciju.";
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    		status = false;
    		message = "Trenutno nije moguce izvrsiti tu operaciju";
    	}
    	
    	odg.put("status", status);
    	odg.put("message",message);
    	return odg;
    }
    private JSONObject filter(HttpServletRequest request) {
    	JSONObject odg = new JSONObject();
    	boolean status = false;
    	String message = "Unexpected error";
    	ArrayList<JSONObject> lista = new ArrayList<JSONObject>();
    	String id_Filma = request.getParameter("idFilma");
    	String pocetak = request.getParameter("pocetak");
    	String pocetakKraj = request.getParameter("pocetakKraj");
    	String idSale = request.getParameter("idSale");
    	String oznakaTipa = request.getParameter("oznakaTipa");
    	String cenaMin = request.getParameter("cenaMin");
    	String cenaMax = request.getParameter("cenaMax");
    	try {
        	if(id_Filma==null || id_Filma=="") {
        		id_Filma  = "%%";
        	}
        	if(idSale==null || idSale=="") {
        		idSale  = "%%";
        	}
        	if(oznakaTipa==null || oznakaTipa=="") {
        		oznakaTipa  = "%%";
        	}
        	if(!(Integer.valueOf(cenaMin)>0)) {
        		cenaMin = String.valueOf(0);
        	}
        	if(!(Integer.valueOf(cenaMax)>0)) {
        		cenaMax = String.valueOf(0);
        	}
        	if(Integer.valueOf(cenaMin)>Integer.valueOf(cenaMax)) {
        		cenaMax = cenaMin;
        	}
        	if(pocetak.length()!=16) {
        		pocetak = "2000-12-31 10:10";
        	}
        	if(pocetakKraj.length()!=16) {
        		pocetakKraj = "2100-12-31 14:02";
        	}
        	System.out.println("ID FILMA : "+id_Filma);
    		JSONObject srv = ProjekcijeDAO.filterProjekcije(id_Filma, pocetak, pocetakKraj, idSale, oznakaTipa, cenaMin, cenaMax);
    		if((Boolean) srv.get("status")) {
    			ArrayList<Projekcija> l = (ArrayList<Projekcija>) srv.get("listaProjekcija");
    			for (Projekcija projekcija : l) {
					JSONObject pr = new JSONObject();
					pr.put("ID",projekcija.getId());
					pr.put("ID_Filma",projekcija.getIdFilma());
					pr.put("Naziv_Filma",FilmoviDAO.get(projekcija.getIdFilma()).getNaziv());
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String datum = df.format(projekcija.getDatum());
					pr.put("Termin",datum);
					pr.put("Sala",SalaDAO.get(projekcija.getIdSale()).getNaziv());
					pr.put("TipProjekcije",projekcija.getTipProjekcije());
					pr.put("Cena",projekcija.getCenaKarte());
					lista.add(pr);
				}
    			odg.put("lista",lista);
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    		message = "Molimo Vas da proverite vase unose.";
    		status = false;
    	}
    	if(lista.size()>0) {
    		status = true;
    		message = "Ucitano";
    	}
    	else {
    		message = "Ni jedna projekcija ne ispunjava zadate kriterijume.";
    	}
    	odg.put("status",status);
    	odg.put("message", message);
    	return odg;
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		
		if(action!=null && request!=null) {
			switch (action) {
			case "ucitajProjekcijeZaDanas":
				out.print(ucitajProjekcijeZaDanas(request));
				break;
			case "ucitajProjekciju":
				out.print(ucitajProjekciju(request));
				break;
			case "dodajProjekciju":
				out.print(proveraTerminaZaDodavanje(request));
				break;
			case "obrisiProjekciju":
				out.print(obrisiProjekciju(request));
				break;
			case "filter":
				out.print(filter(request));
				break;
			default:
				System.out.println("Primnjen je AJAX zahtev sa parametrom action="+action);
				break;
			}
		}
	}

}
