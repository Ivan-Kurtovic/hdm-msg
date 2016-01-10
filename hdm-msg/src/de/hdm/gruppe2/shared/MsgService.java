package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("msgServlet")
public interface MsgService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	
	public User createUser(String email, String firstName, String lastName);
	
	public User editUser(User user);
	
	public ArrayList<User> findAllUser();
}
