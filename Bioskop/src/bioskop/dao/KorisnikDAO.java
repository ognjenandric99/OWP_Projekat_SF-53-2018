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
	public static JSONObject registracija(HttpServletRequest request) {
		JSONObject odg = new JSONObject();
		 
		boolean status = false;
		 
		 String username = request.getParameter("username");
		 String password = request.getParameter("password");
		 
		 if(postojiUser(username)) {
			 odg.put("status",false);
			 return odg;
		 }
		 else {
			 Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = null;
				try {
					String query = "INSERT INTO Users(Username,Password,DatumRegistracije,Uloga) VALUES (?,?,?,'obicanKorisnik')";

					System.out.println(query);
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, username);
					pstmt.setString(2, password);
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();
					System.out.println(dateFormat.format(date));
					pstmt.setString(3,dateFormat.format(date));
					System.out.println(pstmt);

					int broj = pstmt.executeUpdate();
					if (broj>0) {
						
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
	}
	public static boolean postojiUser(String username) {
		Connection conn = ConnectionManager.getConnection();
		System.out.println(conn);
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		boolean status = false;
		try {
			System.out.println("Uslo je ovdexxx");
			String query = "SELECT ID,Username,Password,DatumRegistracije,Uloga WHERE Username=?";

			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, username);
			System.out.println(pstmt);

			rset = pstmt.executeQuery();
			System.out.println(rset);
			if (rset.next()) {
				status = true;
			}

		}
		catch(Exception e) {
			
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		return status;
		
	}
	
	public static JSONObject ucitajKorisnika(String id) {
		JSONObject odg = new JSONObject();
		
		boolean status = false;
		
		Connection conn = ConnectionManager.getConnection();
		System.out.println(conn);
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			System.out.println("Uslo je ovdexxx");
			String query = "SELECT ID,Username,Password,DatumRegistracije,Uloga WHERE ID= ?";

			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			System.out.println(pstmt);

			rset = pstmt.executeQuery();
			System.out.println(rset);
			if (rset.next()) {
				System.out.println("Uslo je ovde23");
				int index = 1;
				String ID = rset.getString(index++);
				String Username = rset.getString(index++);
				String Password = rset.getString(index++);
				String Datum = rset.getString(index++);
				String Uloga = rset.getString(index++);
				
			
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date ddatum = format.parse(Datum);
				
				
				JSONObject obj = new JSONObject();
				obj.put("ID",ID);
				obj.put("Username",Username);
				obj.put("Password",Password);
				obj.put("Datum",Datum);
				obj.put("Uloga",Uloga);
				
				odg.put("korisnik", obj);
				status = true;
				odg.put("status",status);
			}
			else {
				
				odg.put("korisnik", null);
				odg.put("status",status);
				System.out.println("Vraceno 0 redova");
			}

		}
		catch(Exception e) {
			
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return odg;
	}
	
	public static JSONObject ucitajSveKorisnike(String username,String password,String date,String tip) {
		JSONObject odg = new JSONObject();
		ArrayList<JSONObject> korisnici = new ArrayList<JSONObject>();
		
		Connection conn = ConnectionManager.getConnection();
		System.out.println(conn);
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		try {
			System.out.println("Uslo je ovdexxx");
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga FROM Users"
					+ " WHERE Username LIKE ? AND Password LIKE ? AND DatumRegistracije LIKE ? AND Uloga LIKE ?";

			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,"%"+username+"%");
			pstmt.setString(2, "%"+password+"%");
			pstmt.setString(3, "%"+date+"%");
			pstmt.setString(4, "%"+tip+"%");
			System.out.println(pstmt.toString());

			rset = pstmt.executeQuery();
			System.out.println(rset.toString());
			
			while (rset.next()) {
				status = true;
				System.out.println("Uslo je ovde - Load all korisnici");
				int index = 1;
				String ID = rset.getString(index++);
				String Username = rset.getString(index++);
				String Password = rset.getString(index++);
				String Datum = rset.getString(index++);
				String Uloga = rset.getString(index++);
				
				
				//Sredjivanje za pravljenje objekta
				
				System.out.println("Pre pravljenja");
				
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
