package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterMain {

	Twitter twitter;
	Status firststatus;
	File Keyfile = new File("CunsumerKey.txt");
	Scanner scan;
	
	TwitterMain(){
		twitter = TwitterFactory.getSingleton();
		try {
			scan = new Scanner(Keyfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		twitter.setOAuthConsumer(scan.next(),scan.next());
	}
	
	public void setToken(AccessToken accessToken){
		twitter.setOAuthAccessToken(accessToken);
	}
	
	public String getScreenName() throws TwitterException{
		String ScreenName;
		ScreenName = twitter.verifyCredentials().getScreenName();
		return ScreenName;
	}
	
	public void postTweet(String str) throws Exception{
		twitter.updateStatus(str);
	}
	
	public List<Status> readTimeline() throws TwitterException{
		List<Status> statuses =  twitter.getHomeTimeline();
		return statuses;
	}
}
