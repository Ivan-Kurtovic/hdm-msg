package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe2.shared.bo.Abo;
import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagAbo;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserAbo;

/**
 * Mapper-Klasse, die <code>Abo</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see AboMapper
 * @author Thies & Ivan
 */
public class AboMapper {
	
	/**
	 * Die Klasse AboMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * f�r s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see AboMapper()
	 */
	private static AboMapper aboMapper = null;
	
	/**
	 * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected AboMapper() {
	}
	
	/**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>AboMapper.aboMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
	   * Instanz von <code>AboMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> AboMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>AboMapper</code>-Objekt.
	   * @see aboMapper()
	   */
	  public static AboMapper aboMapper() {
	    if (aboMapper == null) {
	    	aboMapper = new AboMapper();
	    }

	    return aboMapper;
	  }
	  
	  /**
	   * Einf�gen eines <code>Abo</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
	   * berichtigt.
	   * @param a das zu speichernde Objekt
	   * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
	   * <code>id</code>.
	   */
	  
	  /**
	   * Einf�gen eines Hashtag Abo Objektes
	   */
	  public HashtagAbo insertHashtagAbo(HashtagAbo hashtagAbo) {
		  Connection con = DBConnection.connection();
		  
		  try {
		      Statement stmt = con.createStatement();

		      /*
		       * Zun�chst schauen wir nach, welches der momentan h�chste
		       * Prim�rschl�sselwert ist.
		       */
		      
		      //TODO: Statement anpassen sobald DB steht
		      ResultSet rs = stmt.executeQuery("SELECT MAX(aboID) AS maxid "
		          + "FROM Abo ");

		      if (rs.next()) {
		        /*
		         * Man erh�lt den bisher maximalen, nun um 1 inkrementierten
		         * Prim�rschl�ssel.
		         */
		    	  hashtagAbo.setId(rs.getInt("maxid") + 1);
	    	 
		    	  
		    	  // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = hashtagAbo.getCreationDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	 hashtagAbo.setCreationDate(sqlDate);
		     	  
	    	  /*
		       * Das Objekt wird nun in die Datenbank geschrieben 
		       */
		       
		     	  stmt = con.createStatement();
		        
		      //TODO: Statement anpassen sobald DB steht
		        stmt.executeUpdate("INSERT INTO Abo VALUES ('"
		        + hashtagAbo.getId() +"','"
		        + hashtagAbo.getCreationDate() +"');");
		        
		      //TODO Eintrag in Zwischentabelle f�r die Hashtags
		      //TODO Eintrag in Zwischentabelle f�r die User
		      }
		      
		    }
		  catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		  return hashtagAbo;
	  }

	/**
	 * Einf�gen eines User Abo Objektes
	 */
	  public UserAbo insertUserAbo(UserAbo userAbo) {
		  Connection con = DBConnection.connection();
		  
		  try {
		      Statement stmt = con.createStatement();

		      /*
		       * Zun�chst schauen wir nach, welches der momentan h�chste
		       * Prim�rschl�sselwert ist.
		       */
		      
		      //TODO: Statement anpassen sobald DB steht
		      ResultSet rs = stmt.executeQuery("SELECT MAX(aboID) AS maxid "
		          + "FROM Abo ");

		      if (rs.next()) {
		        /*
		         * Man erh�lt den bisher maximalen, nun um 1 inkrementierten
		         * Prim�rschl�ssel.
		         */
		    	  userAbo.setId(rs.getInt("maxid") + 1);
	    	 
		    	  
		    	  // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = userAbo.getCreationDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	 userAbo.setCreationDate(sqlDate);
		     	  
	    	  /*
		       * Das Objekt wird nun in die Datenbank geschrieben 
		       */
		       
		     	  stmt = con.createStatement();
		        
		      //TODO: Statement anpassen sobald DB steht
		        stmt.executeUpdate("INSERT INTO Abo VALUES ('"
		        + userAbo.getId() +"','"
		        + userAbo.getCreationDate() +"');");
		        
		      //TODO Eintrag in Zwischentabelle f�r die Hashtags
		      //TODO Eintrag in Zwischentabelle f�r die User
		      }
		      
		    }
		  catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		  return userAbo;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Hashtag Abo Objekts in die Datenbank.
	   */
	  public HashtagAbo updateHashtagAbo(HashtagAbo hashtagAbo) {
	    Connection con = DBConnection.connection();
	    
	    // F�r Statement ID in Integer umwandeln
	    Integer aboId = new Integer(hashtagAbo.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	    //TODO: Statement anpassen sobald DB steht
	      stmt.executeUpdate("UPDATE User SET "
	      		+"' WHERE userID='"+aboId.toString()+"';");

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    return hashtagAbo;
	  }
	  
	  
	  /**
	   * Wiederholtes Schreiben eines User Abo Objekts in die Datenbank.
	   */
	  public UserAbo updateUserAbo(UserAbo userAbo) {
		    Connection con = DBConnection.connection();
		    
		    // F�r Statement ID in Integer umwandeln
		    Integer aboId = new Integer(userAbo.getId());

		    try {
		      Statement stmt = con.createStatement();
		      
		    //TODO: Statement anpassen sobald DB steht
		      stmt.executeUpdate("UPDATE User SET "
		      		+"' WHERE userID='"+aboId.toString()+"';");
		      
		      //TODO Eintrag in Zwischentabelle f�r die Hashtags
		      //TODO Eintrag in Zwischentabelle f�r die Sender

		    }
		    catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		    return userAbo;
		  }
	  
	  
	  /**
	   * L�schen des Hashtag Abo-Objektes aus der Datenbank 
	   */
	  public void deleteHashtagAbo(HashtagAbo hashtagabo) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      stmt.executeUpdate("DELETE FROM HashtagAbo WHERE aboID ='"+ hashtagabo.getId()+"'");
	    }
	    
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }
	  
	  /**
	   * L�schen des Hashtag Abo-Objektes aus der Datenbank 
	   */
	  public void deleteUserAbo(UserAbo user) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      stmt.executeUpdate("DELETE FROM UserAbo WHERE aboID ='"+ user.getId()+"'");
	    }
	    
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }
	  
	  /**
	   * Abo von einem Hashtag finden 
	   */
	  public HashtagAbo findAboByHashtag(Hashtag hashtag){
			
		  Connection con = DBConnection.connection();
		  HashtagAbo hashtagAbo = null;
			try{
				Statement stmt = con.createStatement();
			    
				//TODO: Statement anpassen sobald DB steht
			    ResultSet rs = stmt.executeQuery("SELECT * FROM HashtagAbo WHERE aboID ='"
			    		+hashtag+"';");
			   
			    //Da es nur einen Message mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enth�lt
			    if(rs.next()){
			    	
			    	//TODO: Useraufbau aufbau anpassen sobald DB steht
			    	hashtagAbo = new HashtagAbo();
			    	hashtagAbo.setId(rs.getInt("aboID"));
			    	
			    	// Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
			        // die Datenbank zu speichern 
			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
			        hashtagAbo.setCreationDate(sqlDate);
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return hashtagAbo;
		}
	  
	  
	  /**
	   * Abo von einem Nutzer finden 
	   */
	  public UserAbo findAboByUser(User user){
			
		  Connection con = DBConnection.connection();
		  UserAbo userAbo = null;
			try{
				Statement stmt = con.createStatement();
			    
				//TODO: Statement anpassen sobald DB steht
			    ResultSet rs = stmt.executeQuery("SELECT * FROM UserAbo WHERE aboID ='"
			    		+user+"';");
			   
			    //Da es nur einen Message mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enth�lt
			    if(rs.next()){
			    	
			    	//TODO: Useraufbau aufbau anpassen sobald DB steht
			    	userAbo = new UserAbo();
			    	userAbo.setId(rs.getInt("aboID"));
			    	
			    	// Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
			        // die Datenbank zu speichern 
			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
			        userAbo.setCreationDate(sqlDate);
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return userAbo;
		}	  

}