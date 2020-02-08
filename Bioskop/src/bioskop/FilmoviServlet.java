package bioskop;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.Address;
import org.json.simple.JSONObject;

import bioskop.dao.FilmoviDAO;
import bioskop.model.Film;
import bioskop.dao.ConnectionManager;

/**
 * Servlet implementation class FilmoviServlet
 */
public class FilmoviServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilmoviServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private JSONObject ucitajFilm(String idFilma){
    	JSONObject odg = new JSONObject();
    	Boolean status = false;
    	JSONObject film = null;
		try {
			System.out.println(idFilma);
			film = FilmoviDAO.getFilm(idFilma);
			if(film!=null) {
				boolean imamesta = FilmoviDAO.filmImaSlobodnihProjekcija(idFilma);
				odg.put("imaKarata",imamesta);
				status = true;
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	    odg.put("status", status);
	    odg.put("film", film);
	    
	    return odg;
		
    }
    
    private JSONObject ucitajSveFilmove() {
    	Boolean status = false;
    	ArrayList<JSONObject> filmovi = new ArrayList<JSONObject>();
    	try {
    		filmovi = FilmoviDAO.getFilms("", 0, "", "", "", "", "", "", "");
    		if(filmovi.size()>0) {
    			status = true;
    		}
    		
    	}
    	catch (Exception e) {
    		System.out.println("Puklo je ovde na ucitaj sve filmove.");
    	}
    	JSONObject odg = new JSONObject();

	    odg.put("status", status);
	    odg.put("filmovi", filmovi);
	    
	    return odg;
    }
    
    private JSONObject obrisiFilm(String id) {
    	boolean status = FilmoviDAO.deleteMovie(id);
    	JSONObject odg = new JSONObject();

	    odg.put("status", status);
	    
	    return odg;
    }
    
    private JSONObject dodajFilm(HttpServletRequest request) {
    	boolean status = FilmoviDAO.dodajFilm(request);
    	JSONObject odg = new JSONObject();
    	odg.put("status", status);
	  
	    return odg;
    	
    }
    
    private JSONObject get_Zanrs() {
    	JSONObject odg = new JSONObject();
    	ArrayList<String> lista = FilmoviDAO.get_Zanrove();
    	boolean status = false;
    	if(lista.size()>0) {
    		status = true;
    	}
    	odg.put("status", status);
	    odg.put("zanrovi", lista);
    	return odg;
    }
    private JSONObject filterFilm(HttpServletRequest request) {
    	String naziv = request.getParameter("naziv");
    	int trajanje = 0;
    	try {
    		trajanje = Integer.valueOf(request.getParameter("trajanje"));
    	}
    	catch(Exception e) {
    		
    	}
    	String zanrovi = request.getParameter("zanr");
    	String opis = request.getParameter("opis");
    	String glumci = request.getParameter("glumci");
    	String reziser = request.getParameter("reziser");
    	String godina = request.getParameter("godina");
    	String distributer = request.getParameter("distributer");
    	String zemlja = request.getParameter("zemlja");
    	Boolean status = false;
    	ArrayList<JSONObject> filmovi = new ArrayList<JSONObject>();
    	try {
    		//String naziv1,int trajanje1,String zanrovi1,String opis1,String glumci1,String reziser1,String godina1,String distributer1,String zemlja1
    		filmovi = FilmoviDAO.getFilms(naziv,trajanje,zanrovi,opis,glumci,reziser,godina,distributer,zemlja);
    		if(filmovi.size()>0) {
    			status = true;
    		}
    		
    	}
    	catch (Exception e) {
    		System.out.println("Puklo je ovde na ucitaj sve filmove.");
    	}
    	JSONObject odg = new JSONObject();

	    odg.put("status", status);
	    odg.put("filmovi", filmovi);
	    
	    return odg;
    }
    
    private JSONObject izmenaFilma(HttpServletRequest request) {
    	
    	JSONObject odg = new JSONObject();
	    odg = FilmoviDAO.izmenaFilma(request);
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
		String filmID = request.getParameter("filmID");
		
		switch (action) {
		case "ucitajFilm":
			out.print(ucitajFilm(filmID));
			break;

		case "ucitajSve":
			out.print(ucitajSveFilmove());
			break;
			
		case "obrisiFilm":
			out.print(obrisiFilm(filmID));
			break;
		
		case "dodajFilm":
			out.print(dodajFilm(request));
			break;
			
		case "uzmiZanrove":
			out.print(get_Zanrs());
			break;
		case "filterFilm":
			out.print(filterFilm(request));
			break;
		case "izmeniFilm":
			out.print(izmenaFilma(request));
			break;
		default:
			break;
		}
	}

}
