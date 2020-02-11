package bioskop.dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import bioskop.dao.KorisnikDAO;
import bioskop.dao.SalaDAO;
import bioskop.dao.TipProjekcijeDAO;
import bioskop.dao.SedisteDAO;
import bioskop.model.Film;
import bioskop.model.Karta;
import bioskop.model.Projekcija;
import bioskop.model.Sediste;
import bioskop.model.User;
import bioskop.dao.ConnectionManager;

public class KarteDAO {
	
	
	public static Karta get(String id) {
		Karta karta = null;
		//
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID,ID_Projekcije,ID_Sedista,VremeProdaje,Korisnik FROM Karta WHERE ID=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,id);

			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				String ajdi = rset.getString(index++);
				int ID_Karte = Integer.valueOf(ajdi);
				String idProjekcije = rset.getString(index++);
				String idSedista = rset.getString(index++);
				String oznakaSedista = String.valueOf(SedisteDAO.get(idSedista).getRedniBroj());
				String vremeProdaje = rset.getString(index++);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date datum = df.parse(vremeProdaje);
				String korisnik = rset.getString(index++);
				karta = new Karta(ID_Karte, idProjekcije, oznakaSedista, datum, korisnik);
			}
			
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return karta;
	}

	public static ArrayList<Karta> karteZaProjekciju(String idProjekcije){
		ArrayList<Karta> lista= new ArrayList<Karta>();
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID,ID_Projekcije,ID_Sedista,VremeProdaje,Korisnik FROM Karta WHERE ID_Projekcije=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,idProjekcije);

			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				String ajdi = rset.getString(index++);
				int ID_Karte = Integer.valueOf(ajdi);
				String idProjekcije1 = rset.getString(index++);
				String idSedista = rset.getString(index++);
				String oznakaSedista = String.valueOf(SedisteDAO.get(idSedista).getRedniBroj());
				String vremeProdaje = rset.getString(index++);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date datum = df.parse(vremeProdaje);
				String korisnik = rset.getString(index++);
				Karta karta = new Karta(ID_Karte, idProjekcije1, oznakaSedista, datum, korisnik);
				lista.add(karta);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		return lista;
	}
	
	public static boolean kupiKartu(String idProjekcije,String sedista,String username) {
		boolean status = true;
		try {
			Projekcija p = ProjekcijeDAO.get(Integer.valueOf(idProjekcije));
			String[] s = sedista.split(";");
			ArrayList<String> sedista_f = new ArrayList<String>();
			for (int i=0;i<s.length;i++) {
				String sediste = s[i];
				int vrednost = Integer.valueOf(sediste);
				int vrednost1 = vrednost+1;
				int vrednost2 = vrednost-1;
				if((i+1)<s.length) {
					if(!(vrednost2==Integer.valueOf(s[i+1]) || vrednost1==Integer.valueOf(s[i+1])))	{
						System.out.println("Sedista nisu jedno do drugog");
						throw new Exception();
					}
				}
				sedista_f.add(sediste);
			}
			for (String sed : sedista_f) {
				String idSedista = SedisteDAO.vratiIDSedista(String.valueOf(p.getIdSale()), sed);
				if(!dodajKartuUBazu(idProjekcije, idSedista, username)) {
					status = false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			status = false;
		}
		
		
		return status;
	}
	
	public static boolean dodajKartuUBazu(String idProjekcije,String idSedista,String korisnik) {
		boolean status = false;
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			
			String query = "INSERT INTO Karta(ID_Projekcije,ID_Sedista,VremeProdaje,Korisnik) VALUES(?,?,?,?)";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, idProjekcije);
			pstmt.setString(2, idSedista);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String date = df.format(new Date());
			pstmt.setString(3, date);
			pstmt.setString(4, korisnik);
			
			int broj = 0;
			if(SedisteDAO.daLiJeSlobodnoSediste(idProjekcije, idSedista)) {
				broj = pstmt.executeUpdate();
				
			}
			if(broj==1) {
				status = true;
				ProjekcijeDAO.smanjiStanjeKarata(idProjekcije);
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
	
	public static ArrayList<Karta> ucitajKarteZaKorisnika(String username){
		ArrayList<Karta> lista = new ArrayList<Karta>();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID,ID_Projekcije,ID_Sedista,VremeProdaje,Korisnik FROM Karta WHERE Korisnik=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,username);

			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				String ajdi = rset.getString(index++);
				int ID_Karte = Integer.valueOf(ajdi);
				String idProjekcije = rset.getString(index++);
				String idSedista = rset.getString(index++);
				String oznakaSedista = String.valueOf(SedisteDAO.get(idSedista).getRedniBroj());
				String vremeProdaje = rset.getString(index++);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date datum = df.parse(vremeProdaje);
				String korisnik = rset.getString(index++);
				Karta karta = new Karta(ID_Karte, idProjekcije, oznakaSedista, datum, korisnik);
				lista.add(karta);
			}
			
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return lista;
	}
	
	public static boolean deleteKarta(String idKarte) {
		boolean status = false;
		//
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			
			String query = "DELETE FROM Karta WHERE ID=?";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, idKarte);
			
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
}
