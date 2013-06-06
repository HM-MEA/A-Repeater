package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
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
	
	public String getScreenName() throws Exception{
		String ScreenName;
		ScreenName = twitter.verifyCredentials().getScreenName();
		return ScreenName;
	}
	
	public void postTweet(String str) throws TwitterException{
		twitter.updateStatus(str);
	}
	
	public void postTweet(String str,long reply_id) throws TwitterException{
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(reply_id);
		twitter.updateStatus(update);
	}
	
	public void retweet(Status status) throws TwitterException{
		twitter.retweetStatus(status.getId());
	}
	
	public void favorite(Status status) throws TwitterException{
		twitter.createFavorite(status.getId());
	}
	
	public List<Status> getTimeline(int page,long sinceid) throws TwitterException{
		List<Status> statuses =  twitter.getHomeTimeline(new Paging(1).sinceId(sinceid));
		return statuses;
	}
	
	public List<Status> getMentions(int page,long sinceid) throws TwitterException{
		List<Status> statuses = twitter.getMentionsTimeline(new Paging(1).sinceId(sinceid));
		return statuses;
	}
}
