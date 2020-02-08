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
			System.out.println("SELECT ID FROM Projekcije  WHERE Status='Active' AND Termin BETWEEN "+datum+" AND '"+datum+" 23:59:59' ORDER BY ID_Filma ASC,Termin ASC ;");
			while(rset.next()) {
				System.out.println("USAO SAM U OVO SRANJE");
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
