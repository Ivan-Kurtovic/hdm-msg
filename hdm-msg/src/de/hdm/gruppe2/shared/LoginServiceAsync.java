package de.hdm.gruppe2.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Das asynchrone Gegenst�ck des Interface {@link LoginService}. Es wird
 * semiautomatisch durch das Google Plugin erstellt und gepflegt. Daher erfolgt
 * hier keine weitere Dokumentation. F�r weitere Informationen siehe das
 * synchrone Interface {@link LoginService}.
 * 
 * @author thies
 */
public interface LoginServiceAsync {
	
	void login(String requestUri, AsyncCallback<LoginInfo> callback);
	
}
