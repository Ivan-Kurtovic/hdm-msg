package de.hdm.gruppe2.server.db;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe2.shared.bo.User;

/**
 * Mapper-Klasse, die <code>User</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see UserMapper
 * @author Thies & Ivan Kurtovic und Serkan Yilmaz
 */
public class UserMapper {

	/**
	 * Die Klasse userMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * f�r s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see UserMapper()
	 */
	private static UserMapper userMapper = null;
	
	/**
	 * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected UserMapper() {
	}
	
	 /**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>UserMapper.userMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
	   * Instanz von <code>UserMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> UserMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>UserMapper</code>-Objekt.
	   * @see userMapper()
	   */
	  public static UserMapper userMapper() {
	    if (userMapper == null) {
	    	userMapper = new UserMapper();
	    }

	    return userMapper;
	  }
	  
	  /**
	   * Einf�gen eines <code>Users</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
	   * berichtigt.
	   * @param a das zu speichernde Objekt
	   * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
	   * <code>id</code>.
	   */
	  public User insertUser(User user) {
		  Connection con = DBConnection.connection();
		  
		  try {
		      Statement stmt = con.createStatement();

		      /*
		       * Zun�chst schauen wir nach, welches der momentan h�chste
		       * Prim�rschl�sselwert ist.
		       */
		      //TODO: Statement anpassen sobald DB steht
		      ResultSet rs = stmt.executeQuery("SELECT MAX(userId) AS maxid "
		          + "FROM User ");

		      if (rs.next()) {
		        /*
		         * u erh�lt den bisher maximalen, nun um 1 inkrementierten
		         * Prim�rschl�ssel.
		         */
		    	  user.setId(rs.getInt("maxid") + 1);
	    	 
		    	  
		    	  // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = user.getCreationDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	  user.setCreationDate(sqlDate);
		     	  
	    	  /*
		       * Das Objekt wird nun in die Datenbank geschrieben 
		       */
		       
		     	  stmt = con.createStatement();
		        
		      //TODO: Statement anpassen sobald DB steht
		        stmt.executeUpdate("INSERT INTO User VALUES ('"
		        + user.getId() +"','"
		        + user.getFirstName() +"','"
		        + user.getLastName() +"','"
		        + user.getEmail() +"','"
		        + user.getGoogleId() +"','"
		        + user.getCreationDate() +"');");
		      }
		      
		    }
		  catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		  return user;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   */
	  public User updateUser(User user) {
	    Connection con = DBConnection.connection();
	    
	    // F�r Statement ID in Integer umwandeln
	    Integer userId = new Integer(user.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	      
	    //TODO: Statement anpassen sobald DB steht
	      stmt.executeUpdate("UPDATE User SET "
	      		+"firstName='"+ user.getFirstName() 
	      		+"',lastName='"+ user.getLastName() 
	      		+"',email='"+ user.getEmail() 
	      		+"' WHERE userID='"+userId.toString()+"';");

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    return user;
	  }
	  
	  /**
	   * L�schen des User-Objektes aus der Datenbank 
	   */
	  public void deleteUser(User user) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      stmt.executeUpdate("DELETE FROM User WHERE userID ='"+ user.getId()+"'");
	    }
	    
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }

	  /**
	   * Einen Nutzer in der Datenbank anhand seiner 
	   * GoogleId finden
	   */
	  public User findByGoogleID (String googleId){
			Connection con = DBConnection.connection();
			User user = null;
			try{
				Statement stmt = con.createStatement();
			    
				//TODO: Statement anpassen sobald DB steht
			    ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE googleId ='"
			    		+googleId+"';");
			    //Da es nur einen User mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enth�lt
			    if(rs.next()){
			    	user = new User();
			    	user.setId(rs.getInt("userId"));
			    	user.setFirstName(rs.getString("firstName"));
			    	user.setLastName(rs.getString("lastName"));
			    	user.setEmail(rs.getString("email"));
			    	user.setGoogleId(rs.getString("googleId"));
			    	
			    	// Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
			        // die Datenbank zu speichern 
			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
		     	 	user.setCreationDate(sqlDate);
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return user;
		}
	  
	  /**
	   * Auslesen aller Nutzer
	   */
	  public Vector<User> findAll() {
	    
		  Connection con = DBConnection.connection();
	   
	    Vector<User> resultUserVektor = new Vector<User>();

	    try {
	      Statement stmt = con.createStatement();

	      ResultSet rs = stmt.executeQuery("SELECT * FROM User ORDER BY email");
	      // F�r jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
	      
	      while (rs.next()) {
	        User user = new User();
	        user.setId(rs.getInt("userId"));
	        user.setFirstName(rs.getString("firstName"));
	        user.setLastName(rs.getString("lastName"));
	        user.setEmail(rs.getString("email"));
	        user.setGoogleId(rs.getString("googleId"));
	        
	        // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
	        // die Datenbank zu speichern 
	        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
     	 	user.setCreationDate(sqlDate);  
	     	 
	        // Hinzuf�gen des neuen Objekts zum Ergebnisvektor
     	 	resultUserVektor.addElement(user);
	        
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Ergebnisvektor zur�ckgeben
	    return resultUserVektor;
	  }
	  
	
}