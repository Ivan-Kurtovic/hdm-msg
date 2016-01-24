package de.hdm.gruppe2.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.shared.MsgServiceAsync;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

public class NewPostOverview extends VerticalPanel {
	
	private MsgServiceAsync msgSvc = ClientsideSettings.getMsgService();
	private User loggedInUser = null;
	
	private final FlexTable ftPosts = new FlexTable();
	private final TextArea taPost = new TextArea();
	private final Button btnPost = new Button("Posten!");
	
	public NewPostOverview(User user) {
		this.loggedInUser = user;
	}
	
	public void onLoad() {
		
		this.getAllPosts(ftPosts, loggedInUser.getId());
		
		final Grid mainGrid = new Grid(3,1);
		btnPost.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createPost(taPost.getText(), loggedInUser, null);
			}
			
		});
		
		mainGrid.setWidget(0, 0, ftPosts);
		mainGrid.setWidget(1, 0, taPost);
		mainGrid.setWidget(2, 0, btnPost);
		
		this.add(mainGrid);
	}
	
	private void createPost(String text, User currentUser, ArrayList<Hashtag> hashtagList) {
		msgSvc.createPost(text, currentUser, hashtagList, new AsyncCallback<Message> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Post konnte nicht angelegt werden.");
			}

			@Override
			public void onSuccess(Message result) {
				taPost.setText("");
				
				ClientsideSettings.getLogger().finest("Post wurde angelegt.");				
			}
			
		});		
	}
	
	private void getAllPosts(FlexTable flexTable, int userId) {
		msgSvc.findAllPostsOfUser(userId, new AsyncCallback<ArrayList<Message>> () {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Posts konnten nicht geladen werden.");
			}

			@Override
			public void onSuccess(ArrayList<Message> result) {
				ftPosts.clear();
				
				for(Message m : result) {
					final int numrows = ftPosts.getRowCount();
					final Message message = m;
					
					ftPosts.setText(numrows + 1, 0, Integer.toString(m.getUserId()));
					ftPosts.setText(numrows + 1, 1, m.getText());
					ftPosts.setText(numrows + 1, 2, m.getCreationDate().toString());
					
					Button btnEdit = new Button("Edit");
					btnEdit.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							Window.alert("Edit: " + message.getId());
						}
						
					});
					Button btnRemove = new Button("Remove Post");
					btnRemove.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							deletePost(message.getId(), numrows + 1);
						}
						
					});
					
					ftPosts.setWidget(numrows + 1, 3, btnEdit);
					ftPosts.setWidget(numrows + 1, 4, btnRemove);
				}
				
				ClientsideSettings.getLogger().finest("Posts erfolgreich geladen.");
			}

		});
	}
	
	private void deletePost(int postId, final int flexTableRow) {

		msgSvc.deleteMessage(postId, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe("Zeile erfolgreich entfernt.");
			}

			@Override
			public void onSuccess(Void result) {
				ftPosts.removeRow(flexTableRow);
				
				ClientsideSettings.getLogger().finest("Zeile erfolgreich entfernt.");
			}
			
		});
	}

}
