package sample;

import twitter4j.TwitterException;
import twitter4j.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class DirectMessageWindowController {

	@FXML
	private Label senduser;
	
	@FXML
	private TextArea text;
	
	@FXML
	private Button sendbutton;
	
	TwitterMain Twmain;
	User user;
	
	AnchorPane pane;
	
	public void setData(TwitterMain tw,User u){
		Twmain = tw;
		user = u;
		
		senduser.setText("Send DirectMessage to @" + user.getScreenName());
	}
	
	@FXML
	private void onSendDMessage() throws TwitterException{
		String str = "m " + user.getScreenName() + " " + text.getText();
		Twmain.postTweet(str);
		text.setText("");
	}

}
