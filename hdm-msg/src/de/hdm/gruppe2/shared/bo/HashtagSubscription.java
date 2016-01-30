package de.hdm.gruppe2.shared.bo;

/**
 * Realisierung der HashtagSubscription-Klasse. Sie enth�lt alle notwendigen Informationen
 * der Hashtag Abonnements dieser Anwendung. Darunter f�llt die ID des Abonennten und des
 * Hashtags. Die ID des Abonnenten wird als Parameter der Subscription Klasse geerbt.
 * 
 * @author thies
 * @author Sari
 * @author Yilmaz
 * @version 1.0
 */
public class HashtagSubscription extends Subscription {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Fremdschl�ssel des Hashtags
	 */
	private int hashtagId;
	
	/**
	 * Auslesen des Fremdschl�ssels des Hashtags
	 */
	public int getHashtagId(){
		return hashtagId;
	}
	
	/**
	 * Setzen des Fremdschl�ssels des Hashtags
	 */
	public void setHashtagId(int hashtagId){
		this.hashtagId = hashtagId;
	}
}
