package sample;

import java.awt.Desktop;
import java.net.URI;

import twitter4j.TwitterException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AuthorizationController {
	
	@FXML
	private TextField textField;
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Button button1;
	@FXML
	private Button button2;
	
	TwitterMain Twmain = new TwitterMain();
	
	@FXML
	protected void startAuthorization(ActionEvent e) throws Exception {
		this.label1.setText("ブラウザに接続してPINコードを入手します");
		Twmain.tw_start();
	}
	
	@FXML
	protected void sendPincode(ActionEvent e){
		try {
			Twmain.getAccessToken(this.textField.getText());
			this.label2.setText("Successed");
		} catch (TwitterException e1) {
			this.label2.setText("Falsed");
		}
	}
}