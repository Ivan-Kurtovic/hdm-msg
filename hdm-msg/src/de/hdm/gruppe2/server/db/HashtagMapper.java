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
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `dbmessenger`.`hashtag`");
			// Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
		      if (rs.next()) {
		        /*
		         * hashtag erh�lt den bisher maximalen, nun um 1 inkrementierten
		         * Prim�rschl�ssel.
		         */
		    	 hashtag.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        // Jetzt erst erfolgt die tats�chliche Einf�geoperation
		        stmt.executeUpdate("INSERT INTO `dbmessenger`.`hashtag` (`id`, `text`) VALUES (" + hashtag.getId() + ", '" + hashtag.getKeyword() + "')");
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		
		return hashtag;
	}
	
	public Hashtag update(Hashtag hashtag) {

		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      //TODO Query einf�gen sobald die Datenbankstruktur steht.
	      stmt.executeUpdate("");

	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Um Analogie zu insert(Hashtag hashtag) zu wahren, geben wir hashtag zur�ck
	    return hashtag;
	}
	
	public void delete(Hashtag hashtag) {

		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      // TODO Query einf�gen sobald die Datenbankstruktur steht.
	      stmt.executeUpdate("");
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	}
	
	public Hashtag findById(int id) {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
	
	public Hashtag findHashtagByKeyword(String keyword) {
		Connection con = DBConnection.connection();
		
		try {
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtag` WHERE `text` = '" + keyword + "'");
	      
	      if(rs.next()) {
	    	  Hashtag hashtag = new Hashtag();
	    	  
	    	  hashtag.setId(rs.getInt("id"));
	    	  hashtag.setKeyword(rs.getString("text"));
	    	  hashtag.setCreationDate(rs.getDate("creationDate"));
	    	  
	    	  return hashtag;
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return null;
	}
	
	public Vector<Hashtag> getAllHashtags() {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
}
