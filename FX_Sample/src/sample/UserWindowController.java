package sample;

import java.util.Collections;
import java.util.List;

import twitter4j.Status;
import twitter4j.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserWindowController {
	
	TwitterMain Twmain;
	User user;

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
	
	@FXML
	private Button followButton;

	public void setUserInfo(TwitterMain tw,User us){
		user = us;
		Twmain = tw;
		
		ScreenName.setText(StringController.createScreenNameString(user));
		UserIntroduction.setText(user.getDescription());
		Icon.setImage(new Image(user.getProfileImageURL()));		
		try {
			setTweet(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setTweet(User user) throws Exception{
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
		if(Twmain.isFollowed(user.getId())){
			followButton.setText("アンフォロー");
		}else{
			followButton.setText("フォロー");
		}
	}
	
	@FXML
	private void followc(){
		try{
			if(followButton.getText().equals("フォロー")){
				Twmain.follow(user.getId());
				followButton.setText("アンフォロー");
			}else{
				Twmain.unfollow(user.getId());
				followButton.setText("フォロー");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
