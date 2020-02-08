package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bioskop.model.Film;
import bioskop.model.TipProjekacije;
import bioskop.model.Zanr;

public class TipProjekcijeDAO {
	public static TipProjekacije get(int id) throws SQLException {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		TipProjekacije tip  = null;
		try {
			String query = "SELECT ID,Naziv FROM Tipovi_Projekcija WHERE ID = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,String.valueOf(id));

			rset = pstmt.executeQuery();
			if (rset.next()) {
				int index = 1;
				int ID = Integer.valueOf(rset.getString(index++));
				String Naziv = rset.getString(index++);
				tip = new TipProjekacije(ID, Naziv);
			}
			else {
				System.out.println("Vraceno 0 redova");
			}

		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return tip;
	}
}
