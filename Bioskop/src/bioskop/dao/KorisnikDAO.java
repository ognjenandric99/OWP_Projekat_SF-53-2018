package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import bioskop.model.Film;
import bioskop.model.User;
import bioskop.model.Zanr;
import bioskop.model.UlogeUsera;

public class KorisnikDAO {
	
	public static Boolean isLoggedIn(HttpServletRequest request) {
		if(request.getSession().getAttribute("username")!=null) {
			return true;
		}
		return false;
	}
	public static JSONObject getSessionInfo(HttpServletRequest request) {
		JSONObject obj = new JSONObject();
		Boolean status = false;
		String username = (String) request.getSession().getAttribute("username");
		String uloga = (String) request.getSession().getAttribute("uloga");
		String statusAccounta = (String) request.getSession().getAttribute("status");
		if(username!=null && !username.equals("")) {
			status  = true;
		}
		obj.put("status",status);
		obj.put("username",username);
		obj.put("uloga",uloga);
		obj.put("statusAccount",statusAccounta);
		return obj;
	}
	public static JSONObject login(HttpServletRequest request) {
		JSONObject odg = new JSONObject();
		boolean status = false;
		User user = null;
		JSONObject korisnik = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement pstmt = null;
			ResultSet rset = null;
				String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users WHERE Username=? AND Password=? AND Status='Active'";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				
				rset = pstmt.executeQuery();
				
				if(rset.next()) {
					int index = 1;
					String ID = rset.getString(index++);
					String Username = rset.getString(index++);
					String Password = rset.getString(index++);
					String Datum = rset.getString(index++);
					String Uloga = rset.getString(index++);
					String Status = rset.getString(index++);
					
					//Sredjivanje za pravljenje objekta
					
					
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date ddatum = format.parse(Datum);
					
					
					user = new User(ID, Username, Password, bioskop.model.UlogeUsera.valueOf(Uloga), ddatum, Status);
					korisnik = new JSONObject();
					korisnik.put("username", user.getUsername());
					korisnik.put("uloga",user.getUloga().toString());
					korisnik.put("status",user.getStatus());
					status = true;
					request.getSession().setAttribute("username", user.getUsername());
					request.getSession().setAttribute("uloga", user.getUloga().toString());
					request.getSession().setAttribute("status", user.getStatus());
				}
				else {
					
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		odg.put("status", status);
		odg.put("korisnik", korisnik);
		return odg;
	}
	public static JSONObject logOut(HttpServletRequest request) {
		Boolean status = false;
		String message = "Unexpected error.";
		try {
			request.getSession().removeAttribute("username");
			request.getSession().removeAttribute("uloga");
			request.getSession().removeAttribute("status");
			status = true;
			message = "Uspesno ste se log out-ovali.";
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("message",message);
		return obj;
	}
	public static JSONObject registracija(HttpServletRequest request) {
		JSONObject odg = new JSONObject();
		 
		boolean status = false;
		String message = "uncaught error";
		 String username = request.getParameter("username");
		 String password = request.getParameter("password");
		 if(username.length()==0 || password.length()==0) {
			 odg.put("status",false);
			 message = "Unos je prekratak!";
			 odg.put("message",message);
			 return odg;
		 }else {};
		 if(postojiUser(username)) {
			 odg.put("status",false);
			 message = "Username vec postoji!";
			 odg.put("message",message);
			 return odg;
		 }
		 else {
			 Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = null;
				try {
					String query = "INSERT INTO Users(Username,Password,DatumRegistracije,Uloga,Status) VALUES (?,?,?,'obicanKorisnik','Active')";

					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, username);
					pstmt.setString(2, password);
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					pstmt.setString(3,dateFormat.format(date));

					int broj = pstmt.executeUpdate();
					if (broj>0) {
						
						status = true;
						message = "Registrovan";
						request.getSession().setAttribute("username",username);
						request.getSession().setAttribute("uloga", "obicanKorisnik");
						request.getSession().setAttribute("status", "Active");
						
					}
					else {
						message = "Nije uspelo unosenje u bazu podataka.";
					}

				} 
				catch(Exception e) {
					
				}
				finally {
					try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
					try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
					
				}
				odg.put("status",status);
				odg.put("message",message);
				return odg;
				
		 }
	}
	public static boolean postojiUser(String username) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		boolean status = false;
		try {
			String query = "SELECT ID,Username,Password,DatumRegistracije,Uloga FROM Users WHERE Username='?'";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);

			rset = pstmt.executeQuery();
			if (rset.next()) {
				status = true;
				try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			}
			else {
			}

		}
		catch(Exception e) {
			
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		return status;
		
	}
	
	public static JSONObject ucitajKorisnika(String id) {
		JSONObject odg = new JSONObject();
		JSONObject korisnik = null;
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		try {
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga FROM Users"
					+ " WHERE ID = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,id);

			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				status = true;
				int index = 1;
				String ID = rset.getString(index++);
				String Username = rset.getString(index++);
				String Password = rset.getString(index++);
				String Datum = rset.getString(index++);
				String Uloga = rset.getString(index++);
				String Status = rset.getString(index++);
				
				//Sredjivanje za pravljenje objekta
				
				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date ddatum = format.parse(Datum);
				
				User user = new User(ID, Username, Password, bioskop.model.UlogeUsera.valueOf(Uloga), ddatum, Status);
				JSONObject obj = new JSONObject();
				obj.put("ID",user.getID());
				obj.put("Username",user.getUsername());
				obj.put("Password",user.getPassword());
				obj.put("Datum",format.format(user.getDatumRegistracije()));
				obj.put("Uloga",user.getUloga().toString());
				korisnik = obj;
			}
			else {

			}
			odg.put("status",status);
			odg.put("korisnik",korisnik);
			
	}
	catch(Exception e) {
		
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return odg;
	}
	
	public static JSONObject ucitajSveKorisnike(String username,String password,String date,String tip) {
		JSONObject odg = new JSONObject();
		ArrayList<JSONObject> korisnici = new ArrayList<JSONObject>();
		
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		try {

			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga FROM Users"
					+ " WHERE Username LIKE ? AND Password LIKE ? AND DatumRegistracije LIKE ? AND Uloga LIKE ?";


			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,"%"+username+"%");
			pstmt.setString(2, "%"+password+"%");
			pstmt.setString(3, "%"+date+"%");
			pstmt.setString(4, "%"+tip+"%");

			rset = pstmt.executeQuery();

			
			while (rset.next()) {
				status = true;

				int index = 1;
				String ID = rset.getString(index++);
				String Username = rset.getString(index++);
				String Password = rset.getString(index++);
				String Datum = rset.getString(index++);
				String Uloga = rset.getString(index++);
				
				
				//Sredjivanje za pravljenje objekta
				

				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date ddatum = format.parse(Datum);
				
				User user = new User(ID, Username, Password, bioskop.model.UlogeUsera.valueOf(Uloga), ddatum, "Active");
				JSONObject obj = new JSONObject();
				obj.put("ID",user.getID());
				obj.put("Username",user.getUsername());
				obj.put("Password",user.getPassword());
				obj.put("Datum",format.format(user.getDatumRegistracije()));
				obj.put("Uloga",user.getUloga().toString());
				korisnici.add(obj);
			}
			odg.put("status",status);
			odg.put("lista",korisnici);
			
	}
	catch(Exception e) {
		
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return odg;
	}
}
