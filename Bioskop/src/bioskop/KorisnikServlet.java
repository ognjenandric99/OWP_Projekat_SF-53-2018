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

import org.apache.tomcat.jni.Address;
import org.json.simple.JSONObject;

import bioskop.dao.FilmoviDAO;
import bioskop.dao.KarteDAO;
import bioskop.dao.KorisnikDAO;
import bioskop.dao.ProjekcijeDAO;
import bioskop.dao.SalaDAO;
import bioskop.dao.SedisteDAO;
import bioskop.dao.TipProjekcijeDAO;
import bioskop.model.Film;
import bioskop.model.Karta;
import bioskop.model.Projekcija;
import bioskop.model.Sala;
import bioskop.model.Sediste;
import bioskop.model.User;
import bioskop.dao.ConnectionManager;
import bioskop.dao.SedisteDAO;

/**
 * Servlet implementation class FilmoviServlet
 */
public class KorisnikServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KorisnikServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
   private JSONObject registruj(HttpServletRequest request) {
	   JSONObject odg = new JSONObject();
	   odg = KorisnikDAO.registracija(request);
	   return odg;
   }
   private JSONObject ucitajSveKorisnike() {
	   JSONObject odg = new JSONObject();
	   odg = KorisnikDAO.ucitajSveKorisnike("","","","");
	   return odg;
   }
   private JSONObject ucitajKorisnika(HttpServletRequest request) {
	   JSONObject odg = new JSONObject();
	   String id = request.getParameter("id");
	   odg = KorisnikDAO.ucitajKorisnika(id);
	   return odg;
   }
   
   private JSONObject ucitajFilter(HttpServletRequest request) {
	   JSONObject odg = new JSONObject();
	   String username = request.getParameter("username");
	   String password = request.getParameter("password");
	   String datum = request.getParameter("datum");
	   String tip = request.getParameter("tip");
	   odg = KorisnikDAO.ucitajSveKorisnike(username,password,datum,tip);
	   return odg;
   }
   private JSONObject login(HttpServletRequest request) {
	   JSONObject odg = KorisnikDAO.login(request);
	   return odg;
   }
   private Boolean isLoggedIn(HttpServletRequest request) {
	   return KorisnikDAO.isLoggedIn(request);
   }
   private JSONObject getSessionInfo(HttpServletRequest request) {
	   return KorisnikDAO.getSessionInfo(request);
   }
   private JSONObject logOut(HttpServletRequest request) {
	   return KorisnikDAO.logOut(request);
   }
   private JSONObject changeUser(HttpServletRequest request) {
	   JSONObject odg = new JSONObject();
	   boolean sifra = false;
	   boolean uloga = false;
	   if(((String) request.getSession().getAttribute("username")).equals(KorisnikDAO.get(request.getParameter("idKorisnika")).getUsername())) {
		   String idKorisnika = request.getParameter("idKorisnika");
		   String novaSifra = request.getParameter("novasifra");
		   sifra = KorisnikDAO.promeniSifru(request, idKorisnika, novaSifra);
	   }
	   if(((String) request.getSession().getAttribute("uloga")).equals("Admin")) {
		   String idKorisnika = request.getParameter("idKorisnika");
		   String novaUloga= request.getParameter("novaUloga");
		   if(!((String) request.getSession().getAttribute("username")).equals(KorisnikDAO.get(idKorisnika).getUsername())) {
			   uloga = KorisnikDAO.promeniUlogu(request, idKorisnika, novaUloga);
		   }
	   }
	   odg.put("promenjenaSifra", sifra);
	   odg.put("promenjenaUloga", uloga);
	   return odg;
   }
   
   private JSONObject deleteUser(HttpServletRequest request) {
	   JSONObject obj = new JSONObject();
	   boolean status = false;
	   String message = "Nije moguce obrisati korisnika.";
	   
	   String uloga = (String) request.getSession().getAttribute("uloga");
	   if(uloga.equals("Admin")) {
		   String ajdi = request.getParameter("idKorisnika");
		   System.out.println("Moje : "+(String)request.getSession().getAttribute("ajdi")+" Uneto : "+ajdi);
		   if(!((String)request.getSession().getAttribute("ajdi")).equals(ajdi)) {
			   status = KorisnikDAO.deleteUser(ajdi);
		   }
		   if(status) {
			   message = "Uspesno ste obrisali korisnika";
		   }
	   }
	   else {
		   message = "Morate biti ulogovani kao Administrator.";
	   }
	   obj.put("status", status);
	   obj.put("message",message);
	   return obj;
	   
   }
   private JSONObject ucitajProjFilterInfo(HttpServletRequest request) {
	   JSONObject odg = new JSONObject();
	   boolean status = false;
	   String message = "Unexpected error";
	   
	   try {
		   ArrayList<JSONObject> filmovi = FilmoviDAO.getFilms("", 0, "", "", "", "", "", "", "");
		   ArrayList<JSONObject> sale = SalaDAO.ucitajSale();
		   odg.put("filmovi", filmovi);
		   odg.put("sale", sale);
		   status = true;
		   message = "Ucitano";
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	   
	   odg.put("status", status);
	   odg.put("message", message);
	   return odg;
   }
   private JSONObject kupiKartuInfo(HttpServletRequest request) {
	   JSONObject odg = new JSONObject();
	   boolean status = false;
	   String message = "Desila se greska.";
	   try {
		   String id = request.getParameter("idProjekcije");
		   Projekcija p = ProjekcijeDAO.get(Integer.valueOf(id));
		   if(p!=null) {
			   int maxSedista = 0;
			   if(p.getDatum().compareTo(new Date())>0) {
				   ArrayList<Sediste> sSedista = SedisteDAO.slobodnaSedista(String.valueOf(p.getId()));
				   ArrayList<String> slobSedista = new ArrayList<String>();
				   for (Sediste sediste : sSedista) {
					slobSedista.add(String.valueOf(sediste.getRedniBroj()));
					
					
				}
				   maxSedista = SalaDAO.brojMaksimumSedistaSale(String.valueOf(p.getIdSale()));
				   Film film = FilmoviDAO.get(p.getIdFilma());
				   if(film!=null) {
					   JSONObject info = new JSONObject();
					   info.put("idProjekcije", p.getId());
					   info.put("slobodnaSedista", slobSedista);
					   DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					   info.put("termin",df.format(p.getDatum()));
					   info.put("cenaKarte",p.getCenaKarte());
					   info.put("nazivFilma",film.getNaziv());
					   info.put("tipProjekcije", p.getTipProjekcije());
					   info.put("idSale",p.getIdSale());
					   info.put("nazivSale", SalaDAO.get(p.getIdSale()).getNaziv());
					   info.put("trajanje", film.getTrajanje());
					   info.put("maxSedista",maxSedista);
					   odg.put("info", info);
					   message = "Ucitane informacije";
					   status = true;
				   }
				   else {
					   message = "Film ne postoji";
				   }
			   }
			   else {
				   message = "Ta projekcija je u toku/zavrsena.";
			   }
		   }
		   else {
			   message = "Ne postoji ta projekcija.";
		   }
		   
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	   odg.put("status", status);
	   odg.put("message",message);
	   return odg;
   }
   
   private JSONObject kupiKartu(HttpServletRequest request) {
	   JSONObject odg = new JSONObject();
	   boolean status = false;
	   String message = "Unexpected error";
	   
	   String username = (String) request.getSession().getAttribute("username");
	   String idProjekcije = request.getParameter("id");
	   String sedista = request.getParameter("odabrana_sedista");
	   if(username!=null) {
		   status = KarteDAO.kupiKartu(idProjekcije, sedista,username);
		   if(status) {
			   message = "Uspesno ste zavrsili kupovinu";
		   }
	   }
	   else {
		   message = "Morate biti ulogovani da bi ste kupili kartu";
	   }
	   odg.put("status", status);
	   odg.put("message",message);
	   return odg;
	   
   }
   private JSONObject ucitajKarteKorisnika(HttpServletRequest request,String username) {
	   JSONObject odg = new JSONObject();
	   boolean status = false;
	   String message = "Unexpected error";
	   
	   
	   try {
		   if(!username.equals((String) request.getSession().getAttribute("username")) && !((String)request.getSession().getAttribute("uloga")).equals("Admin")) {
			   message = "Ne mozete dobiti karte za ovog korisnika";
			   throw new Exception();
		   }
		   
		   ArrayList<Karta> karte = KarteDAO.ucitajKarteZaKorisnika(username);
		   ArrayList<JSONObject> k = new ArrayList<JSONObject>();
		   for (Karta karta : karte) {
			   Projekcija p = ProjekcijeDAO.get(Integer.valueOf(karta.getIdProjekcije()));
			   Film f = FilmoviDAO.get(p.getIdFilma());
			   Sala s = SalaDAO.get(p.getIdSale());
			   status = true;
			   JSONObject obj = new JSONObject();
			   obj.put("ID",karta.getId());
			   obj.put("ID_filma", f.getId());
			   obj.put("nazivFilma", f.getNaziv());
			   DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			   String datum = df.format(p.getDatum());
			   obj.put("Termin",datum);
			   obj.put("ID_projekcije", p.getId());
			   obj.put("tipProjekcije", p.getTipProjekcije());
			   obj.put("sala",s.getNaziv());
			   obj.put("sediste",karta.getOznakaSedista());
			   obj.put("cena", p.getCenaKarte());
			   k.add(obj);
			   status = true;
		   }
		   odg.put("karte", k);
	   }catch(Exception e) {
		   e.printStackTrace();
		   status = false;
		   
	   }
	   
	   odg.put("status", status);
	   odg.put("message",message);
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
		String korisnikID = request.getParameter("korisnikID");
		
		if(action!=null && request!=null) {
			switch (action) {
			case "registracija":
				out.print(registruj(request));
				break;
			case "ucitajSve":
				out.print(ucitajSveKorisnike());
				break;
			case "ucitajKorisnika":
				out.print(ucitajKorisnika(request));
				break;
			case "filter":
				out.print(ucitajFilter(request));
				break;
			case "login":
				out.print(login(request));
				break;
			case "isLoggedIn":
				out.print(isLoggedIn(request).toString());
				break;
			case "getSessionInfo":
				out.print(getSessionInfo(request));
				break;
			case "logOut":
				out.print(logOut(request));
				break;
			case "changeUser":
				out.print(changeUser(request));
				break;
			case "deleteUser":
				out.print(deleteUser(request));
				break;
			case "ucitajProjFilterInfo":
				out.print(ucitajProjFilterInfo(request));
				break;
			case "kupiKartuInfo":
				out.print(kupiKartuInfo(request));
				break;
			case "kupiKartu":
				out.print(kupiKartu(request));
				break;
			case "ucitajKarteKorisnika":
				User user = KorisnikDAO.get(request.getParameter("id"));
				if(user!=null) {
					String username = user.getUsername();
					out.print(ucitajKarteKorisnika(request,username));
				}
				
				break;
			default:
				System.out.println("Primnjen je AJAX zahtev sa parametrom action="+action);
				break;
			}
		}
	}

}
