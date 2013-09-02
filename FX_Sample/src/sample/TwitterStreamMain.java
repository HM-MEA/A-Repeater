package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;

public class TwitterStreamMain {
	
	static TwitterStream twitterstream;
	File Keyfile = new File("CunsumerKey.txt");
	Scanner scan;
	String ScreenName;
	ObjectProperty<Exstatus> timeline_status = new SimpleObjectProperty<Exstatus>();
	ObjectProperty<Exstatus> mention_status = new SimpleObjectProperty<Exstatus>();
	ObjectProperty<Exstatus> dmessage = new SimpleObjectProperty<Exstatus>();
	ObjectProperty<FavoritedStatus> favoritedStatusProperty = new SimpleObjectProperty<FavoritedStatus>();
	IntegerProperty stream_f = new SimpleIntegerProperty();

	TwitterStreamMain(){
		twitterstream = new TwitterStreamFactory().getInstance();
		try {
			scan = new Scanner(Keyfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		twitterstream.setOAuthConsumer(scan.next(),scan.next());
		twitterstream.addListener(new MyUserStreamAdapter());
	}
	
	public void setToken(AccessToken accessToken){
		twitterstream.setOAuthAccessToken(accessToken);
	}
	
	public void startUserStream() throws Exception{
		twitterstream.user();
		ScreenName = "@" + twitterstream.getScreenName();
		stream_f.set(1);
	}
	
	public void stopUserStream(){
		twitterstream.shutdown();
		stream_f.set(0);
	}
	
	public void stopStream(){
		twitterstream.shutdown();
	}
	
	public void setTimeline(Status status){
		Exstatus e = new Exstatus();
		e.setStatus(status);
		if(status.isRetweet()){
			e.setImage(new Image(status.getRetweetedStatus().getUser().getMiniProfileImageURL()));

		}else{
			e.setImage(new Image(status.getUser().getMiniProfileImageURL()));
		}
		timeline_status.set(e);
	}
	
	public void setMention(Status status){
		Exstatus e = new Exstatus();
		e.setStatus(status);
		e.setImage(new Image(status.getUser().getMiniProfileImageURL()));
		mention_status.set(e);
	}
	
	public void setDMessage(DirectMessage dm){
		Exstatus e = new Exstatus();
		e.setDMessage(dm);
		e.setImage(new Image(dm.getSender().getMiniProfileImageURL()));
		dmessage.set(e);
	}
		
	class MyUserStreamAdapter extends UserStreamAdapter{
		public void onStatus(final Status status){
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					if(status.getText().contains(ScreenName)){
						setMention(status);
					}
					setTimeline(status);
				}
			});	
		}
		public void onFavorite(final User source, final User target,final Status favoritedStatus){
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					System.out.println(source.getScreenName() + "‚ª" + target.getScreenName() + "‚Ì" + favoritedStatus.getText() + "‚ð‚¨‹C‚É“ü‚è‚É“o˜^‚µ‚Ü‚µ‚½");
					FavoritedStatus favoritedstatus = new FavoritedStatus(source,target,favoritedStatus);
					favoritedStatusProperty.set(favoritedstatus);
				}
			});
		}
		public void onDirectMessage(final DirectMessage dm){
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					setDMessage(dm);
				}
			});
		}
	}
}
