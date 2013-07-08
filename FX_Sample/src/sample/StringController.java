package sample;

import java.util.Calendar;
import java.util.Date;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;

public class StringController {
	
	static String createScreenNameString(User user){
		if(user.isProtected()){
			return "@" + user.getScreenName() + " - " + user.getName() + " 🔒\n";
		}else{
			return "@" + user.getScreenName() + " - " + user.getName() + "\n";
		}
	}

	static String createTweetString(Status status){
		if(status.isRetweet()){
			return "@" + status.getRetweetedStatus().getUser().getScreenName() + " - " + status.getRetweetedStatus().getUser().getName() +"\n"+ status.getRetweetedStatus().getText() + "\n" + returndate(status.getRetweetedStatus().getCreatedAt()) + " ReTweeted by @ "+ status.getUser().getScreenName();
		}else{
			if(status.getUser().isProtected()){
				return "@" + status.getUser().getScreenName() + " - " + status.getUser().getName() + " 🔒\n" +  status.getText() + "\n" + returndate(status.getCreatedAt());
			}else{
				return "@" + status.getUser().getScreenName() + " - " + status.getUser().getName() + "\n" +  status.getText() + "\n" + returndate(status.getCreatedAt()); 
			}
		}
	}
	
	static String createDMString(DirectMessage dm){
		if(dm.getSender().isProtected()){
			return "@" + dm.getSender().getScreenName() + " - " + dm.getSender().getName() + " 🔒\n" +  dm.getText() + "\n" + returndate(dm.getCreatedAt());
		}else{
			return "@" + dm.getSender().getScreenName() + " - " + dm.getSender().getName() + "\n" +  dm.getText() + "\n" + returndate(dm.getCreatedAt());
		}
	}
	
	static String returndate(Date date){
		Calendar calendar = Calendar.getInstance();
		Calendar calendar_n = Calendar.getInstance();
		calendar.setTime(date);
		if(calendar.get(Calendar.YEAR) == calendar_n.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == calendar_n.get(Calendar.DAY_OF_YEAR)){
			return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
		}else if(!(calendar.get(Calendar.DAY_OF_YEAR) == calendar_n.get(Calendar.DAY_OF_YEAR))){
			return (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
		}else{
			return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND); 
		}
	}
}
