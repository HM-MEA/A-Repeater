package sample;

import java.io.*;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterAuthorization {
	String Aurl;
	public AccessToken accessToken;
	public RequestToken requestToken;
	public Twitter twitterAuth;
	
	TwitterAuthorization(){
		twitterAuth = TwitterFactory.getSingleton();
	}

	public void tw_start() throws TwitterException, IOException{ 
	    requestToken = twitterAuth.getOAuthRequestToken();
	    accessToken = null;
	    Aurl = requestToken.getAuthorizationURL();
	}
	
	public void getAccessToken(String pin) throws TwitterException{
		if(pin.length() > 0){
			accessToken = twitterAuth.getOAuthAccessToken(requestToken, pin);
		}else{
			accessToken = twitterAuth.getOAuthAccessToken();
		}
	}
	
	public void storeAccessToken() throws IOException{
		File file = new File("AccessToken.txt");
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		pw.println(accessToken.getToken()+"Å@"+accessToken.getTokenSecret());
		pw.close();
	}
}

