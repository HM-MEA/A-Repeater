package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

import twitter4j.DirectMessage;
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
	
	ObjectProperty<List<Status>> TimelineStatuses = new SimpleObjectProperty<List<Status>>();
	ObjectProperty<List<Status>> MentionStatuses = new SimpleObjectProperty<List<Status>>();
	ObjectProperty<List<DirectMessage>> DMessages = new SimpleObjectProperty<List<DirectMessage>>();
	
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
	
	public void postTweet(final String str) throws TwitterException{
		Task<Status> task = new Task<Status>(){
			@Override
			protected Status call() throws Exception {
				return twitter.updateStatus(str);			
			}
		};
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void postTweet(final String str,final long reply_id) throws TwitterException{
		Task<Status> task = new Task<Status>(){
			@Override
			protected Status call() throws Exception {
				StatusUpdate update = new StatusUpdate(str);
				update.setInReplyToStatusId(reply_id);
				return twitter.updateStatus(update);			
			}
		};
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void retweet(final Status status) throws TwitterException{
		Task<Status> task = new Task<Status>(){
			@Override
			protected Status call() throws Exception {
				return 	twitter.retweetStatus(status.getId());
			}
		};
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void favorite(final Status status) throws TwitterException{
		Task<Status> task = new Task<Status>(){
			@Override
			protected Status call() throws Exception {
				return 	twitter.createFavorite(status.getId());
			}
		};
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public List<Status> getUserTweet(long userid) throws TwitterException{
		List<Status> statuses =  twitter.getUserTimeline(userid);
		return statuses;
	}
	
	public List<Status> getUserFavorite(long userid) throws TwitterException{
		List<Status> statuses =  twitter.getFavorites(userid);
		return statuses;
	}
			
	public void getTimeline(int page,final long sinceid) throws TwitterException{
		Task<List<Status>> task = new Task<List<Status>>(){
			@Override
			protected List<Status> call() throws Exception {
				return twitter.getHomeTimeline(new Paging(1).sinceId(sinceid));
			}
			@Override
			protected void succeeded(){
				TimelineStatuses.set(getValue());
			}
		};
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void getMentions(int page,final long sinceid) throws TwitterException{
		Task<List<Status>> task = new Task<List<Status>>(){
			@Override
			protected List<Status> call() throws Exception {
				return twitter.getMentionsTimeline(new Paging(1).sinceId(sinceid));
			}
			@Override
			protected void succeeded(){
				MentionStatuses.set(getValue());
			}
		};
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public void getDMs(int page,final long sinceid) throws TwitterException{
		Task<List<DirectMessage>> task = new Task<List<DirectMessage>>(){
			@Override
			protected List<DirectMessage> call() throws Exception {
				return twitter.getDirectMessages(new Paging(1).sinceId(sinceid));
			}
			@Override
			protected void succeeded(){
				DMessages.set(getValue());
			}
		};
		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
	
	public Status getStatus(long Id) throws TwitterException{
		return twitter.showStatus(Id);
	}
	
	public boolean isFollowed(long id) throws Exception{
		if(twitter.showFriendship(twitter.getId(), id).isTargetFollowedBySource()){
			return true;
		}else{
			return false;
		}
	}
	
	public void follow(long id) throws TwitterException{
		twitter.createFriendship(id);
	}
	public void unfollow(long id) throws TwitterException{
		twitter.destroyFriendship(id);
	}
}
