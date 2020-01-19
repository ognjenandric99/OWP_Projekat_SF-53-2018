package bioskop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bioskop.model.Film;
import bioskop.model.Zanr;

public class FilmoviDAO {

	public static Film getFilm(String id) throws Exception {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT ID, Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis FROM Filmovi WHERE id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			System.out.println(pstmt);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				int index = 1;
				int ID = Integer.valueOf(rset.getString(index++));
				String Naziv = rset.getString(index++);
				String Reziser = rset.getString(index++);
				String Glumci = rset.getString(index++);
				String Zanrovi = rset.getString(index++);
				int Trajanje = Integer.valueOf(rset.getString(index++));
				String Distributer = rset.getString(index++);
				String Zemlja_Porekla = rset.getString(index++);
				int Godina_Proizvodnje = Integer.valueOf(rset.getString(index++));
				String Opis = rset.getString(index++);
				
				//Sredjivanje za pravljenje objekta
				ArrayList<Zanr> Zanrovi_n = new ArrayList<Zanr>();
				String[] Zanrs = Zanrovi.split(";");
				for (String znr : Zanrs) {
					try{
						Zanrovi_n.add(Zanr.valueOf(znr));
					}
					catch(Exception e){
						System.out.println("Puklo kod unosa zanra - "+e);
					}
				}

				return new Film(ID, Naziv, Reziser, Glumci, Zanrovi_n, Trajanje, Distributer, Zemlja_Porekla, Godina_Proizvodnje, Opis);			}
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return null;
	}

	/*public static List<Product> getAll(String name, double lowPrice, double highPrice) throws Exception {
		List<Product> products = new ArrayList<>();

		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT id, name, price FROM products WHERE "
					+ "name LIKE ? AND price >= ? AND price <= ?";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, "%" + name + "%");
			pstmt.setDouble(index++, lowPrice);
			pstmt.setDouble(index++, highPrice);
			System.out.println(pstmt);

			rset = pstmt.executeQuery();

			while (rset.next()) {
				index = 1;
				String productID = rset.getString(index++);
				String productName = rset.getString(index++);
				Double productPrice = rset.getDouble(index++);

				Product product = new Product(productID, productName, productPrice);
				products.add(product);
			}
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
		
		return products;
	}

	public static boolean add(Product product) throws Exception {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO products (name, price) "
					+ "VALUES (?, ?)";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, product.getName());
			pstmt.setDouble(index++, product.getPrice());
			System.out.println(pstmt);

			return pstmt.executeUpdate() == 1;
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
	}

	public static boolean update(Product product) throws Exception {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE products SET name = ?, price = ? "
					+ "WHERE id = ?";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, product.getName());
			pstmt.setDouble(index++, product.getPrice());
			pstmt.setString(index++, product.getId());
			System.out.println(pstmt);

			return pstmt.executeUpdate() == 1;
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();}
		}
	}

	public static boolean delete(String id) throws Exception {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM products WHERE id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			System.out.println(pstmt);

			return pstmt.executeUpdate() == 1;
		} finally {
			try {pstmt.close();} catch (Exception ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (Exception ex1) {ex1.printStackTrace();} // ako se koristi DBCP2, konekcija se mora vratiti u pool
		}
	}*/

}
