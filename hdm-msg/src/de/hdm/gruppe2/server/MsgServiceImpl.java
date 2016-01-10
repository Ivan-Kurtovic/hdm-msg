package de.hdm.gruppe2.server;

import de.hdm.gruppe2.server.db.UserMapper;
import de.hdm.gruppe2.shared.FieldVerifier;
import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.bo.User;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MsgServiceImpl extends RemoteServiceServlet implements MsgService {

	private UserMapper usermapper = UserMapper.usermapper();
	
	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public User createUser(String email, String firstName, String lastName) {
		
		User u = new User();
		u.setEmail(email);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		
		/*
	     * Setzen einer vorl�ufigen User-Id Der insert-Aufruf liefert dann ein
	     * Objekt, dessen Nummer mit der Datenbank konsistent ist.
	     */
		u.setId(1);

		return this.usermapper.insert(u);
	}

	@Override
	public User editUser(User user) {
		return this.usermapper.update(user);
	}

	@Override
	public ArrayList<User> findAllUser() {
		return this.usermapper.findAllUsers();
	}
}
