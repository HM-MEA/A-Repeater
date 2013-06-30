package sample;

import java.util.Collections;
import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserWindowController {
	
	TwitterMain Twmain;

	@FXML
	private Label ScreenName;
	
	@FXML
	private Label UserIntroduction;
	
	@FXML
	private ImageView Icon;
	
	@FXML
	private ListView<Label> UserTweet;
	
	@FXML
	private ListView<Label> UserFavorite;

	
	public void setUserInfo(TwitterMain tw,User user){
		if(user.isProtected()){
			ScreenName.setText("@" + user.getScreenName() + " 🔒");
		}else{
			ScreenName.setText("@" + user.getScreenName());
		}
		UserIntroduction.setText(user.getDescription());
		Icon.setImage(new Image(user.getProfileImageURL()));	
		Twmain = tw;
		try {
			setTweet(user);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public void setTweet(User user) throws TwitterException{
		List<Status> UserTweetStatuses = Twmain.getUserTweet(user.getId());
		List<Status> UserFavoriteStatuses = Twmain.getUserFavorite(user.getId());
		Collections.reverse(UserTweetStatuses);
		Collections.reverse(UserFavoriteStatuses);
		for (Status status : UserTweetStatuses) {
			Label label = new Label();
			label.setUserData(status);
			label.setText(StringController.createTweetString(status));
			UserTweet.getItems().add(0,label);
		}
		for(Status status : UserFavoriteStatuses){
			Label label = new Label();
			label.setUserData(status);
			label.setText(StringController.createTweetString(status));
			UserFavorite.getItems().add(0,label);
		}
	}
}
