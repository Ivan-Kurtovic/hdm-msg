package de.hdm.gruppe2.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Synchrone Schnittstelle f�r die Klasse LoginServiceImpl
 * 
 * @author Sari
 *
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	
	/**
	 * Ausf�hren des Logins und ablegen aller relevanten Nutzer Informationen
	 * in einem LoginInfo Objekt
	 * 
	 * @param requestUri Die Basis URL der aufrufenden Seite 
	 * 					 (ermittelt �ber GWT.getHostPageBaseURL())
	 * @return
	 */
	public LoginInfo login(String requestUri);
	
}
