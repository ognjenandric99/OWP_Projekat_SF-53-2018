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
import bioskop.model.Projekcija;
import bioskop.model.User;
import bioskop.model.Zanr;
import bioskop.model.UlogeUsera;

public class ProjekcijeDAO {
	
	public static boolean smanjiStanjeKarata(String idProjekcije) {
		boolean status = false;
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Projekcije SET BrojProdanihKarata=BrojProdanihKarata+1 WHERE ID=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, idProjekcije);
			
			

			int broj = pstmt.executeUpdate();
			if (broj>0) {
				status = true;
			}

		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
			
		}
		return status;
	}
	
	public static Projekcija get(int id) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		Projekcija proj = null;
		try {
			String query = "SELECT ID,ID_Filma,TipProjekcije,ID_Sale,Termin,CenaKarte,Administrator,Status,MaksimumKarata,BrojProdanihKarata FROM Projekcije  WHERE ID=?;";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				int index = 1;
				String ID1 = rset.getString(index++);
				int idi = Integer.valueOf(ID1);
				String ID_Filma = rset.getString(index++);
				int idFilma = Integer.valueOf(ID_Filma);
				String tipProjekcije = rset.getString(index++);
				String ID_Sale = rset.getString(index++);
				int idSale = Integer.valueOf(ID_Sale);
				String Termin = rset.getString(index++);
				Date datum = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(Termin);
				String CenaKarte = rset.getString(index++);
				int cenaKarte = Integer.valueOf(CenaKarte);
				String Administrator = rset.getString(index++);
				String Status = rset.getString(index++);
				int maks = Integer.valueOf(rset.getString(index++));
				int prod = Integer.valueOf(rset.getString(index++));
				proj = new Projekcija(idi, idFilma, tipProjekcije, idSale, datum, cenaKarte, Administrator,Status,maks,prod);
				status = true;
			}
			else {
				System.out.println("Nisam nasao projekciju");
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
		return proj;
	}
	
	public static JSONObject filterProjekcije(String id_Filma,String pocetak,String pocetakKraj,String idSale,String oznakaTipa,String cenaMin,String cenaMax) {
		JSONObject odg = new JSONObject();
		boolean status = false;
		String message = "Unexpected error.";
		
		ArrayList<Projekcija> lista = new ArrayList<Projekcija>();
				
				Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = null;
				ResultSet rset = null;
				
				try {
					String query = "SELECT ID FROM Projekcije WHERE Status='Active' AND (Termin BETWEEN ? AND ?) AND ID_Filma LIKE ?"
							+ " AND ID_sale LIKE ? AND TipProjekcije LIKE ? AND CenaKarte BETWEEN ? AND ?  ORDER BY ID_Filma ASC,Termin ASC";
		
					pstmt = conn.prepareStatement(query);
		
					pstmt.setString(1,pocetak);
					pstmt.setString(2,pocetakKraj);
					pstmt.setString(3,id_Filma);
					pstmt.setString(4,idSale);
					pstmt.setString(5,oznakaTipa);
					pstmt.setString(6, cenaMin);
					pstmt.setString(7, cenaMax);
					
					
					rset = pstmt.executeQuery();
					while(rset.next()) {
						
						int index = 1;
						String ID = rset.getString(index++);
						Projekcija p = get(Integer.valueOf(ID));
						if(p!=null) {
							lista.add(p);
						}
					status = true;
					message = "Ucitano";
					}
					odg.put("listaProjekcija", lista);
					
					
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
				try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
				try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
			}
		
		odg.put("status", status);
		odg.put("message", message);
		return odg;
	}
	
	public static boolean obrisiProjekciju(String idProjekcije) {
		boolean status = false;
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Projekcije SET Status='Deleted' WHERE ID=?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, idProjekcije);
			
			

			int broj = pstmt.executeUpdate();
			if (broj>0) {
				status = true;
			}

		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
			
		}
		return status;
	}
	public static boolean dodajProjekciju(Projekcija projekcija,String krajTermina) {
		boolean status = false;
		 Connection conn = ConnectionManager.getConnection();
			PreparedStatement pstmt = null;
			try {
				String query = "INSERT INTO Projekcije(ID_Filma,TipProjekcije,ID_Sale,Termin,CenaKarte,Administrator,Status,MaksimumKarata,KrajTermina) VALUES (?,?,?,?,?,?,?,?,?)";

				pstmt = conn.prepareStatement(query);
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				pstmt.setString(1, projekcija.getIdFilma()+"");
				pstmt.setString(2, projekcija.getTipProjekcije());
				pstmt.setString(3, projekcija.getIdSale()+"");
				Date datum = projekcija.getDatum();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");      
				String pocetak = df.format(datum);
				pstmt.setString(4, pocetak+"");
				pstmt.setString(5, projekcija.getCenaKarte()+"");
				pstmt.setString(6, projekcija.getUsernameAdministratora()+"");
				pstmt.setString(7, "Active");
				pstmt.setString(8, projekcija.getMaksimumKarata()+"");
				pstmt.setString(9, krajTermina);
				
				
				

				int broj = pstmt.executeUpdate();
				if (broj>0) {
					status = true;
				}

			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
				try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
				
			}
		return status;
	}
	public static ArrayList<Projekcija> ucitajZaDatum(HttpServletRequest request,String datum) {
		ArrayList<Projekcija> lista = new ArrayList<Projekcija>();
		
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		boolean status = false;
		try {
			String query = "SELECT ID FROM Projekcije  WHERE Status='Active' AND Termin BETWEEN ? AND ? ORDER BY ID_Filma ASC,Termin ASC ;";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, datum);
			pstmt.setString(2,datum+" 23:59:59");
			rset = pstmt.executeQuery();
			while(rset.next()) {
				int index = 1;
				String ID = rset.getString(index++);
				Projekcija p = get(Integer.valueOf(ID));
				if(p!=null) {
					lista.add(p);
				}
				
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
