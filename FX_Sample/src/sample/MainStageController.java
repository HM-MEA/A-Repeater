package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainStageController implements Initializable{
	
	File Authfile = new File("AccessToken.txt");
	ArrayList<AccessToken> Tokenlist = new ArrayList<AccessToken>();
	static ArrayList<Status> StatusList = new ArrayList<Status>();
	TwitterMain Twmain = new TwitterMain();
	TwitterStreamMain TwSmain = new TwitterStreamMain();
	static long status_id = 1;
	long reply_id = 0;
	
	@FXML
	private Button button1;
	
	@FXML
	private Button button2;
	
	@FXML
	private Button button3;
	
	@FXML
	private TextArea textarea;
	
	@FXML
	private Label label1;
	
	@FXML
	private Label label2;
	   
	@FXML
	static ListView<Label> timelines;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		readTokenfile();
		TwSmain.startUserStream();
	}
	
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
	protected void readTokenfile(){
		Scanner scan = null;
		try {
			scan = new Scanner(Authfile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		AccessToken accessToken;
		while(scan.hasNext()){
			accessToken = new AccessToken(scan.next(),scan.next());
			Tokenlist.add(accessToken);
		}
		scan.close();
		setToken();
		try {
			this.label1.setText(Twmain.getScreenName());
		} catch (Exception e) {
			this.label2.setText("スクリーンネームの取得に失敗しました");
			e.printStackTrace();
		}
	}
	
	@FXML
	protected void postTweet(){
		String str = this.textarea.getText();
		if(str.length() > 0){
			try {
				if(reply_id == 0){
					Twmain.postTweet(str);
				}else{
					Twmain.postTweet(str, reply_id);
				}
			} catch (TwitterException e) {
				this.label2.setText("投稿に失敗しました");
				e.printStackTrace();
			}
		}
		this.textarea.setText("");
		reply_id = 0;
	}
	
	public void setToken(){
		Twmain.setToken(Tokenlist.get(0));
		TwSmain.setToken(Tokenlist.get(0));
	}
	
	@FXML
	protected void onSetHometimeline(){
		try {
			List<Status> statuses = Twmain.readTimeline(1,status_id);
			Collections.reverse(statuses);
			for (Status status : statuses) {
				setTimeline(status);
			}
			status_id = StatusList.get(0).getId();
		} catch (Exception e) {
			this.label2.setText("タイムラインの更新に失敗しました");
		}
	}
	
	static void setTimeline(Status status){
		StatusList.add(0,status);
		status_id = status.getId();
		
		Label label = new Label();
		label.setText(status.getUser().getScreenName() + ":" +  status.getText() + status.getCreatedAt());
		timelines.getItems().add(0,label);
	}
	@FXML
	private void onReply(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		int index = model.getSelectedIndex();
		Status replystatus = StatusList.get(index);
		textarea.setText("@" + replystatus.getUser().getScreenName() + " ");
		reply_id = replystatus.getId();
	}
	
}
