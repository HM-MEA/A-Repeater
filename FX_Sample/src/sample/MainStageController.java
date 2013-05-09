package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainStageController{
	
	File Authfile = new File("AccessToken.txt");
	ArrayList<AccessToken> Tokenlist = new ArrayList<AccessToken>();
	TwitterMain Twmain = new TwitterMain();
	
	@FXML
	private Button button1;
	
	@FXML
	private Button button2;
	
	@FXML
	private Button button3;
	
	@FXML
	private TextField textfield;
	
	@FXML
	private Label label;
	
	ObservableList<String> list = FXCollections.observableArrayList();
    
	@FXML
	ListView<String> listView = new ListView<String>(list);
	
	@FXML
	protected void accountAuthorization(ActionEvent e) throws Exception {
		Stage AStage = new Stage();
		AStage.setTitle("Authorization");
		AStage.initModality(Modality.APPLICATION_MODAL);
		Parent Aroot = FXMLLoader.load(getClass().getResource("Authorization.fxml"));
		Scene Ascene = new Scene(Aroot);
		AStage.setScene(Ascene);
		AStage.show();
	}
	
	@FXML
	protected void readTokenfile() throws FileNotFoundException{
		Scanner scan = new Scanner(Authfile);
		AccessToken accessToken;
		while(scan.hasNext()){
			accessToken = new AccessToken(scan.next(),scan.next());
			Tokenlist.add(accessToken);
		}
		scan.close();
		setToken();
		try {
			this.label.setText(Twmain.getScreenName());
		} catch (TwitterException e) {
			this.label.setText("faild");
			e.printStackTrace();
		}
		setHometimeline();
	}
	
	@FXML
	protected void postTweet() throws Exception{
		String str = this.textfield.getText();
		if(str.length() > 0){
			Twmain.postTweet(str);
		}
		this.textfield.setText("");
	}
	
	public void setToken(){
		Twmain.setToken(Tokenlist.get(0));
	}
	
	void setHometimeline(){		
		try {
			List<Status> statuses = Twmain.readTimeline();
			for (Status status : statuses) {
		        list.add(status.getUser().getName() + ":" +  status.getText());
				System.out.println(status.getUser().getName() + ":" +  status.getText());
		    }
		} catch (TwitterException e) {
		}
	    
	}
}
