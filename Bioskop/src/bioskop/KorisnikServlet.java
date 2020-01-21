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
import bioskop.dao.KorisnikDAO;
import bioskop.model.Film;
import bioskop.dao.ConnectionManager;

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
		
		default:
			break;
		}
	}

}
