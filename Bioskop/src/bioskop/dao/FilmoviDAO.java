package bioskop.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import bioskop.model.Film;
import bioskop.model.Zanr;

public class FilmoviDAO {
	public static boolean filmImaSlobodnihProjekcija(String idFilma) throws SQLException {
		boolean imanema = false;
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM Projekcije WHERE ID_Filma = ? AND MaksimumKarata-BrojProdanihKarata>0";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, idFilma);

			rset = pstmt.executeQuery();
			if (rset.next()) {
				imanema = true;
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return imanema;
	}
	public static Film get(int id1) throws Exception {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID, Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis,Status FROM Filmovi WHERE id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, String.valueOf(id1));

			rset = pstmt.executeQuery();
			if (rset.next()) {
				int index = 1;
				int ID = Integer.valueOf(rset.getString(index++));
				String Naziv = rset.getString(index++);
				String Reziser = rset.getString(index++);
				String Glumci = rset.getString(index++);
				String Zanrovi = rset.getString(index++);
				int Trajanje = Integer.valueOf(rset.getString(index++));
				String Distributer = rset.getString(index++);
				String Zemlja_Porekla = rset.getString(index++);
				int Godina_Proizvodnje = Integer.valueOf(rset.getString(index++));
				String Opis = rset.getString(index++);
				String status = rset.getString(index++);
				
				//Sredjivanje za pravljenje objekta
				ArrayList<Zanr> Zanrovi_n = new ArrayList<Zanr>();
				String[] Zanrs = Zanrovi.split(";");
				for (String znr : Zanrs) {
					try{
						Zanrovi_n.add(Zanr.valueOf(znr));
					}
					catch(Exception e){
						System.out.println("Puklo kod unosa zanra - "+e);
					}
				}
				Film film = new Film(ID, Naziv, Reziser, Glumci, Zanrovi_n, Trajanje, Distributer, Zemlja_Porekla, Godina_Proizvodnje, Opis);
				
				return film;
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return null;
	}
	
	public static JSONObject getFilm(String id1) throws Exception {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			System.out.println("Uslo je ovdexxx");
			String query = "SELECT ID, Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis,Status FROM Filmovi WHERE id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id1);

			rset = pstmt.executeQuery();
			if (rset.next()) {
				int index = 1;
				int ID = Integer.valueOf(rset.getString(index++));
				String Naziv = rset.getString(index++);
				String Reziser = rset.getString(index++);
				String Glumci = rset.getString(index++);
				String Zanrovi = rset.getString(index++);
				int Trajanje = Integer.valueOf(rset.getString(index++));
				String Distributer = rset.getString(index++);
				String Zemlja_Porekla = rset.getString(index++);
				int Godina_Proizvodnje = Integer.valueOf(rset.getString(index++));
				String Opis = rset.getString(index++);
				String status = rset.getString(index++);
				
				//Sredjivanje za pravljenje objekta
				ArrayList<Zanr> Zanrovi_n = new ArrayList<Zanr>();
				String[] Zanrs = Zanrovi.split(";");
				for (String znr : Zanrs) {
					try{
						Zanrovi_n.add(Zanr.valueOf(znr));
					}
					catch(Exception e){
						System.out.println("Puklo kod unosa zanra - "+e);
					}
				}
				Film film = new Film(ID, Naziv, Reziser, Glumci, Zanrovi_n, Trajanje, Distributer, Zemlja_Porekla, Godina_Proizvodnje, Opis);
				JSONObject obj = new JSONObject();
				obj.put("ID",film.getId());
				obj.put("Naziv",film.getNaziv());
				obj.put("Reziser",film.getReziser());
				String[] glumci1 = film.getGlumci().split(";");
				ArrayList<String> glumci = new ArrayList<String>();
				for (String string : glumci1) {
					glumci.add(string);
				}
				obj.put("Glumci",glumci);
				ArrayList<String> zanrovi = new ArrayList<String>();
				for (Zanr z : film.getZanr()) {
					zanrovi.add(z.toString());
				}
				obj.put("Zanrovi",zanrovi);
				obj.put("Trajanje",film.getTrajanje());
				obj.put("Distributer",film.getDistributer());
				obj.put("Zemlja_Porekla",film.getZemljaPorekla());
				obj.put("Godina_Proizvodnje",film.getGodinaProizvodnje());
				obj.put("Opis",film.getOpis());
				obj.put("status",status);
				if(status.equalsIgnoreCase("active")) {
					return obj;
				}
				return null;
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return null;
	}

	
	public static ArrayList<JSONObject> getFilms(String naziv1,int trajanje1,String zanrovi1,String opis1,String glumci1,String reziser1,String godina1,String distributer1,String zemlja1) throws Exception {
		
		ArrayList<JSONObject> filmovi = new ArrayList<JSONObject>();
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID, Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis,Status FROM Filmovi"
					+ " WHERE Naziv LIKE ? AND Reziser LIKE ? AND Glumci LIKE ? AND Zanrovi LIKE ? AND Trajanje>? AND Distributer LIKE ? AND Zemlja_Porekla LIKE ? AND Godina_Proizvodnje LIKE ? AND Opis LIKE ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,"%"+naziv1+"%");
			pstmt.setString(2, "%"+reziser1+"%");
			pstmt.setString(3, "%"+glumci1+"%");
			pstmt.setString(4, "%"+zanrovi1+"%");
			pstmt.setInt(5, trajanje1);
			pstmt.setString(6, "%"+distributer1+"%");
			pstmt.setString(7, "%"+zemlja1+"%");
			pstmt.setString(8, "%"+godina1+"%");
			pstmt.setString(9, "%"+opis1+"%");

			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				int ID = Integer.valueOf(rset.getString(index++));
				String Naziv = rset.getString(index++);
				String Reziser = rset.getString(index++);
				String Glumci = rset.getString(index++);
				String Zanrovi = rset.getString(index++);
				int Trajanje = Integer.valueOf(rset.getString(index++));
				String Distributer = rset.getString(index++);
				String Zemlja_Porekla = rset.getString(index++);
				int Godina_Proizvodnje = Integer.valueOf(rset.getString(index++));
				String Opis = rset.getString(index++);
				String status = rset.getString(index++);
				
				//Sredjivanje za pravljenje objekta
				ArrayList<Zanr> Zanrovi_n = new ArrayList<Zanr>();
				String[] Zanrs = Zanrovi.split(";");
				if(Zanrs.length>0) {
					for (String znr : Zanrs) {
						try{
							Zanrovi_n.add(Zanr.valueOf(znr));
						}
						catch(Exception e){
							System.out.println("Puklo kod unosa zanra - "+e);
						}
					}
				}
				else {
					Zanrovi_n.add(Zanr.valueOf("NeunesenZanr"));
				}

				Film film = new Film(ID, Naziv, Reziser, Glumci, Zanrovi_n, Trajanje, Distributer, Zemlja_Porekla, Godina_Proizvodnje, Opis);
				JSONObject obj = new JSONObject();
				obj.put("ID",film.getId());
				obj.put("Naziv",film.getNaziv());
				obj.put("Reziser",film.getReziser());
				String[] glumci2 = film.getGlumci().split(";");
				ArrayList<String> glumci = new ArrayList<String>();
				for (String string : glumci2) {
					glumci.add(string);
				}
				obj.put("Glumci",glumci);
				ArrayList<String> zanrovi = new ArrayList<String>();
				for (Zanr z : film.getZanr()) {
					zanrovi.add(z.toString());
				}
				obj.put("Zanrovi",zanrovi);
				obj.put("Trajanje",film.getTrajanje());
				obj.put("Distributer",film.getDistributer());
				obj.put("Zemlja_Porekla",film.getZemljaPorekla());
				obj.put("Godina_Proizvodnje",film.getGodinaProizvodnje());
				obj.put("Opis",film.getOpis());
				
				if(status.equalsIgnoreCase("active")) {
					filmovi.add(obj);
				}
			}
			return filmovi;

		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
			// ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
	}

	
	public static boolean deleteMovie(String id) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Filmovi SET Status='Deleted' WHERE ID = ?";


			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);


			int broj = pstmt.executeUpdate();
			if (broj>0) {
				return true;
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} 
		catch(Exception e) {
			
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
			
		}
		return false;
		
		
	}
	
	public static ArrayList<String> get_Zanrove(){
		ArrayList<String> zanrovi = new ArrayList<String>();
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT Zanr FROM Zanrovi WHERE 1";


			pstmt = conn.prepareStatement(query);


			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index = 1;
				String zanr = rset.getString(index++);
				zanrovi.add(zanr);
				
			}

		}
		catch(Exception e) {
			
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return zanrovi;
	}
	
	public static JSONObject izmenaFilma(HttpServletRequest request) {
		String naziv = request.getParameter("naziv");
    	int trajanje = 0;
    	try {
    		trajanje = Integer.valueOf(request.getParameter("trajanje"));
    	}
    	catch(Exception e) {
    		
    	}
    	String id = request.getParameter("id");
    	String zanrovi = request.getParameter("zanr");
    	if(zanrovi.length()<1) {
    		zanrovi = "NeunesenZanr";
    	}
    	String opis = request.getParameter("opis");
    	String glumci = request.getParameter("glumci");
    	String reziser = request.getParameter("reziser");
    	String godina = request.getParameter("godina");
    	String distributer = request.getParameter("distributer");
    	String zemlja = request.getParameter("zemlja");
    	Boolean status = false;
    	
    	JSONObject odg = new JSONObject();
	    
	    
	    Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Filmovi SET Naziv=?,Reziser=?,Glumci=?,Zanrovi=?,Trajanje=?,Distributer=?,Zemlja_Porekla=?,Godina_Proizvodnje=?,Opis=? WHERE ID = ?";

			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, naziv);
			pstmt.setString(2, reziser);
			pstmt.setString(3, glumci);
			pstmt.setString(4, zanrovi);
			pstmt.setString(5, String.valueOf(trajanje));
			pstmt.setString(6, distributer);
			pstmt.setString(7, zemlja);
			pstmt.setString(8, godina);
			pstmt.setString(9, opis);
			pstmt.setString(10, id);

			

			int broj = pstmt.executeUpdate();

			if (broj>0) {
				System.out.println("Doslo je do ovde");
				status = true;
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} 
		catch(Exception e) {
			
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
			
		}
		odg.put("status",status);
		return odg;
	}
	
	public static boolean dodajFilm(HttpServletRequest request) {
		String naziv = request.getParameter("naziv");
		int trajanje = Integer.valueOf(request.getParameter("trajanje"));
		String zanrovi = request.getParameter("zanr");
		if(zanrovi.length()<1) {
    		zanrovi = "NeunesenZanr";
    	}
		String opis = request.getParameter("opis");
		String glumci = request.getParameter("glumci");
		String reziser = request.getParameter("reziser");
		int godina = Integer.valueOf(request.getParameter("godina"));
		String distributer = request.getParameter("distributer");
		String zemlja = request.getParameter("zemlja");
		
		System.out.println("OVO JE ZEMLJA : "+zemlja);
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO Filmovi(Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis) VALUES (?,?,?,?,?,?,?,?,?)";

			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, naziv);
			pstmt.setString(2, reziser);
			pstmt.setString(3, glumci);
			pstmt.setString(4, zanrovi);
			pstmt.setInt(5, trajanje);
			pstmt.setString(6, distributer);
			pstmt.setString(7, zemlja);
			pstmt.setInt(8, godina);
			pstmt.setString(9, opis);
			
			int broj = pstmt.executeUpdate();
			if (broj>0) {
				return true;
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
			
		}
		return false;
		
	}
	

}
