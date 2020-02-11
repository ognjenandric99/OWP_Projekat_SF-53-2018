package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import bioskop.model.Projekcija;
import bioskop.model.TipProjekacije;

public class AdminDAO {
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
