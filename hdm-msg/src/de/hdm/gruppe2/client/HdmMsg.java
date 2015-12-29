package de.hdm.gruppe2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.client.gui.AboView;
import de.hdm.gruppe2.client.gui.ChatView;
import de.hdm.gruppe2.client.gui.HashtagView;
import de.hdm.gruppe2.client.gui.Impressum;
import de.hdm.gruppe2.client.gui.Login;
import de.hdm.gruppe2.client.gui.MessageView;
import de.hdm.gruppe2.client.gui.Registration;




/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HdmMsg extends VerticalPanel implements EntryPoint {
	
	public void onModuleLoad() {
		
		
		
			HTML welcomeText = new HTML("<h1>Wilkommen im Messenger Editor!</h1>");
		
			
			
			//Neu: MenuBar mit Commands (~ClickHandler)
			Command messageview = new Command() {
			      public void execute() {
				        RootPanel.get("content_wrap").clear();
				        RootPanel.get("content_wrap").add(new MessageView());
			      }
			};
			
			Command chatview = new Command() {
			      public void execute() {
			        RootPanel.get("content_wrap").clear();
			        RootPanel.get("content_wrap").add(new ChatView());
			      }
			};
			
			Command hashtagview = new Command() {
			      public void execute() {
				        RootPanel.get("content_wrap").clear();
				        RootPanel.get("content_wrap").add(new HashtagView());
			      }
			};
			
			Command aboview = new Command() {
			      public void execute() {
			        RootPanel.get("content_wrap").clear();
			        RootPanel.get("content_wrap").add(new AboView());
			      }
			};
			
			
			
			Command loginTest = new Command() {
			      public void execute() {
			    	  RootPanel.get("content_wrap").clear();
			    	  RootPanel.get("content_wrap").add(new Login());
			      }
			};
			
			Command loginRegistration = new Command() {
			      public void execute() {
			    	  RootPanel.get("content_wrap").clear();
			    	  RootPanel.get("content_wrap").add(new Registration());
			      }
			};
			
			
			
			
			
			
			
			//Neu: MenuBar mit Mouse-Over Untermen�s
			
			//Das Men� von Nachrichten erh�lt folgende Mouse-Over Untermen�s
		    MenuBar messageMenue = new MenuBar(true);
		    messageMenue.addItem(" Nachrichten Anzeigen",messageview);
		    messageMenue.addItem(" Chats ", chatview);

		    //Das Men� von Chats erh�lt folgende Mouse-Over Untermen�s
		    MenuBar chatMenu = new MenuBar(true);
		    chatMenu.addItem(" Chats Anzeigen ", chatview);
		    chatMenu.addItem(" Nachrichten ",messageview );

		    //Das Men� von Hashtags erh�lt folgende Mouse-Over Untermen�s
		    MenuBar hashtagMenu = new MenuBar(true);
		    hashtagMenu.addItem(" Hashtags Anzeigen ", hashtagview);
		    hashtagMenu.addItem(" Nachrichten ", messageview);
		    
		    //Das Men� von Abos erh�lt folgende Mouse-Over Untermen�s
		    MenuBar aboMenu = new MenuBar(true);
		    aboMenu.addItem(" Abos Anzeigen ", aboview);
		    aboMenu.addItem("Nachrichten", messageview);
		    

		    // TODO Login am Ende Auskommentierung entfernen
		    //Testweise Men� f�r Login-GUI
		 //   MenuBar loginMenu = new MenuBar(true);
		 //   loginMenu.addItem("Login", loginTest);
		 //   loginMenu.addItem("Registrieren", loginRegistration);

		    //Alle Untermen�s werden hier dem Hauptmen� zugeordnet
		    MenuBar mainMenu = new MenuBar();
		    mainMenu.setWidth("100%");
		    mainMenu.setAutoOpen(true);
		    mainMenu.addItem(" Nachrichten ", messageMenue);
		    mainMenu.addItem(" Chats ", chatMenu);
		    mainMenu.addItem(" Hashtags ", hashtagMenu);
		    mainMenu.addItem(" Abos ", aboMenu);
	     // mainMenu.addItem("Login", loginMenu);
		    
		    //Der Default-Text, der beim Aufruf der Applikation angezeigt wird
		    
		    //Das Begr��ungsbild der Applikation
			
			    
		    //Hautpmen� schlie�lich dem RootPanel in den Men�-div Container zuordnen
		    RootPanel.get("head_wrap_right").add(mainMenu);
			RootPanel.get("Impressum").add(new Impressum());

		
}
	
}
