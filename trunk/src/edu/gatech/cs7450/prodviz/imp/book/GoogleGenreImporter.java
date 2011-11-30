package edu.gatech.cs7450.prodviz.imp.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.gatech.cs7450.prodviz.data.Database;
import edu.gatech.cs7450.prodviz.data.DatabaseConfig;
import edu.gatech.cs7450.prodviz.data.IDatabaseConfig;
import edu.gatech.cs7450.prodviz.data.SQLDatabase;

public class GoogleGenreImporter {

	public static void main(String[] args) {
		GoogleGenreImporter imp = new GoogleGenreImporter();
		imp.run();
		System.exit(0);
	}
	
	public void run() {
		
		Connection conn = null;
		
		try {
			// get connection to the database
			IDatabaseConfig config = new DatabaseConfig(
					"com.mysql.jdbc.Driver",
					"jdbc:mysql://localhost/",
					"books",
					"admin",
					"admin");
			Database database = new SQLDatabase(new BookImp(), config);
			conn = database.getConnection();
			
			// how many books already have a genre
			Statement s = conn.createStatement();
			ResultSet results = s.executeQuery("SELECT COUNT(*) FROM `BX-Books` WHERE genre IS NOT NULL");
			int i = results.getInt(1);
			
			// iterate over all books in database, extract Genre info, and update corresponding entry
			s = conn.createStatement();
			results = s.executeQuery("SELECT * FROM `BX-Books` WHERE genre IS NULL");
			while (results.next()) {
			
				// wait briefly to not DOS the API server (0.5 seconds)
				Thread.sleep(1 * 500);
				
				// udpate genre
				String isbn = results.getString("ISBN");
				String genre = GoogleBooksApi.getInstance().getGenre(isbn);
				
				PreparedStatement ps = conn.prepareStatement("UPDATE `BX-Books` SET genre=? WHERE ISBN=?");
				ps.setString(1, genre);
				ps.setString(2, isbn);
				ps.executeUpdate();
				
				System.out.println("Found entry [" + ++i + "] with genre [" + genre + "]");
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.err.println("Failed to close database connection.");
				}
			}
		}
	}
}
