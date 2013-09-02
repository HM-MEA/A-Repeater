package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class NotificationWindowController {
	
	@FXML
	private VBox vbox;
	
	@FXML
	private Label Title;
	
	@FXML 
	private Label TargetName;
	
	@FXML
	private Label status;
		
	public void setNotificationData(FavoritedStatus fs){
	Title.setText("���@" + fs.getSource().getScreenName() + "�����C�ɓ���ɓo�^");
	TargetName.setText(fs.getTarget().getScreenName());
	status.setText(fs.getFavoritedStatus().getText());
	}
}
