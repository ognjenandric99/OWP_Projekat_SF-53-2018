package bioskop;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import bioskop.dao.AdminDAO;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private JSONObject adminPanelInfo(HttpServletRequest request) {
    	String uloga = (String) request.getSession().getAttribute("uloga");
    	JSONObject odg = new JSONObject();
    	boolean status = false;
    	String message = "Niste ulogovani kao admin, stoga se desila greska.";
    	if(uloga=="Admin") {
    		status = true;
    		ArrayList<JSONObject> filmovi = AdminDAO.adminPanel_moguciFilmovi();
    		ArrayList<JSONObject> sale = AdminDAO.adminPanel_moguceSale();
    		message = "Dobrodosli na Admin Panel, ulogovani ste kao Admin : "+request.getSession().getAttribute("username");
    		odg.put("listaFilmova",filmovi);
    		odg.put("listaSala",sale);
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
			
			case "adminPanelInfo":
				out.print(adminPanelInfo(request));
				break;
			default:
				System.out.println("Primnjen je AJAX zahtev sa parametrom action="+action);
				break;
			}
		}
	}

}
