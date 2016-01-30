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
public class Subscription extends BusinessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	/**
	 * Die Fremdschl�ssel ID des Rezipienten
	 */
	private int recipientId; 
	
	/**
	 * Auslesen des Fremdschl�ssels des Rezipienten
	 */
	public int getRecipientId(){
		return recipientId;	
	}
	
	/**
	 * Setzen des Fremdschl�ssels des Rezipienten
	 */
	public void setRecipientId(int recipientId){
		this.recipientId = recipientId;
	}
}
