package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import bioskop.model.Karta;
import bioskop.model.Projekcija;
import bioskop.model.Sediste;

import bioskop.dao.KarteDAO;

public class SedisteDAO {
	public static ArrayList<Sediste> slobodnaSedista(String idProjekcije){
		ArrayList<Sediste> lista= new ArrayList<Sediste>();
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID FROM Projekcije WHERE ID=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,idProjekcije);

			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				String ajdi = rset.getString(index++);
				Projekcija proj = ProjekcijeDAO.get(Integer.valueOf(ajdi));
				int idSale = proj.getIdSale();
				ArrayList<Sediste> sedista = SedisteDAO.listaSedista(String.valueOf(idSale));
				ArrayList<Karta> karte = KarteDAO.karteZaProjekciju(idProjekcije);
				System.out.println("Ima karata za projekciju broj : "+karte.size());
				for (Sediste sed : sedista) {
					boolean slobodno = true;
					for(Karta karta : karte) {
						if((sed.getRedniBroj()+"").equals(karta.getOznakaSedista())) {
							slobodno = false;
						}
					}
					if(slobodno) {
						lista.add(sed);
					}
				}
				
			}
			
		}
		catch(Exception e) {
			
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
		return lista;
	}
	
	public static ArrayList<Sediste> listaSedista(String idSale){
		ArrayList<Sediste> lista = new ArrayList<Sediste>();
		//
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID,ID_Sale,Broj_Sedista FROM Sedista WHERE ID_Sale=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,idSale);

			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				String ajdi = rset.getString(index++);
				String idSale1 = rset.getString(index++);
				String brojSedista = rset.getString(index++);
				Sediste sediste = new Sediste(Integer.valueOf(idSale1), Integer.valueOf(brojSedista));
				lista.add(sediste);
			}
			
	}
	catch(Exception e) {
		
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return lista;
	}
	
	public static Sediste get(String id) {
		Sediste sediste = null;
		//
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID,ID_Sale,Broj_Sedista FROM Sedista WHERE ID=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,id);

			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				String ajdi = rset.getString(index++);
				String idSale = rset.getString(index++);
				String brojSedista = rset.getString(index++);
				sediste = new Sediste(Integer.valueOf(idSale), Integer.valueOf(brojSedista));
			}
			
	}
	catch(Exception e) {
		
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return sediste;
	}

	public static boolean daLiJeSlobodnoSediste(String idProjekcije,String idSedista) {
		boolean status = true;
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID FROM Karta WHERE ID_Projekcije=? AND ID_Sedista=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,idProjekcije);
			pstmt.setString(2, idSedista);

			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				status = false;
			}
			
	}
	catch(Exception e) {
		e.printStackTrace();
		status = false;
	}
	finally {
		try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
		try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
	}
		return status;
	}
	public static String vratiIDSedista(String idSale,String redniBroj) {
		String broj = "0";
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID FROM Sedista WHERE ID_Sale=? AND Broj_Sedista=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,idSale);
			pstmt.setString(2, redniBroj);

			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				String ajdi = rset.getString(index++);
				broj = ajdi;
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
		return broj;
	}
}
