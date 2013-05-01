package sample;

import java.io.IOException;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterMain {
	String Aurl;
	public AccessToken accessToken;
	public RequestToken requestToken;
	public Twitter twitter;

	public void tw_start() throws TwitterException, IOException{
		twitter = TwitterFactory.getSingleton();
	    twitter.setOAuthConsumer("3nVuSoBZnx6U4vzUxf5w", "Bcs59EFbbsdF6Sl9Ng71smgStWEGwXXKSjYvPVt7qys");
	    requestToken = twitter.getOAuthRequestToken();
	    accessToken = null;
	    Aurl = requestToken.getAuthorizationURL();
	    
	}
	
	public void getAccessToken(String pin) throws TwitterException{
		if(pin.length() > 0){
			accessToken = twitter.getOAuthAccessToken(requestToken, pin);
		}else{
			accessToken = twitter.getOAuthAccessToken();
		}
	}
}

