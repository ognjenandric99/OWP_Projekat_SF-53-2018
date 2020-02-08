package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bioskop.model.Film;
import bioskop.model.Sala;
import bioskop.model.TipProjekacije;
import bioskop.model.Zanr;

public class SalaDAO {
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
