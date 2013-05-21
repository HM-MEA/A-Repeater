package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.control.Label;

import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamAdapter;
import twitter4j.auth.AccessToken;

public class TwitterStreamMain {
	
	static TwitterStream twitterstream;
	File Keyfile = new File("CunsumerKey.txt");
	Scanner scan;

	TwitterStreamMain(){
		twitterstream = new TwitterStreamFactory().getInstance();
		try {
			scan = new Scanner(Keyfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		twitterstream.setOAuthConsumer(scan.next(),scan.next());
	}
	
	public void setToken(AccessToken accessToken){
		twitterstream.setOAuthAccessToken(accessToken);
	}
	
	public void startUserStream(){
		twitterstream.addListener(new MyUserStreamAdapter());
		twitterstream.user();
	}
	
	public static void stopUserStream(){
		twitterstream.shutdown();
	}
	
	protected void setStreamStatus(Status status){
		MainStageController.StatusList.add(0,status);
		
		Label label = new Label();
		label.setText(status.getUser().getScreenName() + ":" +  status.getText() + status.getCreatedAt());
		MainStageController.listview.getItems().add(0, label);
	}
	
	class MyUserStreamAdapter extends UserStreamAdapter{
		public void onStatus(Status status){
			setStreamStatus(status);
		}
	}
	
	
}
