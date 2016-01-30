package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.report.AllFollowersOfHashtagReport;
import de.hdm.gruppe2.shared.report.AllFollowersOfUserReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfAllUsersReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfPeriodReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfUserReport;

/**
 * <p>
 * Synchrone Schnittstelle f�r eine RPC-f�hige Klasse zur Erstellung von
 * Reports. Diese Schnittstelle benutzt die gleiche Realisierungsgrundlage wir
 * das Paar {@link MsgService} und {MsgServiceImpl}. Zu
 * technischen Erl�uterung etwa bzgl. GWT RPC bzw. {@link RemoteServiceServlet}
 * siehe {@link MsgService} und {MsgServiceImpl}.
 * </p>
 * <p>
 * Ein ReportGenerator bietet die M�glichkeit, eine Menge von Berichten
 * (Reports) zu erstellen, die Menge von Daten bzgl. bestimmter Sachverhalte des
 * Systems zweckspezifisch darstellen.
 * </p>
 * <p>
 * Die Klasse bietet eine Reihe von <code>create...</code>-Methoden, mit deren
 * Hilfe die Reports erstellt werden k�nnen. Jede dieser Methoden besitzt eine
 * dem Anwendungsfall entsprechende Parameterliste. Diese Parameter ben�tigt der
 * der Generator, um den Report erstellen zu k�nnen.
 * </p>
 * <p> 
 * Bei neu hinzukommenden Bedarfen an Berichten, kann diese Klasse auf einfache
 * Weise erweitert werden. Hierzu k�nnen zus�tzliche <code>create...</code>
 * -Methoden implementiert werden. Die bestehenden Methoden bleiben davon
 * unbeeinflusst, so dass bestehende Programmlogik nicht ver�ndert werden muss.
 * </p>
 * 
 * @author thies
 * @author Korkmaz
 */

@RemoteServiceRelativePath("reportGenerator")
public interface ReportRPC extends RemoteService {
	
	/**
	* Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von GWT
	* RPC zus�tzlich zum No Argument Constructor der implementierenden Klasse
	* {MsgServiceImpl} notwendig. Bitte diese Methode direkt nach der
	* Instantiierung aufrufen.
	* 
	* @throws IllegalArgumentException
	*/
	public void init() throws IllegalArgumentException;
  
	/**
	* Erstellen eines <code>AllMessagesOfUserReport</code>-Reports. Dieser
	* Report-Typ stellt s�mtliche Nachrichten eines Nutzers dar.
	* 
	* @param u	Eine Referenz auf das Userobjekt bzgl. dem der Report
	*         	erstellt werden soll.
	* @return 	Das fertige Reportobjekt
	* @throws 	IllegalArgumentException
	* @see 		AllMessagesOfUserReport
	*/
	public abstract AllMessagesOfUserReport createAllMessagesOfUserReport(
			String userMail) throws IllegalArgumentException;
	
	/**
	* Erstellen eines <code>AllMessagesOfAllUsersReport</code>-Reports. Dieser
	* Report-Typ stellt s�mtliche Nachrichten aller Nutzer dar.
	* 
	* @return	Das fertige Reportobjekt
	* @throws	IllegalArgumentException
	* @see 		AllMessagesOfAllUsersReport
	*/
	public abstract AllMessagesOfAllUsersReport createAllMessagesOfAllUsersReport() 
			throws IllegalArgumentException;
	
	/**
	* Erstellen eines <code>AllMessagesOfPeriodReport</code>-Reports. Dieser
	* Report-Typ stellt s�mtliche Nachrichten eines spezifischen Zeitraums dar.
	* 
	* @param start	Das Startdatum als String in Form (yyyy-MM-dd hh:mm:ss)
	* @param end	Das Enddatum als String in Form (yyyy-MM-dd hh:mm:ss)
	*         
	* @return	Das fertige Reportobjekt
	* @throws	IllegalArgumentException
	* @see		AllAccountsOfCustomerReport
	*/
	public abstract AllMessagesOfPeriodReport createAllMessagesOfPeriodReport(
			String start, String end) throws IllegalArgumentException;
	
	/**
	* Erstellen eines <code>AllFollowersOfHashtagReport</code>-Reports. Dieser
	* Report-Typ stellt s�mtliche Konten eines Kunden dar.
	* 
	* @param h	Eine Referenz auf das Hashtagobjekt bzgl. dem der Report 
	* 			erstellt werden soll
	* @return	Das fertige Reportobjekt
	* @throws	IllegalArgumentException
	* @see		AllFollowersOfHashtagReport
	*/
	public abstract AllFollowersOfHashtagReport createAllFollowersOfHashtagReport(
			Hashtag h) throws IllegalArgumentException;
	
	/**
	* Erstellen eines <code>AllFollowersOfUserReport</code>-Reports. Dieser
	* Report-Typ stellt s�mtliche Konten eines Kunden dar.
	* 
	* @param u 	Eine Referenz auf das Userobjekt bzgl. dessen der Report
	*          	erstellt werden soll.
	* @return	Das fertige Reportobjekt
	* @throws	IllegalArgumentException
	* @see 		AllFollowersOfUserReport
	*/
	public abstract AllFollowersOfUserReport createAllFollowersOfUserReport(
			User u) throws IllegalArgumentException;
	
	/**
	* Erstellen einer <code>ArrayList</code> die s�mtliche der Datenbank bekannte
	* Hashtags enth�lt.
	* 
	* @return	Eine ArrayList mit allen Hashtagobjekten der Datenbank
	* @throws	IllegalArgumentException
	*/
	public ArrayList<Hashtag> findAllHashtags() throws IllegalArgumentException;
	
	/**
	* Erstellen einer <code>ArrayList</code> die s�mtliche der Datenbank bekannte
	* Nutzer enth�lt.
	* 
	* @return	Eine ArrayList mit allen Nutzerobjekten der Datenbank
	* @throws	IllegalArgumentException
	*/
	public ArrayList<User> findAllUsers() throws IllegalArgumentException;
}
