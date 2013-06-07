package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Platform;

import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;

public class TwitterStreamMain {
	
	static TwitterStream twitterstream;
	File Keyfile = new File("CunsumerKey.txt");
	Scanner scan;
	String ScreenName;

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
	
	public void startUserStream(){
		twitterstream.user();
		ScreenName = "@" + MainStageController.ScreenName;
	}
	
	public static void stopUserStream(){
		twitterstream.shutdown();
	}
	
	public void stopStream(){
		twitterstream.shutdown();
	}
	
	protected void setStreamStatus(Status status){
		MainStageController.setTimeline(status);
	}
	
	protected void setStreamMention(Status status){
		MainStageController.setMention(status);
	}
	
	class MyUserStreamAdapter extends UserStreamAdapter{
		public void onStatus(final Status status){
			Platform.runLater(new Runnable(){
				@Override
				public void run(){
					if(status.getText().contains(ScreenName)){
						setStreamMention(status);
					}
					setStreamStatus(status);
				}
			});
		}
	}
}
