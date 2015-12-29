package de.hdm.gruppe2.server.db;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Mapper-Klasse, die <code>Hashtag</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see HashtagMapper
 * @author Thies & Ioannidou
 */
public class HashtagMapper {
	
	/**
	 * Die Klasse HashtagMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * f�r s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see hashtagMapper()
	 */
	private static HashtagMapper hashtagMapper = null;
	
	/**
	 * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected HashtagMapper() {
	}
	
	/**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>HashtagMapper.HashtagMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
	   * Instanz von <code>HashtagMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> HashtagMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>HashtagMapper</code>-Objekt.
	   * @see hashtagMapper()
	   */
	  public static HashtagMapper hashtagMapper() {
	    if (hashtagMapper == null) {
	    	hashtagMapper = new HashtagMapper();
	    }

	    return hashtagMapper;
	  }
	  
	  /**
	   * Einf�gen eines <code>Hashtag</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
	   * berichtigt.
	   * @param a das zu speichernde Objekt
	   * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
	   * <code>id</code>.
	   */
	  public Hashtag insertHashtag(Hashtag hashtag) {
		  Connection con = DBConnection.connection();
		  
		  try {
		      Statement stmt = con.createStatement();

		      /*
		       * Zun�chst schauen wir nach, welches der momentan h�chste
		       * Prim�rschl�sselwert ist.
		       */
		      //TODO: Statement anpassen sobald DB steht
		      ResultSet rs = stmt.executeQuery("SELECT MAX(hashtagID) AS maxid "
		          + "FROM Hashtag ");

		      if (rs.next()) {
		        /*
		         * Man erh�lt den bisher maximalen, nun um 1 inkrementierten
		         * Prim�rschl�ssel.
		         */
		    	  hashtag.setId(rs.getInt("maxid") + 1);
	    	 
		    	  
		    	  // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = hashtag.getCreationDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	 hashtag.setCreationDate(sqlDate);
		     	  
	    	  /*
		       * Das Objekt wird nun in die Datenbank geschrieben 
		       */
		       
		     	  stmt = con.createStatement();
		        
		      //TODO: Statement anpassen sobald DB steht
		        stmt.executeUpdate("INSERT INTO Hashtag VALUES ('"
		        + hashtag.getId() +"','"
		        + hashtag.getKeyword() +"','"
		        + hashtag.getCreationDate() +"');");
		        
		      }
		      
		    }
		  catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		  return hashtag;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   */
	  public Hashtag updateHashtag(Hashtag hashtag) {
	    Connection con = DBConnection.connection();
	    
	    // F�r Statement ID in Integer umwandeln
	    Integer hashtagId = new Integer(hashtag.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	      
	    //TODO: Statement anpassen sobald DB steht
	      stmt.executeUpdate("UPDATE Hashtag SET "
	      		+"keyword='"+ hashtag.getKeyword() 
	      		+"' WHERE hashtagID='"+hashtagId.toString()+"';");
	      
	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    return hashtag;
	  }
	  
	  
	  /**
	   * L�schen des Hashtag-Objektes aus der Datenbank 
	   */
	  public void deleteHashtag(Hashtag hashtag) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      stmt.executeUpdate("DELETE FROM Hashtag WHERE hashtagID ='"+ hashtag.getId()+"'");
	    }
	    
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }
	  
	  /**
	   * Eine Hashtag in der Datenbank anhand seiner 
	   * ID finden
	   */
	  public Hashtag findByID (String id){
			Connection con = DBConnection.connection();
			Hashtag hashtag = null;
			try{
				Statement stmt = con.createStatement();
			    
				//TODO: Statement anpassen sobald DB steht
			    ResultSet rs = stmt.executeQuery("SELECT * FROM Hashtag WHERE hashtagID ='"
			    		+id+"';");
			    //Da es nur einen Hashtag mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enth�lt
			    if(rs.next()){
			    	hashtag = new Hashtag();
			    	hashtag.setId(rs.getInt("hashtagID"));
			    	hashtag.setKeyword(rs.getString("keyword"));
			    	
			    	// Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
			        // die Datenbank zu speichern 
			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
			        hashtag.setCreationDate(sqlDate);
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return hashtag;
		}
	  
	  /**
	   * Auslesen aller Hashtag
	   */
	  public Vector<Hashtag> findAll() {
	    
		  Connection con = DBConnection.connection();
	   
	    Vector<Hashtag> resultHashtagVektor = new Vector<Hashtag>();

	    try {
	      Statement stmt = con.createStatement();

	      ResultSet rs = stmt.executeQuery("SELECT * FROM Hashtag ORDER BY keyword");
	      // F�r jeden Eintrag im Suchergebnis wird nun ein Customer-Objekt
	      
	      while (rs.next()) {
	        Hashtag hashtag = new Hashtag();
	        hashtag.setId(rs.getInt("hashtagID"));
	        hashtag.setKeyword(rs.getString("keyword"));
	        
	        // Java Util Date wird umgewandelt in SQL Date um das �nderungsdatum in
	        // die Datenbank zu speichern 
	        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
	        hashtag.setCreationDate(sqlDate);  
	     	 
	        // Hinzuf�gen des neuen Objekts zum Ergebnisvektor
     	 	resultHashtagVektor.addElement(hashtag);
	        
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Ergebnisvektor zur�ckgeben
	    return resultHashtagVektor;
	  }

}