package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

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
	ObjectProperty<Status> timeline_status = new SimpleObjectProperty<Status>();
	ObjectProperty<Status> mention_status = new SimpleObjectProperty<Status>();
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
		
	class MyUserStreamAdapter extends UserStreamAdapter{
		public void onStatus(final Status status){
			Platform.runLater(new Runnable(){
				@Override
				public void run(){
					if(status.getText().contains(ScreenName)){
						mention_status.set(status);
					}
					timeline_status.set(status);
				}
			});
		}
		public void onFavorite(User source, User target,Status favoritedStatus){
			System.out.println(source.getScreenName() + "‚ª" + target.getScreenName() + "‚Ì" + favoritedStatus.getText() + "‚ð‚¨‹C‚É“ü‚è‚É“o˜^‚µ‚Ü‚µ‚½");	
		}
	}
}
