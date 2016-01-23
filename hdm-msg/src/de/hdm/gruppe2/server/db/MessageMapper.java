package de.hdm.gruppe2.server.db;

import java.util.Vector;
import java.sql.*;
import de.hdm.gruppe2.shared.bo.*;

public class MessageMapper {

	private static MessageMapper messageMapper = null;
	
	protected MessageMapper() {}
	
	public static MessageMapper messageMapper() {
		if(messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		
		return messageMapper;
	}
	
	public Message insertPost(Message message) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			
			/*
		     * Zun�chst schauen wir nach, welches der momentan h�chste
		     * Prim�rschl�sselwert ist.
		     */
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `message`");
			// Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
		      if (rs.next()) {
		        /*
		         * message erh�lt den bisher maximalen, nun um 1 inkrementierten
		         * Prim�rschl�ssel.
		         */
		        message.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        // Jetzt erst erfolgt die tats�chliche Einf�geoperation
		        stmt.executeUpdate("INSERT INTO `message` (`id`, `text`, `autorId`) VALUES (" + message.getId() + ", '" + message.getText() + "', " + message.getUserId() + ")");
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		
		return message;
	}
	
	public Message update(Message message) {
		
		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      //TODO Query einf�gen sobald die Datenbankstruktur steht.
	      stmt.executeUpdate("");

	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Um Analogie zu insert(Message message) zu wahren, geben wir message zur�ck
	    return message;
	}
	
	public void delete(Message message) {
		
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
	
	public Vector<Message> findByUser(User user) {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
	
	public Vector<Message> findById(int id) {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
	
	public Vector<Message> findByChat(int chatId) {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
}
