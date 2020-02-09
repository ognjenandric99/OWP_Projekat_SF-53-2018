package bioskop.dao;

import java.io.IOException;
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
	
	public static User get(String id) {
		JSONObject odg = new JSONObject();
		JSONObject korisnik = null;
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		try {
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users"
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
				return user;
			}
			else {

			}
			
	}
	catch(Exception e) {
		
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return null;
	}
	
	public static Boolean isLoggedIn(HttpServletRequest request) {
		if(request.getSession().getAttribute("username")!=null) {
			return true;
		}
		return false;
	}
	
	public static boolean deleteUser(String idKorisnika) {
		boolean status = false;
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			
			String query = "UPDATE Users SET Status='Deleted' WHERE ID=? ;";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, idKorisnika);
			
			int broj = pstmt.executeUpdate();
			if(broj==1) {
				status = true;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return status;
	}
	
	public static JSONObject getSessionInfo(HttpServletRequest request) {
		
		JSONObject obj = new JSONObject();
		Boolean status = false;
		String username = (String) request.getSession().getAttribute("username");
		String password = (String) request.getSession().getAttribute("password");
		String uloga = (String) request.getSession().getAttribute("uloga");
		String statusAccounta = (String) request.getSession().getAttribute("status");
		String id = (String) request.getSession().getAttribute("ajdi");
		if(username!=null && !username.equals("") && proveriLoginInformacije(username, password)) {
			status  = true;
		}
		obj.put("status",status);
		obj.put("username",username);
		obj.put("uloga",uloga);
		obj.put("statusAccount",statusAccounta);
		obj.put("ajdi",id);
		return obj;
	}
	public static boolean proveriLoginInformacije(String username,String password) {
		boolean status = false;
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			
				String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users WHERE Username=? AND Password=? AND Status='Active'";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				
				rset = pstmt.executeQuery();
				
				if(rset.next()) {
					status = true;

				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return status;
	}
	public static JSONObject login(HttpServletRequest request) {
		JSONObject odg = new JSONObject();
		String message = "Unexpected error";
		boolean status = false;
		User user = null;
		JSONObject korisnik = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			
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
					request.getSession().setAttribute("password", user.getPassword());
					request.getSession().setAttribute("uloga", user.getUloga().toString());
					request.getSession().setAttribute("status", user.getStatus());
					request.getSession().setAttribute("ajdi", String.valueOf(user.getID()));
					message = "Uspesno ste se loginovali.";
				}
				else {
					message = "Molimo Vas da proverite tacnost podataka.";
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		odg.put("status", status);
		odg.put("korisnik", korisnik);
		odg.put("message",message);
		return odg;
	}
	public static JSONObject logOut(HttpServletRequest request) {
		Boolean status = false;
		String message = "Unexpected error.";
		try {
			request.getSession().removeAttribute("username");
			request.getSession().removeAttribute("uloga");
			request.getSession().removeAttribute("status");
			request.getSession().removeAttribute("ajdi");
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
		 System.out.println(username+"|"+password);
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
						request.getSession().setAttribute("password", password);
						request.getSession().setAttribute("uloga", "obicanKorisnik");
						request.getSession().setAttribute("status", "Active");
						request.getSession().setAttribute("ajdi", String.valueOf(ucitajBrojKorisnika()));
						
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
		Boolean status  = true;
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT ID,Username,Password,DatumRegistracije,Uloga,Status FROM Users"
					+ " WHERE Username = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,username);

			rset = pstmt.executeQuery();
			
			if (!rset.next()) {
				status = false;
			}


			
	}
	catch(Exception e) {
		
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return status;
		
	}
	
	public static int ucitajBrojKorisnika() {
		int broj = 0;
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT ID FROM Users WHERE 1";

			pstmt = conn.prepareStatement(query);

			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				broj++;
			}

			
	}
	catch(Exception e) {
		
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return broj;
	}
	
	public static JSONObject ucitajKorisnika(String id) {
		JSONObject odg = new JSONObject();
		JSONObject korisnik = null;
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		try {
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users"
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
				obj.put("Status",user.getStatus());
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
	
	public static boolean promeniSifru(HttpServletRequest request,String idKorisnika, String novaSifra) {
		boolean status = false;
		System.out.println(novaSifra+" "+idKorisnika);
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			if(!((String) request.getSession().getAttribute("username")).equals(KorisnikDAO.get(idKorisnika).getUsername())) {
				System.out.println((String) request.getSession().getAttribute("username"));
				System.out.println(KorisnikDAO.get(idKorisnika).getUsername());
				System.out.println("Useri se ne poklapaju");
				throw new IOException(); 
			}
			
			String query = "UPDATE Users SET Password=? WHERE ID=? ;";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,novaSifra);
			pstmt.setString(2, idKorisnika);
			
			int broj = pstmt.executeUpdate();
			if(broj==1) {
				status = true;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return status;
	}
	public static boolean promeniUlogu(HttpServletRequest request,String idKorisnika, String novaUloga) {
		boolean status = false;
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			if(!((String)request.getSession().getAttribute("uloga")).equals("Admin")) {
				System.out.println("Ulogovani korisnik nije admin");
				throw new IOException(); 
			}
			if(!novaUloga.equals("Admin") && !novaUloga.equals("obicanKorisnik")) {
				System.out.println("Unesite pravilan naziv uloge.");
				throw new IOException();
			}
			
			String query = "UPDATE Users SET Uloga=? WHERE ID=? ;";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,novaUloga);
			pstmt.setString(2, idKorisnika);
			
			int broj = pstmt.executeUpdate();
			if(broj==1) {
				status = true;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		
		return status;
	}
	
	public static JSONObject ucitajSveKorisnike(String username,String password,String date,String tip) {
		JSONObject odg = new JSONObject();
		ArrayList<JSONObject> korisnici = new ArrayList<JSONObject>();
		
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		try {

			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users"
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
				obj.put("Status",user.getStatus());
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
