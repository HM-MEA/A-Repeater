package sample;

import javafx.scene.image.Image;
import twitter4j.Status;

public class Exstatus {

	private Status status;
	private Image image;
	
	public void setStatus(Status s){
		status = s;
	}
	
	public void setImage(Image i){
		image = i;
	}
	
	public Status getStatus(){
		return status;
	}
	
	public Image getImage(){
		return image;
	}
	
}
