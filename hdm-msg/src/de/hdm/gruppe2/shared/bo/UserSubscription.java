package de.hdm.gruppe2.shared.bo;

/**
 * Realisierung der UserSubscription-Klasse. Sie enth�lt alle notwendigen Informationen
 * der Nutzer Abonnements dieser Anwendung. Darunter f�llt die ID des Abonennten und des
 * abonnierten Nutzers. Die ID des Abonnenten wird als Parameter der Subscription Klasse geerbt.
 * 
 * @author thies
 * @author Sari
 * @author Yilmaz
 * @version 1.0
 */
public class UserSubscription extends Subscription {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Fremdschl�ssel des abonnierten Nutzers
	 */
	private int senderId;	
	
	/**
	 * Auslesen des Fremdschl�ssels des abonnierten Nutzers
	 */	
	public int getSenderId(){
		return senderId;
	}

	/**
	 * Setzen des Fremdschl�ssels des abonnierten Nutzers
	 */	
	public void setSenderId(int userId){
		this.senderId = userId;
	}
}
