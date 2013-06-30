package sample;

import java.util.Calendar;
import java.util.Date;

import twitter4j.Status;

public class StringController {

	static String createTweetString(Status status){
		if(status.getUser().isProtected()){
			return "@" + status.getUser().getScreenName() + " 🔒\n" +  status.getText() + "\n" + returndate(status.getCreatedAt());
		}else{
			return "@" + status.getUser().getScreenName() + "\n" +  status.getText() + "\n" + returndate(status.getCreatedAt());
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
