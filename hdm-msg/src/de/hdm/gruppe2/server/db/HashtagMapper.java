package de.hdm.gruppe2.server.db;

import java.util.Vector;
import java.sql.*;
import de.hdm.gruppe2.shared.bo.*;

public class HashtagMapper {

	private static HashtagMapper hashtagMapper = null;
	
	protected HashtagMapper() {}
	
	public static HashtagMapper hashtagMapper() {
		if(hashtagMapper == null) {
			hashtagMapper = new HashtagMapper();
		}
		
		return hashtagMapper;
	}
	
	public Hashtag insert(Hashtag hashtag) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			
			/*
		     * Zun�chst schauen wir nach, welches der momentan h�chste
		     * Prim�rschl�sselwert ist.
		     * TODO: Queries definieren sobald die Datenbankstruktur steht.
		     */
			ResultSet rs = stmt.executeQuery("");
			// Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
		      if (rs.next()) {
		        /*
		         * hashtag erh�lt den bisher maximalen, nun um 1 inkrementierten
		         * Prim�rschl�ssel.
		         */
		    	 hashtag.setId(rs.getInt("") + 1);

		        stmt = con.createStatement();

		        // Jetzt erst erfolgt die tats�chliche Einf�geoperation
		        stmt.executeUpdate("");
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		
		return hashtag;
	}
	
	public Hashtag update(Hashtag hashtag) {
		return null;
	}
	
	public Hashtag delete(Hashtag hashtag) {
		return null;
	}
	
	public Hashtag findById(int id) {
		return null;
	}
	
	public Vector<Hashtag> getAllHashtags() {
		return null;
	}
}
