package bioskop.dao;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class ConnectionManager {

	//                                                                LAPTOP :  C:\Users\BUDI\gitz\OWP_Projekat_SF-53-2018\Bioskop\sql\Bioskop.db
	
	private static final String DATABASE_NAME = "Bioskop.db";

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String WINDOWS_PATH = "C:" + FILE_SEPARATOR + "Users" + FILE_SEPARATOR + "BUDI" + FILE_SEPARATOR +"gitz"+FILE_SEPARATOR+"OWP_Projekat_SF-53-2018"+FILE_SEPARATOR+"Bioskop"+FILE_SEPARATOR+"sql"+FILE_SEPARATOR+ DATABASE_NAME;
	private static final String WINDOWS_PATH_KOMP = "C:" + FILE_SEPARATOR + "Users" + FILE_SEPARATOR + "ognje" + FILE_SEPARATOR +"git"+FILE_SEPARATOR+"OWP_Projekat_SF-53-2018"+FILE_SEPARATOR+"Bioskop"+FILE_SEPARATOR+"sql"+FILE_SEPARATOR+ DATABASE_NAME;

	private static final String LINUX_PATH = "SQLite" + FILE_SEPARATOR + DATABASE_NAME;

	private static final String PATH = WINDOWS_PATH;	

	private static DataSource dataSource;

	public static void open() {
		try {
			Properties dataSourceProperties = new Properties();
			dataSourceProperties.setProperty("driverClassName", "org.sqlite.JDBC");
			dataSourceProperties.setProperty("url", "jdbc:sqlite:" + PATH);
			
			dataSource = BasicDataSourceFactory.createDataSource(dataSourceProperties); // connection pool
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			System.out.println("Pokusao sam");
			return dataSource.getConnection(); // slobodna konekcija se vadi iz pool-a na zahtev
		} catch (Exception ex) {
			System.out.println("Nisam uspeo");
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		return null;
	}

}
