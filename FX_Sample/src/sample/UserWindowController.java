package sample;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import twitter4j.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class UserWindowController implements Initializable{
	
	TwitterMain Twmain;
	User user;
	List<Exstatus> UserTweetStatuses;
	List<Exstatus> UserFavoriteStatuses;

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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	public void setUserInfo(TwitterMain tw,User us){
		user = us;
		Twmain = tw;
		Twmain.UserInfo.addListener(new ChangeListener<UserInfomation>(){
			@Override
			public void changed(ObservableValue<? extends UserInfomation> arg0,UserInfomation arg1, UserInfomation arg2) {
					setInfo(arg2);
			}
		});
		tw.getUserInfo(us);
	}
	
	public void setInfo(UserInfomation ui){
		UserTweetStatuses = ui.getTweet();
		UserFavoriteStatuses = ui.getFavorite();
		Collections.reverse(UserTweetStatuses);
		Collections.reverse(UserFavoriteStatuses);
		for (Exstatus status : UserTweetStatuses) {
			Label label = new Label();
			label.setUserData(status.getStatus());
			label.setGraphic(new ImageView(status.getImage()));
			label.setText(StringController.createTweetString(status.getStatus()));
			UserTweet.getItems().add(0,label);
		}
		for(Exstatus status : UserFavoriteStatuses){
			Label label = new Label();
			label.setUserData(status.getStatus());
			label.setText(StringController.createTweetString(status.getStatus()));
			label.setGraphic(new ImageView(status.getImage()));
			UserFavorite.getItems().add(0,label);
		}
		if(ui.isFollow){
			followButton.setText("アンフォロー");
		}else{
			followButton.setText("フォロー");
		}
		Icon.setImage(ui.getIcon());
		UserIntroduction.setText(ui.getUserInfo());
		ScreenName.setText(ui.getScreenName());
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
