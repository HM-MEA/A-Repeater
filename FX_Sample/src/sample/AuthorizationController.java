package sample;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

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
	@FXML
	private Button button3;
	
	TwitterAuthorization TwAuth = new TwitterAuthorization();
	
	@FXML
	protected void startAuthorization(ActionEvent e) throws Exception {
		TwAuth.tw_start();
		this.label1.setText("ブラウザにURLを入れてPINコードを入手して下さい");
	}
	
	@FXML
	protected void sendPincode(ActionEvent e) throws IOException{
		try {
			TwAuth.getAccessToken(this.textField.getText());
			this.label2.setText("Successed");
		} catch (TwitterException e1) {
			this.label2.setText("Falsed");
		}
		TwAuth.storeAccessToken();
	}
	
	@FXML
	protected void copyurl(ActionEvent e){
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(TwAuth.Aurl);
		clipboard.setContents(selection, null);
	}
}