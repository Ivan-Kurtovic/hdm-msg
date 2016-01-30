package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserSubscription;

/**
 * Mapper-Klasse, die <code>UserSubscription</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verf�gung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gel�scht werden k�nnen. Das Mapping ist bidirektional. D.h., Objekte k�nnen
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see UserMapper, HashtagMapper, MessageMapper, HashtagSubscriptionMapper, ChatMapper
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */
public class UserSubscriptionMapper {

  /**
   * Die Klasse UserSubscriptionMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal f�r
   * s�mtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see userSubscriptionMapper()
   */
	private static UserSubscriptionMapper userSubscriptionMapper = null;

  /**
   * Gesch�tzter Konstruktor - verhindert die M�glichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
	protected UserSubscriptionMapper() {}

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>UserSubscriptionMapper.userSubscriptionMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie daf�r sorgt, dass nur eine einzige
   * Instanz von <code>UserSubscriptionMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> UserSubscriptionMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>UserSubscriptionMapper</code>-Objekt.
   * @see userSubscriptionMapper
   */
	public static UserSubscriptionMapper userSubscriptionMapper() {
		if(userSubscriptionMapper == null) {
			userSubscriptionMapper = new UserSubscriptionMapper();
		}
		
		return userSubscriptionMapper;
	}

  /**
   * Einf�gen eines <code>UserSubscription</code>-Objekts in die Datenbank. Der
   * Prim�rschl�ssel wird dabei automatisch von der Datenbank erzeugt.
   * 
   * @param us das zu speichernde UserSubscription Objekt
   * 
   */
	public void insert(UserSubscription us) {
		Connection con = DBConnection.connection();
		
		try {
			if(findByRecipientAndSenderId(us.getRecipientId(), us.getSenderId()) == null) {
				Statement stmt = con.createStatement();			
				stmt.executeUpdate("INSERT INTO `dbmessenger`.`usersubscription`(`posterId`, `subscriberId`) VALUES (" + us.getSenderId() + ", " + us.getRecipientId() + " )");					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
 /**
   * L�schen eines Nutzerabonnements aus der Datenbank.
   * 
   * @param senderId Die Id des Users der abonniert wurde
   * @param recipientId Die Id des abonnierenden Users
   * 
   */
	public void delete(int senderId, int recipientId) {
		Connection con = DBConnection.connection();
		
		try {			
			Statement stmt = con.createStatement();			
			stmt.executeUpdate("DELETE FROM `dbmessenger`.`usersubscription` WHERE `posterId` = " 
			+ senderId + " AND `subscriberId` = " 
			+ recipientId);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
 /**
   * Suchen eines Nutzerabonnements anhand der ID des abonnierenden und abonnierten Nutzers.
   * 
   * @param senderId Die Id des Users der abonniert wurde
   * @param recipientId Die Id des abonnierenden Users
   * @return UserSubscription Objekt das das Nutzerabonnement repr�sentiert
   */
	public UserSubscription findByRecipientAndSenderId(int recipientId, int senderId) {
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`usersubscription` WHERE `posterId` = " + senderId + " AND `subscriberId` = " + recipientId);
			
			if(rs.next()) {
				UserSubscription us = new UserSubscription();
				us.setSenderId(rs.getInt("posterId"));
				us.setRecipientId(rs.getInt("subscriberId"));		
				return us;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

 /**
   * Suchen aller Nutzerabonnements eines bestimmten Nutzers.
   * 
   * @param user Das NutzerObjekt nach dessen Abonnements gesucht wird
   * @return UserSubscription Objekte nach denen gesucht wurde
   */
	public ArrayList<UserSubscription> findAllUserSubscriptionsOfUser(User user) {
		Connection con = DBConnection.connection();
		ArrayList<UserSubscription> subscriptions = new ArrayList<UserSubscription>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`usersubscription` WHERE `subscriberId` = " + user.getId());
			
			while(rs.next()) {
				UserSubscription us = new UserSubscription();
				us.setSenderId(rs.getInt("posterId"));
				us.setRecipientId(rs.getInt("subscriberId"));		
				subscriptions.add(us);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriptions;
	}

 /**
   * Suchen aller Abonnenten eines bestimmten Nutzers.
   * 
   * @param u Das NutzerObjekt nach dessen Abonnenten gesucht wird
   * @return User Objekte nach denen gesucht wurde
   */
	public ArrayList<User> findAllFollowersOfUser(User u) {
		Connection con = DBConnection.connection();
		ArrayList<User> subscriptions = new ArrayList<User>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `user`.`id`, `user`.`email`, `user`.`nickname`, `user`.`creationDate` "
											+ "FROM `dbmessenger`.`user` INNER JOIN `dbmessenger`.`usersubscription` "
											+ "ON `dbmessenger`.`user`.`id` = `dbmessenger`.`usersubscription`.`subscriberId`"
											+ "WHERE `posterId` = " + u.getId());
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setNickname(rs.getString("nickname"));
				user.setCreationDate(rs.getDate("creationDate"));
				subscriptions.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriptions;
	}	
}
