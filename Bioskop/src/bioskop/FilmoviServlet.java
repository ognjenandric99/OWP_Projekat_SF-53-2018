package bioskop;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import bioskop.dao.FilmoviDAO;
import bioskop.model.Film;

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
    private JSONObject ucitajFilm(HttpServletRequest request,HttpServletResponse response){
    	String idFilma = request.getParameter("filmID");
    	Boolean status = false;
    	Film film = null;
		try {
			film = FilmoviDAO.getFilm(idFilma);
			status = true;
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		JSONObject odg = new JSONObject();

	    odg.put("status", status);
	    odg.put("film", film);
	    
	    return odg;
		
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		System.out.println(action);
		Boolean status = false;
		switch (action) {
		case "ucitajFilm":
			System.out.println(ucitajFilm(request, response));
			break;

		default:
			break;
		}
	}

}
