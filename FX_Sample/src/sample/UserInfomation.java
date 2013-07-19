package sample;

import java.util.List;

import javafx.scene.image.Image;

public class UserInfomation {

	boolean isFollow;
	String ScreenName;
	List<Exstatus> Tweet;
	List<Exstatus> Favorite;
	String UserIntro;
	Image Icon;
	
	public void setIsFollow(boolean b){
		isFollow = b;
	}
	public void setScreenName(String s){
		ScreenName = s;
	}
	public void setTweet(List<Exstatus> e){
		Tweet = e;
	}
	public void setFavorite(List<Exstatus> e){
		Favorite = e;
	}
	public void setUserInfo(String str){
		UserIntro = str;
	}
	public void setIcon(Image i){
		Icon = i;
	}
	
	public boolean getIsFollow(){
		return isFollow;
	}
	public String getScreenName(){
		return ScreenName;
	}
	public List<Exstatus> getTweet(){
		return Tweet;
	}
	public List<Exstatus> getFavorite(){
		return Favorite;
	}
	public String getUserInfo(){
		return UserIntro;
	}
	public Image getIcon(){
		return Icon;
	}
}
