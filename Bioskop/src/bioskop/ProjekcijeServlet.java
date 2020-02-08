package bioskop;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import bioskop.dao.FilmoviDAO;
import bioskop.dao.ProjekcijeDAO;
import bioskop.dao.SalaDAO;
import bioskop.model.Film;
import bioskop.model.Projekcija;
import bioskop.model.Sala;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
			default:
				System.out.println("Primnjen je AJAX zahtev sa parametrom action="+action);
				break;
			}
		}
	}

}
