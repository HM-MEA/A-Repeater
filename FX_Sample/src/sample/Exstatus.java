package sample;

import javafx.scene.image.Image;
import twitter4j.DirectMessage;
import twitter4j.Status;

public class Exstatus {

	private Status status;
	private Image image;
	private DirectMessage dmessage;
	
	public void setStatus(Status s){
		status = s;
	}
	
	public void setImage(Image i){
		image = i;
	}
	
	public void setDMessage(DirectMessage dm){
		dmessage = dm;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public Image getImage(){
		return image;
	}
	
	public DirectMessage getDMessage(){
		return dmessage;
	}
	
}
