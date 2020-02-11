package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import bioskop.model.Film;
import bioskop.model.Karta;
import bioskop.model.Projekcija;
import bioskop.model.TipProjekacije;

public class AdminDAO {
	
	public static ArrayList<Projekcija> uzmiProjekcijeZaFilm(String idFilma,String termin){
		ArrayList<Projekcija> lista = new ArrayList<Projekcija>();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT ID FROM Projekcije WHERE Status='Active' AND ID_Filma=? AND (Termin BETWEEN ? AND ?)ORDER BY ID ASC;";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, idFilma);
			pstmt.setString(2, termin);
			pstmt.setString(3, termin+" 23:59:59");
			rset = pstmt.executeQuery();
			while(rset.next()) {
				int index = 1;
				String ID = rset.getString(index++);
				Projekcija p = ProjekcijeDAO.get(Integer.valueOf(ID));
				lista.add(p);
				
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
	public static ArrayList<JSONObject> izvestaj(String termin){
		ArrayList<JSONObject> lista = new ArrayList<JSONObject>();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT ID,Naziv FROM Filmovi WHERE Status='Active' ORDER BY Naziv ASC;";

			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				int index = 1;
				String ID = rset.getString(index++);
				Film f = FilmoviDAO.get(Integer.valueOf(ID));
				JSONObject obj = new JSONObject();
				ArrayList<Projekcija> projekcijeFilma = uzmiProjekcijeZaFilm(ID,termin);
				obj.put("ID", ID);
				obj.put("Naziv",f.getNaziv());
				obj.put("BrojProjekcija",projekcijeFilma.size());
				int brojProdatihKarata = 0;
				double zarada = 0;
				for (Projekcija projekcija : projekcijeFilma) {
					ArrayList<Karta> karte = KarteDAO.karteZaProjekciju(String.valueOf(projekcija.getId()));
					brojProdatihKarata += karte.size();
					zarada += projekcija.getCenaKarte()*karte.size();
				}
				obj.put("brojProdatihKarata", brojProdatihKarata);
				obj.put("zarada",zarada);
				lista.add(obj);
				
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
	
	public static ArrayList<JSONObject> adminPanel_moguciFilmovi(){
		ArrayList<JSONObject> lista = new ArrayList<JSONObject>();
				
				Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = null;
				ResultSet rset = null;
				
				try {
					String query = "SELECT ID,Naziv FROM Filmovi WHERE Status=? ORDER BY Naziv ASC;";
		
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "Active");
					rset = pstmt.executeQuery();
					System.out.println("Proslo je ovo");
					while(rset.next()) {
						System.out.println("Usao sam");
						int index = 1;
						String ID = rset.getString(index++);
						String Naziv = rset.getString(index++);
						JSONObject obj = new JSONObject();
						obj.put("ID", ID);
						obj.put("Naziv",Naziv);
						lista.add(obj);
						
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
	public static ArrayList<JSONObject> adminPanel_moguceSale(){
		ArrayList<JSONObject> lista = new ArrayList<JSONObject>();
				
				Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = null;
				ResultSet rset = null;
				
				try {
					String query = "SELECT ID,Naziv,ID_Tipova_Projekcija FROM Sale WHERE Status=? ORDER BY Naziv ASC;";
		
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "Active");
					rset = pstmt.executeQuery();
					System.out.println("Proslo je ovo");
					while(rset.next()) {
						System.out.println("Usao sam");
						int index = 1;
						String ID = rset.getString(index++);
						String Naziv = rset.getString(index++);
						ArrayList<JSONObject> listaTipovaProjekcija = new ArrayList<JSONObject>();
						String[] tipovi = rset.getString(index++).split(";");
						for (String string : tipovi) {
							TipProjekacije tp =  TipProjekcijeDAO.get(Integer.valueOf(string));
							if(tp!=null) {
								JSONObject tipJSON = new JSONObject();
								tipJSON.put("ID", tp.getId());
								tipJSON.put("Naziv",tp.getNaziv());
								listaTipovaProjekcija.add(tipJSON);
							}
						}
						JSONObject obj = new JSONObject();
						obj.put("ID", ID);
						obj.put("Naziv",Naziv);
						obj.put("ListaTipovaProjekcija",listaTipovaProjekcija);
						lista.add(obj);
						
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
}
