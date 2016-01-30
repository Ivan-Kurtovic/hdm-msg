package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.User;

/**
 * Mapper-Klasse, die <code>User</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see MessageMapper, ChatMapper, HashtagMapper, HashtagSubscriptionMapper, UserSubscriptionMapper
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */
public class UserMapper {

  /**
   * Die Klasse UserMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal f�r
   * s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see usermapper()
   */
	private static UserMapper userMapper = null;
	
  /**
   * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
	protected UserMapper() {}
	
  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>UserMapper.usermapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
   * Instanz von <code>UserMapper</code> existiert.
   * <p>
   * 
   * @return DAS <code>UserMapper</code>-Objekt.
   * @see usermapper
   */	
	public static UserMapper usermapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}
		return userMapper;
	}

  /**
   * Einf�gen eines <code>User</code>-Objekts in die Datenbank. Dabei wird
   * auch der Prim�rschl�ssel des �bergebenen Objekts gepr�ft und ggf.
   * berichtigt.
   * 
   * @param user das zu speichernde Objekt
   * @return das bereits �bergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
	public User insert(User user) throws IllegalArgumentException {
		// DB-Verbindung herstellen
		Connection con = DBConnection.connection();
		try {
			
			User tmpUser = findUserByEmail(user.getEmail());
			
			if(tmpUser == null) {
				
				// Insert-Statement erzeugen
				Statement stmt = con.createStatement();
				// Zun�chst wird geschaut welches der momentan h�chste
				// Prim�rschl�ssel ist.
				ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM user");
		
				// Wenn ein Datensatz gefunden wurde, wird auf diesen zugegriffen
				if(rs.next()) {
					// Die gefundene id wird um 1 inkrementiert und in das neue User-Objekt geschrieben.
					user.setId(rs.getInt("maxid") + 1);
				}
				
				String sql = "INSERT INTO `user`(`id`, `email`, `nickname`) "
						+ "VALUES (?, ?, ?)";

				PreparedStatement preStmt;
				preStmt = con.prepareStatement(sql);
				preStmt.setString(1, Integer.toString(user.getId()));
				preStmt.setString(2, user.getEmail());
				preStmt.setString(3, user.getNickname());
				preStmt.executeUpdate();
				preStmt.close();
			} else {
				return tmpUser;
			}
		} catch (SQLException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return user;
	}

  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank.
   * 
   * @param user das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter �bergebene Objekt
   */
	public User update(User user) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			PreparedStatement preStmt;
			preStmt = con.prepareStatement("UPDATE `user` SET nickname=?, email=? WHERE id=" + user.getId());
			preStmt.setString(1, user.getNickname());
			preStmt.setString(2, user.getEmail());
			preStmt.executeUpdate();
			preStmt.close();		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return user;
	}

	/**
	 * L�schen der Daten eines <code>User</code>-Objekts aus der
	 * Datenbank.
	 * 
	 * @param user das aus der DB zu l�schende "Objekt"
	 */
	public void delete(User user) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM `user` WHERE id="	+ user.getId());
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
	}

	/**
	 * Diese Methode erm�glicht es alle Nutzer aus der Datenbank
	 * zu finden und anzuzeigen.
	 * 
	 * @return alle in der Datenbank eingetragene Nutzer
	 */
	public ArrayList<User> findAllUsers() throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		ArrayList<User> allUsers = new ArrayList<User>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` ORDER BY id");
	
			while (rs.next()) {
				User user = new User();
				
				user.setId(rs.getInt("id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
	
				allUsers.add(user);
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return allUsers;
	}

	/**
	 * Diese Methode erm�glicht es alle Nutzer aus der Datenbank
	 * zu finden und anzuzeigen. Dabei wird der �bergebene Nutzer ausgelassen.
	 * 
	 *	@return alle in der Datenbank eingetragene Nutzer au�er dem �bergebenen Nutzer.
	 */
	public ArrayList<User> findAllUsersWithoutLoggedInUser(User loggedInUser) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		ArrayList<User> allUsers = new ArrayList<User>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` WHERE `id` <> " + loggedInUser.getId());
	
			while (rs.next()) {
				User user = new User();
				
				user.setId(rs.getInt("id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
	
				allUsers.add(user);
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return allUsers;
	}

	/**
	 * Diese Methode erm�glicht einen Nutzer anhand seiner Email zu finden
	 * und anzuzeigen.
	 * 
	 * @return uebergebener Paramater
	 */
	public User findUserByEmail(String email) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` WHERE email='"	+ email + "'");
	
			if (rs.next()) {
				User user = new User();
				
				user.setId(rs.getInt("id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
	
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return null;
	}
}


