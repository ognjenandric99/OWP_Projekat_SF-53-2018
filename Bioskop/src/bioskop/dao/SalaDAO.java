package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import bioskop.model.Film;
import bioskop.model.Sala;
import bioskop.model.TipProjekacije;
import bioskop.model.Zanr;

public class SalaDAO {
	
	public static boolean slobodnaSalaUTerminu(String idSale,String pocetakTermina,String krajTermina) throws SQLException {
		boolean status = true;
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Sala sala = null;
		try {
			String query = "SELECT * FROM Projekcije WHERE ID_Sale=? AND (KrajTermina>? AND Termin<?)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, String.valueOf(idSale));
			pstmt.setString(2, pocetakTermina);
			pstmt.setString(3, krajTermina);

			rset = pstmt.executeQuery();
			if(rset.next()) {
				status = false;
			}

		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		return status;
	}
	
	public static int brojMaksimumSedistaSale(String idSale) throws SQLException{
		int broj = 0;
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Sala sala = null;
		try {
			String query = "SELECT * FROM Sedista WHERE ID_Sale = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, String.valueOf(idSale));

			rset = pstmt.executeQuery();
			while(rset.next()) {
				broj++;
			}

		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}

		return broj;
	}
	
	public static ArrayList<JSONObject> ucitajSale() throws SQLException{
		ArrayList<JSONObject> lista = new ArrayList<JSONObject>();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID,Naziv,ID_Tipova_Projekcija FROM Sale WHERE 1";

			pstmt = conn.prepareStatement(query);

			rset = pstmt.executeQuery();
			while (rset.next()) {
				int index = 1;
				int ID = Integer.valueOf(rset.getString(index++));
				String Naziv = rset.getString(index++);
				String[] ID_Tipova_Projekcija = rset.getString(index++).split(";");
				ArrayList<TipProjekacije> listaProjekcija = new ArrayList<TipProjekacije>();
				for (String string : ID_Tipova_Projekcija) {
					TipProjekacije tip = TipProjekcijeDAO.get(Integer.valueOf(string));
					if(tip!=null) {
						listaProjekcija.add(tip);
					}
				}
				
				Sala sala = new Sala(ID, Naziv, listaProjekcija);
				JSONObject odg = new JSONObject();
				odg.put("ID", sala.getId());
				odg.put("Naziv",sala.getNaziv());
				odg.put("MaksimumSedista",brojMaksimumSedistaSale(String.valueOf(sala.getId())));
				
				ArrayList<JSONObject> tipovi = new ArrayList<JSONObject>();
				for (TipProjekacije tipProjekacije : sala.tipoviProjekcija()){
					JSONObject t = new JSONObject();
					t.put("ID", tipProjekacije.getId());
					t.put("Naziv",tipProjekacije.getNaziv());
					tipovi.add(t);
				}
				odg.put("listaTipova",tipovi);
				lista.add(odg);
				
			}

		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return lista;
	}
	
	public static Sala get(int id) throws SQLException {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Sala sala = null;
		try {
			String query = "SELECT ID,Naziv,ID_Tipova_Projekcija FROM Sale WHERE ID = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, String.valueOf(id));

			rset = pstmt.executeQuery();
			if (rset.next()) {
				int index = 1;
				int ID = Integer.valueOf(rset.getString(index++));
				String Naziv = rset.getString(index++);
				String[] ID_Tipova_Projekcija = rset.getString(index++).split(";");
				ArrayList<TipProjekacije> listaProjekcija = new ArrayList<TipProjekacije>();
				for (String string : ID_Tipova_Projekcija) {
					TipProjekacije tip = TipProjekcijeDAO.get(Integer.valueOf(string));
					if(tip!=null) {
						listaProjekcija.add(tip);
					}
				}
				
				sala = new Sala(ID, Naziv, listaProjekcija);
				
				
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return sala;
	}
}
