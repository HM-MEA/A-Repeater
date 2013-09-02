package sample;

import twitter4j.Status;
import twitter4j.User;

public class FavoritedStatus{
	User source;
	User target;
	Status favoritedStatus;
	
	public FavoritedStatus(User source, User target, Status favoritedStatus) {
		this.source = source;
		this.target = target;
		this.favoritedStatus = favoritedStatus;
	}
	public User getSource() {
		return source;
	}
	public void setSource(User source) {
		this.source = source;
	}
	public User getTarget() {
		return target;
	}
	public void setTarget(User target) {
		this.target = target;
	}
	public Status getFavoritedStatus() {
		return favoritedStatus;
	}
	public void setFavoritedStatus(Status favoritedStatus) {
		this.favoritedStatus = favoritedStatus;
	}		
}
