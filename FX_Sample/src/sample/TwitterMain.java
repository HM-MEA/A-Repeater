package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

public class TwitterMain{

	Twitter twitter;
	Status firststatus;
	File Keyfile = new File("CunsumerKey.txt");
	Scanner scan;
	
	ObjectProperty<Exstatus> TimelineStatuses = new SimpleObjectProperty<Exstatus>();
	ObjectProperty<Exstatus> MentionStatuses = new SimpleObjectProperty<Exstatus>();
	ObjectProperty<Exstatus> DMessages = new SimpleObjectProperty<Exstatus>();
	ObjectProperty<Exstatus> ChatStatuses = new SimpleObjectProperty<Exstatus>();
	ObjectProperty<UserInfomation> UserInfo = new SimpleObjectProperty<UserInfomation>();
	
	IntegerProperty indicator = new SimpleIntegerProperty();
	
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
		ScreenName = twitter.getScreenName();
		return ScreenName;
	}
	
	public void postTweet(final String str) throws TwitterException{
		Service<Status> s = new Service<Status>(){
			@Override
			protected Task<Status> createTask(){
				Task<Status> task = new Task<Status>() {
		            @Override
		            protected Status call() throws Exception {
		            	return twitter.updateStatus(str); 
		            }
		            @Override
		            protected void succeeded(){
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void postTweet(final String str,final long reply_id) throws TwitterException{
		Service<Status> s = new Service<Status>(){
			@Override
			protected Task<Status> createTask(){
				Task<Status> task = new Task<Status>() {
		            @Override
		            protected Status call() throws Exception {
		            	StatusUpdate update = new StatusUpdate(str);
						update.setInReplyToStatusId(reply_id);
						return twitter.updateStatus(update);	
		            }
		            @Override
		            protected void succeeded(){
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void postTweet(final String str,final File f){
		Service<Status> s = new Service<Status>(){
			@Override
			protected Task<Status> createTask(){
				Task<Status> task = new Task<Status>() {
		            @Override
		            protected Status call() throws Exception {
		            	StatusUpdate update = new StatusUpdate(str);
						update.setMedia(f);
						return twitter.updateStatus(update);	
		            }
		            @Override
		            protected void succeeded(){
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void postTweet(final String str,final long reply_id,final File f){
		Service<Status> s = new Service<Status>(){
			@Override
			protected Task<Status> createTask(){
				Task<Status> task = new Task<Status>() {
		            @Override
		            protected Status call() throws Exception {
		            	StatusUpdate update = new StatusUpdate(str);
						update.setMedia(f);
						update.setInReplyToStatusId(reply_id);
						return twitter.updateStatus(update);	
		            }
		            @Override
		            protected void succeeded(){
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void retweet(final Status status) throws TwitterException{
		Service<Status> s = new Service<Status>(){
			@Override
			protected Task<Status> createTask(){
				Task<Status> task = new Task<Status>() {
		            @Override
		            protected Status call() throws Exception {
		            	return twitter.retweetStatus(status.getId());
		            }
		            @Override
		            protected void succeeded(){
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void favorite(final Status status) throws TwitterException{
		Service<Status> s = new Service<Status>(){
			@Override
			protected Task<Status> createTask(){
				Task<Status> task = new Task<Status>() {
		            @Override
		            protected Status call() throws Exception {
		            	return twitter.createFavorite(status.getId());
		            }
		            @Override
		            protected void succeeded(){
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
		
	
	
	public void getTimeline(final int page,final long sinceid) throws TwitterException{
		Service<List<Exstatus>> s = new Service<List<Exstatus>>(){
			@Override
			protected Task<List<Exstatus>> createTask(){
				Task<List<Exstatus>> task = new Task<List<Exstatus>>() {
		            @Override
		            protected List<Exstatus> call() throws Exception {
		            	List<Exstatus> statuses = new ArrayList<Exstatus>();
		            	List<Status> list = twitter.getHomeTimeline(new Paging(page).sinceId(sinceid));
		            	for(Status status:list){
		            		Exstatus e = new Exstatus();
		            		e.setStatus(status);
		            		if(status.isRetweet()){
		            			e.setImage(new Image(status.getRetweetedStatus().getUser().getMiniProfileImageURL()));

		            		}else{
		            			e.setImage(new Image(status.getUser().getMiniProfileImageURL()));
		            		}		  
		            		statuses.add(e);
		            	}
		            	return statuses;
		            }
		            @Override
		            protected void succeeded(){
		            	List<Exstatus> statuses = getValue();
		            	Collections.reverse(statuses);
		            	for(int i = 0;i < statuses.size();i++){
		            		TimelineStatuses.set(statuses.get(i));
		            	}
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void getMentions(final int page,final long sinceid) throws TwitterException{
		Service<List<Exstatus>> s = new Service<List<Exstatus>>(){
			@Override
			protected Task<List<Exstatus>> createTask(){
				Task<List<Exstatus>> task = new Task<List<Exstatus>>() {
		            @Override
		            protected List<Exstatus> call() throws Exception {
		            	List<Exstatus> statuses = new ArrayList<Exstatus>();
		            	List<Status> list = twitter.getMentionsTimeline(new Paging(page).sinceId(sinceid));
		            	for(Status status:list){
		            		Exstatus e = new Exstatus();
		            		e.setStatus(status);
		            		e.setImage(new Image(status.getUser().getMiniProfileImageURL()));
		            		statuses.add(e);
		            	}
		            	return statuses;
		            }
		            @Override
		            protected void succeeded(){
		            	List<Exstatus> statuses = getValue();
		            	Collections.reverse(statuses);
		            	for(int i = 0;i < statuses.size();i++){
		            		MentionStatuses.set(statuses.get(i));
		            	}
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void getDMs(final int page,final long sinceid) throws TwitterException{
		Service<List<Exstatus>> s = new Service<List<Exstatus>>(){
			@Override
			protected Task<List<Exstatus>> createTask(){
				Task<List<Exstatus>> task = new Task<List<Exstatus>>() {
		            @Override
		            protected List<Exstatus> call() throws Exception {
		            	List<Exstatus> statuses = new ArrayList<Exstatus>();
		            	List<DirectMessage> list = twitter.getDirectMessages(new Paging(page).sinceId(sinceid));
		            	for(DirectMessage dm:list){
		            		Exstatus e = new Exstatus();
		            		e.setDMessage(dm);
		            		e.setImage(new Image(dm.getSender().getMiniProfileImageURL()));
		            		statuses.add(e);
		            	}
		            	return statuses;
		            }
		            @Override
		            protected void succeeded(){
		            	List<Exstatus> statuses = getValue();
		            	Collections.reverse(statuses);
		            	for(int i = 0;i < statuses.size();i++){   	
		            		DMessages.set(statuses.get(i));
		            	}
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public void getChats(final Status status){
		Service<ArrayList<Exstatus>> s = new Service<ArrayList<Exstatus>>(){
			@Override
			protected Task<ArrayList<Exstatus>> createTask(){
				Task<ArrayList<Exstatus>> task = new Task<ArrayList<Exstatus>>() {
		            @Override
		            protected ArrayList<Exstatus> call() throws Exception {
		            	ArrayList<Exstatus> Chats = new ArrayList<Exstatus>();
						long chatsId = status.getId();
						try{
							while(chatsId != 0){
								Status replystatus = twitter.showStatus(chatsId);
								Exstatus e = new Exstatus();
								e.setStatus(replystatus);
								e.setImage(new Image(replystatus.getUser().getMiniProfileImageURL()));
								Chats.add(e);
								chatsId = replystatus.getInReplyToStatusId();
							}
						}catch(Exception e){
						}
		            	return Chats;
		            }
		            @Override
		            protected void succeeded(){
		            	ArrayList<Exstatus> Chats = getValue();
		            	Collections.reverse(Chats);
		            	for(int i = 0;i < Chats.size();i++){
		            		ChatStatuses.set(Chats.get(i));
		            	}
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
		s.start();
    	indicator.set(-1);
	}
	
	public Status getStatus(long Id) throws TwitterException{
		return twitter.showStatus(Id);
	}
	
	public void follow(long id) throws TwitterException{
		twitter.createFriendship(id);
	}
	public void unfollow(long id) throws TwitterException{
		twitter.destroyFriendship(id);
	}
	
	public void getUserInfo(final User user){
		Service<UserInfomation> s = new Service<UserInfomation>(){
			@Override
			protected Task<UserInfomation> createTask(){
				Task<UserInfomation> task = new Task<UserInfomation>() {
		            @Override
		            protected UserInfomation call() throws Exception {
		            	UserInfomation ui = new UserInfomation();
		            	ui.setScreenName(StringController.createScreenNameString(user));
		            	ui.setUserInfo(user.getDescription());
		            	ui.setIsFollow(twitter.showFriendship(twitter.getId(),user.getId()).isSourceFollowingTarget());
		            	ui.setIcon(new Image(user.getOriginalProfileImageURL()));
		            	ui.setTweet(getExstatuses(twitter.getUserTimeline(user.getId())));
		            	ui.setFavorite(getExstatuses(twitter.getFavorites(user.getId())));
		            	return ui;
		            }
		            @Override
		            protected void succeeded(){
		            	UserInfo.set(getValue());
		            	indicator.set(0);
		            }
		        };
				return task;
			}
		};
    	indicator.set(-1);
		s.start();
	}
	
	public List<Exstatus> getExstatuses(List<Status> l){
		List<Exstatus> le = new ArrayList<Exstatus>();
		for(Status status:l){
			Exstatus e = new Exstatus();
    		e.setStatus(status);
    		if(status.isRetweet()){
    			e.setImage(new Image(status.getRetweetedStatus().getUser().getMiniProfileImageURL()));

    		}else{
    			e.setImage(new Image(status.getUser().getMiniProfileImageURL()));
    		}	
    		le.add(e);
		}
		return le;
	}
}
