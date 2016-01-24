package de.hdm.gruppe2.server.db;

import java.util.ArrayList;
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
		     * Zunächst schauen wir nach, welches der momentan höchste
		     * Primärschlüsselwert ist.
		     */
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `message`");
			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
		      if (rs.next()) {
		        /*
		         * message erhält den bisher maximalen, nun um 1 inkrementierten
		         * Primärschlüssel.
		         */
		        message.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
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
	      //TODO Query einfügen sobald die Datenbankstruktur steht.
	      stmt.executeUpdate("");

	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Um Analogie zu insert(Message message) zu wahren, geben wir message zurück
	    return message;
	}
	
	public void delete(int messageId) {
		
		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      
	      stmt.executeUpdate("DELETE FROM `dbmessenger`.`message` WHERE `id` = " + messageId);
	      stmt.close();
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	}
	
	public Vector<Message> findByUser(User user) {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
	
	public Message findMessageById(int messageId) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`message` WHERE `id` = " + messageId);
			
			if(rs.next()) {
				Message message = new Message();
				
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorId"));
				message.setChatId(rs.getInt("chatId"));
				message.setHashtagList(getAllHashtagsOfMessage(rs.getInt("id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				return message;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Vector<Message> findByChat(int chatId) {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
	
	public ArrayList<Message> findAllPostsOfUser(int userId) {
		
		Connection con = DBConnection.connection();
		ArrayList<Message> result = new ArrayList<Message>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`message` WHERE `chatId` IS NULL AND `autorId` = " + userId);
			
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorId"));
				message.setChatId(-1);
				message.setHashtagList(getAllHashtagsOfMessage(rs.getInt("id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				result.add(message);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Hashtag> getAllHashtagsOfMessage(int messageId) {
		
		Connection con = DBConnection.connection();
		ArrayList<Hashtag> result = new ArrayList<Hashtag>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `messagehashtags`.`hashtagId`, `hashtag`.`text`, `hashtag`.`creationDate` "
											+ "FROM `hashtag` INNER JOIN `messagehashtags` "
											+ "ON `hashtag`.`id` = `messagehashtags`.`hashtagId` "
											+ "WHERE `messagehashtags`.`messageId` = " + messageId);
			
			while(rs.next()) {
				Hashtag hashtag = new Hashtag();
				hashtag.setId(rs.getInt("hashtagid"));
				hashtag.setKeyword(rs.getString("text"));
				hashtag.setCreationDate(rs.getDate("creationDate"));
				
				result.add(hashtag);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
