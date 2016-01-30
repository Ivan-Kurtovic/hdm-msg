package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.Hashtag;

/**
 * Mapper-Klasse, die <code>Hashtag</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see UserMapper, ChatMapper, MessageMapper, HashtagSubscriptionMapper, UserSubscriptionMapper
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */
public class HashtagMapper {

  /**
   * Die Klasse HashtagMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal f�r
   * s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see hashtagMapper()
   */
	private static HashtagMapper hashtagMapper = null;

  /**
   * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
	protected HashtagMapper() {}

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>HashtagMapper.hashtagMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
   * Instanz von <code>HashtagMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> HashtagMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>HashtagMapper</code>-Objekt.
   * @see hashtagMapper
   */
	public static HashtagMapper hashtagMapper() {
		if(hashtagMapper == null) {
			hashtagMapper = new HashtagMapper();
		}
		
		return hashtagMapper;
	}

  /**
   * Einf�gen eines <code>Hashtag</code>-Objekts in die Datenbank. Dabei wird
   * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
   * berichtigt.
   * 
   * @param hashtag das zu speichernde Objekt
   * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
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
	
	/**
	 * L�schen der Daten eines <code>Hashtag</code>-Objekts aus der
	 * Datenbank. Etwaige L�schweitergaben aufgrund von Fremdschl�sselbeziehungen
	 * werden �ber die Einstellungen der Datenbank geregelt.
	 * 
	 * @param hashtag das zu l�schende HashtagObjekt
	 */
	public void delete(Hashtag hashtag) {

		Connection con = DBConnection.connection();

		try {
	      Statement stmt = con.createStatement();
	      
	      stmt.executeUpdate("DELETE FROM `dbmessenger`.`hashtag` WHERE `id` = " + hashtag.getId());
	      stmt.close();
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	}
	
 /**
   * Suchen eines Hashtags anhand seines Schlagworts. Da bei der Eingabe
   * darauf geachtet wird das Schlagw�rter einmalig sind, kann davon ausgegangen
   * werden, dass das Ergebnis eindeutig ist.
   * 
   * @param hashtag Das HashtagObjekt nach dessen Schlagwort gesucht wird
   * @return HashtagObjekt nach dem gesucht wurde
   */
	public Hashtag findHashtagByKeyword(Hashtag hashtag) {
		Connection con = DBConnection.connection();
		
		try {
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtag` WHERE `text` = '" + hashtag.getKeyword() + "'");
	      
	      if(rs.next()) {
	    	  
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
	
 /**
   * Suchen aller Hashtags der Datenbank. Es wird eine
   * Liste aller passenden Hashtags zur�ckgegeben.
   * 
   * @return HashtagObjekte der Datenbank
   */
	public ArrayList<Hashtag> findAllHashtags() {
		Connection con = DBConnection.connection();
		ArrayList<Hashtag> allHashtags = new ArrayList<Hashtag>();
		
		try {
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtag`");
	      
	      while(rs.next()) {
	    	  Hashtag hashtag = new Hashtag();
	    	  
	    	  hashtag.setId(rs.getInt("id"));
	    	  hashtag.setKeyword(rs.getString("text"));
	    	  hashtag.setCreationDate(rs.getDate("creationDate"));
	    	  
	    	  allHashtags.add(hashtag);
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return allHashtags;
	}
}
