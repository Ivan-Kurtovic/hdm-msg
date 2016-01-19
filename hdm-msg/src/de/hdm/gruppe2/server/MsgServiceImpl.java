package de.hdm.gruppe2.server;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe2.server.db.*;
import de.hdm.gruppe2.shared.*;
import de.hdm.gruppe2.shared.bo.*;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.gruppe2.shared.LoginInfo;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * <p>
 * Implementierungsklasse des Interface <code>Sms</code>. Diese Klasse ist
 * <em>die</em> Klasse, die neben {@link SmsReportImpl} s�mtliche
 * Applikationslogik (oder engl. Business Logic) aggregiert. Sie ist wie eine
 * Spinne, die s�mtliche Zusammenh�nge in ihrem Netz (in unserem Fall die Daten
 * der Applikation) �berblickt und f�r einen geordneten Ablauf und dauerhafte
 * Konsistenz der Daten und Abl�ufe sorgt.
 * </p>
 * <p>
 * Die Applikationslogik findet sich in den Methoden dieser Klasse. Jede dieser
 * Methoden kann als <em>Transaction Script</em> bezeichnet werden. Dieser Name
 * l�sst schon vermuten, dass hier analog zu Datenbanktransaktion pro
 * Transaktion gleiche mehrere Teilaktionen durchgef�hrt werden, die das System
 * von einem konsistenten Zustand in einen anderen, auch wieder konsistenten
 * Zustand �berf�hren. Wenn dies zwischenzeitig scheitern sollte, dann ist das
 * jeweilige Transaction Script daf�r verantwortlich, eine Fehlerbehandlung
 * durchzuf�hren.
 * </p>
 * <p>
 * Diese Klasse steht mit einer Reihe weiterer Datentypen in Verbindung. Dies
 * sind:
 * <ol>
 * <li>{@link Sms}: Dies ist das <em>lokale</em> - also Server-seitige -
 * Interface, das die im System zur Verf�gung gestellten Funktionen deklariert.</li>
 * <li>{@link SmsAsync}: <code>SmsImpl</code> und<code>Sms</code> bilden nur die
 * Server-seitige Sicht der Applikationslogik ab. Diese basiert vollst�ndig auf
 * synchronen Funktionsaufrufen. Wir m�ssen jedoch in der Lage sein,
 * Client-seitige asynchrone Aufrufe zu bedienen. Dies bedingt ein weiteres
 * Interface, das in der Regel genauso benannt wird, wie das synchrone
 * Interface, jedoch mit dem zus�tzlichen Suffix "Async". Es steht nur mittelbar
 * mit dieser Klasse in Verbindung. Die Erstellung und Pflege der Async
 * Interfaces wird durch das Google Plugin semiautomatisch unterst�tzt. Weitere
 * Informationen unter {@link SmsAsync}.</li>
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig �ber GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis f�r die Anbindung von <code>SmsImpl</code> an die Runtime des GWT
 * RPC-Mechanismus.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Wichtiger Hinweis:</b> Diese Klasse bedient sich sogenannter
 * Mapper-Klassen. Sie geh�ren der Datenbank-Schicht an und bilden die
 * objektorientierte Sicht der Applikationslogik auf die relationale
 * organisierte Datenbank ab. Zuweilen kommen "kreative" Zeitgenossen auf die
 * Idee, in diesen Mappern auch Applikationslogik zu realisieren. Einzig
 * nachvollziehbares Argument f�r einen solchen Ansatz ist die Steigerung der
 * Performance umfangreicher Datenbankoperationen. Doch auch dieses Argument
 * zieht nur dann, wenn wirklich gro�e Datenmengen zu handhaben sind. In einem
 * solchen Fall w�rde man jedoch eine entsprechend erweiterte Architektur
 * realisieren, die wiederum s�mtliche Applikationslogik in der
 * Applikationsschicht isolieren w�rde. Also, keine Applikationslogik in die
 * Mapper-Klassen "stecken" sondern dies auf die Applikationsschicht
 * konzentrieren!
 * </p>
 * <p>
 * Beachten Sie, dass s�mtliche Methoden, die mittels GWT RPC aufgerufen werden
 * k�nnen ein <code>throws IllegalArgumentException</code> in der
 * Methodendeklaration aufweisen. Diese Methoden d�rfen also Instanzen von
 * {@link IllegalArgumentException} auswerfen. Mit diesen Exceptions k�nnen z.B.
 * Probleme auf der Server-Seite in einfacher Weise auf die Client-Seite
 * transportiert und dort systematisch in einem Catch-Block abgearbeitet werden.
 * </p>
 * <p>
 * Es gibt sicherlich noch viel mehr �ber diese Klasse zu schreiben. Weitere
 * Infos erhalten Sie in der Lehrveranstaltung.
 * </p>
 * 
 * @see Sms
 * @see SmsAsync
 * @see RemoteServiceServlet
 * @author Thies, Kurtovic, Cem , Marina
 */
@SuppressWarnings("serial")
public class MsgServiceImpl extends RemoteServiceServlet implements
		MsgService {

	
	/**
	 * DatenbankMapper.
	 */
	private UserMapper userMapper = null;
	private MessageMapper messageMapper = null;
	private ChatMapper chatMapper = null;
	private HashtagMapper hashtagMapper = null;
	private AboMapper aboMapper = null;
	
	private LoginInfo logInfo = null;
	
	/**
	 * Da diese Klasse ein gewisse Gr��e besitzt - dies ist eigentlich ein
	 * Hinweise, dass hier eine weitere Gliederung sinnvoll ist - haben wir zur
	 * besseren �bersicht Abschnittskomentare eingef�gt. Sie leiten ein Cluster
	 * in irgeneinerweise zusammengeh�riger Methoden ein. Ein entsprechender
	 * Kommentar steht am Ende eines solchen Clusters.
	 */

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Initialisierung
	 * ***************************************************************************
	 */
	
	/**
	 * Initialsierungsmethode. Siehe dazu Anmerkungen zum
	 * No-Argument-Konstruktor {@link #ReportGeneratorImpl()}. Diese Methode
	 * muss f�r jede Instanz von <code>BankVerwaltungImpl</code> aufgerufen
	 * werden.
	 * 
	 * @see #ReportGeneratorImpl()
	 */
	public MsgServiceImpl() throws IllegalArgumentException {
		/*
		 * Eine weitergehende Funktion muss der No-Argument-Constructor nicht
		 * haben. Er muss einfach vorhanden sein.
		 */
	}
	
	/**
	 * <p>
	 * Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	 * <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	 * ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines
	 * anderen Konstruktors ist durch die Client-seitige Instantiierung durch
	 * <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
	 * m�glich.
	 * </p>
	 * <p>
	 * Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
	 * Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	 * aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	 * </p>
	 * 
	 * @see #init()
	 */
	@Override
	public void init() throws IllegalArgumentException {
		/*
		 * Ganz wesentlich ist, dass die BankAdministration einen vollst�ndigen
		 * Satz von Mappern besitzt, mit deren Hilfe sie dann mit der Datenbank
		 * kommunizieren kann.
		 */
		
		this.userMapper = UserMapper.userMapper();
		this.messageMapper = MessageMapper.messageMapper();
		this.chatMapper = ChatMapper.chatMapper();
		this.hashtagMapper = HashtagMapper.hashtagMapper();
		this.aboMapper = AboMapper.aboMapper();
	}
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Initialisierung
	 * ***************************************************************************
	 */
	
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden f�r die User Verwaltung
	 * ***************************************************************************
	 */
	/**
	 * <p>
	 * Anlegen eines neuen Users. Dies f�hrt implizit zu einem Speichern des
	 * neuen User in der Datenbank.
	 * </p>
	 * 
	 * <p>
	 * <b>HINWEIS:</b> �nderungen an User-Objekten m�ssen stets durch Aufruf
	 * von {@link #save(User b)} in die Datenbank transferiert werden.
	 * </p>
	 * 
	 * @see createUser(User user)
	 */
	@Override
	public User createUser(User user)
			throws IllegalArgumentException {
		
		//TODO Login einbauen und Kommentar entfernen 
		// Setzen des Users
		//b.setEditUser(logInfo.getUser());
		
		User u = new User();
		u.setGoogleId(user.getGoogleId());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setEmail(user.getEmail());
		
		// Erstellungsdatum wird generiert und dem Objekt angeh�ngt
		// Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		Date creationDate = new Date();
		u.setCreationDate(creationDate);

		// Objekt in der DB speichern.
		return this.userMapper.insertUser(u);
	}
	
	/**
	 * Speichern eines Nutzers.
	 * @see #saveUser(User user)
	 */
	@Override
	public void saveUser(User user) throws IllegalArgumentException{

		//TODO Login einbauen und Kommentar entfernen 
		// Setzen des Users
		//b.setEditUser(logInfo.getUser());
		
		User u = new User();
		u.setGoogleId(user.getGoogleId());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setEmail(user.getEmail());
		
		// Objekt in der DB speichern.
		this.userMapper.updateUser(u);
	}

	/**
	 * L�schen eines Nutzers. 
	 * @see #deleteUser(User user)
	 */
	@Override
	public void deleteUser(User user) throws IllegalArgumentException {
		
		// Objekt in der DB l�schen.
		this.userMapper.deleteUser(user);
	}
	
	/**
	 * Auslesen aller Nutzer.
	 * @see #getAllUsers()
	 */
	@Override
	public Vector<User> getAllUser() throws IllegalArgumentException {
		
		// Objekte aus der Datenbank holen und im Vektor zur�ckgeben.
		return this.userMapper.findAll();
		
	}
	
	/**
	 * Auslesen eines Users anhand seiner GoogleId.
	 * @see #getUserByGoogleId(int googleId)
	 */
	@Override
	public User getUserByGoogleId(String googleId) throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.userMapper.findByGoogleID(googleId);
		
	}
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden f�r die User Verwaltung
	 * ***************************************************************************
	 */
	
	
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden f�r die Message Verwaltung
	 * ***************************************************************************
	 */
	/**
	 * <p>
	 * Anlegen eines neuen Messages. Dies f�hrt implizit zu einem Speichern der
	 * neuen Message in der Datenbank.
	 * </p>
	 * 
	 * <p>
	 * <b>HINWEIS:</b> �nderungen an Message-Objekten m�ssen stets durch Aufruf
	 * von {@link #save(Message m)} in die Datenbank transferiert werden.
	 * </p>
	 * 
	 * @see createMessage(Message message)
	 */
	@Override
	public Message createMessage(Message message)
			throws IllegalArgumentException {
		
		Message m = new Message();
		m.setId(message.getId());
		m.setText(message.getText());
		m.setSender(message.getSender());
		m.setChat(message.getChat());
		m.setHashtagList(message.getHashtagList());
		
		// Erstellungsdatum wird generiert und dem Objekt angeh�ngt
		// Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		Date creationDate = new Date();
		m.setCreationDate(creationDate);

		// Objekt in der DB speichern.
		return this.messageMapper.insertMessage(m);
	}
	
	/**
	 * Speichern einer Message.
	 * @see #saveMessage(Message message)
	 */
	@Override
	public void saveMessage(Message message) 
			throws IllegalArgumentException{
		
		// Objekt in der DB speichern.
		this.messageMapper.updateMessage(message);
	}

	/**
	 * L�schen einer Message. 
	 * @see #deleteMessage(Message message)
	 */
	@Override
	public void deleteMessage(Message message) 
			throws IllegalArgumentException {
		
		// Objekt in der DB l�schen.
		this.messageMapper.deleteMessage(message);
	}
	
	/**
	 * Auslesen einer Message anhand der Id.
	 * @see #getMessageById(int id)
	 */
	@Override
	public Message getMessageById(int id)
			throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.messageMapper.findByID(id);
	}
	
	/**
	 * Auslesen einer Message anhand eines Nutzers 
	 * und eines Zeitraumes.
	 * @see #getMessageByUserAndTime(User user, Timestamp startTime, Timestamp endTime )
	 */
	@Override
	public Message getMessageByUserAndTime(User user, Timestamp startTime, Timestamp endTime )
			throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.messageMapper.findByUserAndTime(user,startTime,endTime);
	}
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden f�r die Message Verwaltung
	 * ***************************************************************************
	 */
	
	
	
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden f�r die Chat Verwaltung
	 * ***************************************************************************
	 */
	/**
	 * <p>
	 * Anlegen eines neuen Chats. Dies f�hrt implizit zu einem Speichern der
	 * neuen Chats in der Datenbank.
	 * </p>
	 * <p>
	 * <b>HINWEIS:</b> �nderungen an Chats-Objekten m�ssen stets durch Aufruf
	 * von {@link #save(Chat c)} in die Datenbank transferiert werden.
	 * </p>
	 * 
	 * @see createChat(Chat chat)
	 */
	@Override
	public Chat createChat(Chat chat)
			throws IllegalArgumentException {
		
		Chat c = new Chat();
		c.setId(chat.getId());
		c.setMemberList(chat.getMemberList());
		c.setMessageList(chat.getMessageList());
		
		// Erstellungsdatum wird generiert und dem Objekt angeh�ngt
		// Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		Date creationDate = new Date();
		c.setCreationDate(creationDate);

		// Objekt in der DB speichern.
		return this.chatMapper.insertChat(c);
	}
	
	/**
	 * Speichern eines Chats.
	 * @see #saveChat(Chat chat)
	 */
	@Override
	public void saveChat(Chat chat) 
			throws IllegalArgumentException{
		
		// Objekt in der DB speichern.
		this.chatMapper.updateChat(chat);
	}

	/**
	 * L�schen eines Chats. 
	 * @see #deleteChat(Chat chat)
	 */
	@Override
	public void deleteChat(Chat chat) 
			throws IllegalArgumentException {
		
		// Objekt in der DB l�schen.
		this.chatMapper.deleteChat(chat);
	}
	
	/**
	 * Auslesen eines Chats anhand der Id.
	 * @see #getChatById(int id)
	 */
	@Override
	public Chat getChatById(int id)
			throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.chatMapper.findByID(id);
	}
	
	/**
	 * Auslesen eines Chats anhand eines Nutzers 
	 * @see #getChatByUser(User user)
	 */
	@Override
	public Chat getChatByUser(User user)
			throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.chatMapper.findByUser(user);
	}
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden f�r die Chat Verwaltung
	 * ***************************************************************************
	 */
	
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden f�r Hashtag
	 * ***************************************************************************
	 */
	/**
	 * <p>
	 * Anlegen eines neuen Hashtag. Dies f�hrt implizit zu einem Speichern des
	 * neuen Hashtag in der Datenbank.
	 * </p>
	 * 
	 * <p>
	 * <b>HINWEIS:</b> �nderungen an User-Objekten m�ssen stets durch Aufruf
	 * von {@link #save(Hashtag h)} in die Datenbank transferiert werden.
	 * </p>
	 * 
	 * @see createHashtag(Hashtag hashtag)
	 */
	@Override
	public Hashtag createHashtag(Hashtag hashtag)
			throws IllegalArgumentException {
		
		//TODO Login einbauen und Kommentar entfernen 
		// Setzen des Users
		//b.setEditUser(logInfo.getUser());
		
		Hashtag h = new Hashtag();
		h.setId(hashtag.getId());
		h.setKeyword(hashtag.getKeyword());
		
		// Erstellungsdatum wird generiert und dem Objekt angeh�ngt
		// Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		Date creationDate = new Date();
		h.setCreationDate(creationDate);

		// Objekt in der DB speichern.
		return this.hashtagMapper.insertHashtag(h);
	}
	
	/**
	 * Speichern eines Hashtags.
	 * @see #saveHashtag(Hashtag hashtag)
	 */
	@Override
	public void saveHashtag(Hashtag hashtag) 
			throws IllegalArgumentException{
		
		// Objekt in der DB speichern.
		this.hashtagMapper.updateHashtag(hashtag);
	}

	/**
	 * L�schen eines Hashtag. 
	 * @see #deleteUser(User user)
	 */
	@Override
	public void deleteHashtag(Hashtag hashtag) 
			throws IllegalArgumentException {
		
		// Objekt in der DB l�schen.
		this.hashtagMapper.deleteHashtag(hashtag);
	}
	
	/**
	 * Auslesen aller Hashtags.
	 * @see #getAllHashtags()
	 */
	@Override
	public Vector<Hashtag> getAllHashtags() 
			throws IllegalArgumentException {
		
		// Objekte aus der Datenbank holen und im Vektor zur�ckgeben.
		return this.hashtagMapper.findAll();
		
	}
	
	/**
	 * Auslesen eines Hashtags anhand seiner Id.
	 * @see #getHashtagById(int id)
	 */
	@Override
	public Hashtag getHashtagById(int id) 
			throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.hashtagMapper.findByID(id);
		
	}
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden f�r Hashtag
	 * ***************************************************************************
	 */
	
	
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden f�r Abo
	 * ***************************************************************************
	 */
	/**
	 * <p>
	 * Anlegen eines neuen Abos. Dies f�hrt implizit zu einem Speichern des
	 * neuen Abos in der Datenbank.
	 * </p>
	 * 
	 * <p>
	 * <b>HINWEIS:</b> �nderungen an User-Objekten m�ssen stets durch Aufruf
	 * von {@link #save(Abo a)} in die Datenbank transferiert werden.
	 * </p>
	 * 
	 * @see createHashtagAbo(HashtagAbo hashAbo)
	 */
	
	/**
	 * Hashtag-Abo anlegen
	 */
	@Override
	public HashtagAbo createHashtagAbo(HashtagAbo hashtagAbo)
			throws IllegalArgumentException {
		
		//TODO Login einbauen und Kommentar entfernen 
		// Setzen des Users
		//b.setEditUser(logInfo.getUser());
		
		HashtagAbo hA = new HashtagAbo();
		hA.setId(hashtagAbo.getId());
		hA.setAboHashtag(hashtagAbo.getAboHashtag());
		hA.setSuscriber(hashtagAbo.getSuscriber());
		
		// Erstellungsdatum wird generiert und dem Objekt angeh�ngt
		// Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		Date creationDate = new Date();
		hA.setCreationDate(creationDate);

		// Objekt in der DB speichern.
		return this.aboMapper.insertHashtagAbo(hA);
	}
	
	/**
	 * User-Abo anlegen
	 */
	@Override
	public UserAbo createUserAbo(UserAbo userAbo)
			throws IllegalArgumentException {
		
		//TODO Login einbauen und Kommentar entfernen 
		// Setzen des Users
		//b.setEditUser(logInfo.getUser());
		
		UserAbo uA = new UserAbo();
		uA.setId(userAbo.getId());
		uA.setAboUser(userAbo.getAboUser());
		uA.setSuscriber(userAbo.getSuscriber());
		
		// Erstellungsdatum wird generiert und dem Objekt angeh�ngt
		// Das Datum wird zum Zeitpunkt des RPC Aufrufs erstellt
		Date creationDate = new Date();
		uA.setCreationDate(creationDate);

		// Objekt in der DB speichern.
		return this.aboMapper.insertUserAbo(uA);
	}
	
	/**
	 * Speichern eines Hashtag-Abos.
	 * @see #saveHashtagAbo(HashtagAbo hashtagAbo)
	 */
	@Override
	public void saveHashtagAbo(HashtagAbo hashtagAbo) 
			throws IllegalArgumentException{
		
		// Objekt in der DB speichern.
		this.aboMapper.updateHashtagAbo(hashtagAbo);
	}
	
	/**
	 * Speichern eines User-Abos.
	 * @see #saveUserAbo(UserAbo userAbo)
	 */
	@Override
	public void saveUserAbo(UserAbo userAbo) 
			throws IllegalArgumentException{
		
		// Objekt in der DB speichern.
		this.aboMapper.updateUserAbo(userAbo);
	}
	
	/**
	 * L�schen eines Hashtag Abos. 
	 * @see #deleteHashtagAbo(HashtagAbo hashtagAbo)
	 */
	@Override
	public void deleteHashtagAbo(HashtagAbo hashtagAbo) 
			throws IllegalArgumentException {
		
		// Objekt in der DB l�schen.
		this.aboMapper.deleteHashtagAbo(hashtagAbo);
	}
	/**
	 * L�schen eines User Abos. 
	 * @see #deleteUserAbo(User user)
	 */
	@Override
	public void deleteUserAbo(UserAbo userAbo) 
			throws IllegalArgumentException {
		
		// Objekt in der DB l�schen.
		this.aboMapper.deleteUserAbo(userAbo);
	}
	
	/**
	 * Auslesen eines Abos anhand eines Nutzers.
	 * @see #getAboByUser(User user)
	 */
	@Override
	public UserAbo getAboByUser(User user)
			throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.aboMapper.findAboByUser(user);
	}
	
	/**
	 * Auslesen eines Abos anhand eines Hashtags.
	 * @see #getAboByUser(User user)
	 */
	@Override
	public HashtagAbo getAboByHashtag(Hashtag hashtag)
			throws IllegalArgumentException {
		
		// Objekt aus der DB holen.
		return this.aboMapper.findAboByHashtag(hashtag);
	}

	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden f�r Abo
	 * ***************************************************************************
	 */
	
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Beginn: Methoden f�r Login
	 * ***************************************************************************
	 */
	public LoginInfo getUserInfo(String requestUri)
			throws IllegalArgumentException{
		
		UserService userService = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			de.hdm.gruppe2.shared.bo.User currentUser = new de.hdm.gruppe2.shared.bo.User();
			currentUser = userMapper.findByGoogleID(user.getUserId());
			if (currentUser == null) {
				currentUser = new de.hdm.gruppe2.shared.bo.User();
				currentUser.setGoogleId(user.getUserId());
				currentUser.setEmail(user.getEmail());
				currentUser = userMapper.insertUser(currentUser);
			}
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
			loginInfo.setUser(currentUser);

		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}
	
	 public void setLoginInfo(LoginInfo loginInfo){
		 logInfo=loginInfo;
	 }

	@Override
	public String greetServer(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nutzerAktualisieren(User n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nutzerLoeschen(User user) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * ***************************************************************************
	 * ABSCHNITT, Ende: Methoden f�r Login
	 * ***************************************************************************
	 */
	
	
}