package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainStageController implements Initializable{
	
	File Authfile = new File("AccessToken.txt");
	ArrayList<AccessToken> Tokenlist = new ArrayList<AccessToken>();
	TwitterMain Twmain = new TwitterMain();
	TwitterStreamMain TwSmain = new TwitterStreamMain();
	long Timeline_id = 1;
	long Mention_id = 1;
	long reply_id = 0;
	static String ScreenName;
	
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
	static CheckBox streamcheck;
	   
	@FXML
	private ListView<Label> timelines;
	
	@FXML
	private ListView<Label> mentions;
	
	@FXML
	private ListView<Label> chats;
	
	@FXML
	private TabPane TwitterTab;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		readTokenfile();
		onUpdateTimelines();
		TwSmain.timeline_status.addListener(new ChangeListener<Status>(){
			@Override
			public void changed(ObservableValue<? extends Status> arg0,	Status arg1, Status arg2) {
				setTimeline(arg2);
			}
		});
		TwSmain.mention_status.addListener(new ChangeListener<Status>(){
			@Override
			public void changed(ObservableValue<? extends Status> arg0,	Status arg1, Status arg2) {
				setMention(arg2);
			}
		});
		TwSmain.stream_f.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,	Number arg1, Number arg2) {
				if(arg2.intValue() == 1){
					streamcheck.setSelected(true);
				}else{
					streamcheck.setSelected(false);
				}
			}
		});
		try {
			TwSmain.startUserStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			ScreenName = Twmain.getScreenName();
			this.label1.setText(ScreenName);
		} catch (Exception e) {
			this.label2.setText("ÉXÉNÉäÅ[ÉìÉlÅ[ÉÄÇÃéÊìæÇ…é∏îsÇµÇ‹ÇµÇΩ");
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
			this.label2.setText("ìäçeÇµÇ‹ÇµÇΩ");
			} catch (TwitterException e) {
				this.label2.setText("ìäçeÇ…é∏îsÇµÇ‹ÇµÇΩ");
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
	private void onUpdateTimelines(){
		try {
			List<Status> TimelineStatuses = Twmain.getTimeline(1,Timeline_id);
			List<Status> MentionStatuses = Twmain.getMentions(1, Mention_id);
			Collections.reverse(TimelineStatuses);
			Collections.reverse(MentionStatuses);
			for (Status status : TimelineStatuses) {
				setTimeline(status);
			}
			for(Status status : MentionStatuses){
				setMention(status);
			}
		} catch (Exception e) {
			this.label2.setText("É^ÉCÉÄÉâÉCÉìÇÃçXêVÇ…é∏îsÇµÇ‹ÇµÇΩ");
		}
	}
	
	public void setTimeline(Status status){
		Timeline_id = status.getId();
		
		Label label = new Label();
		label.setUserData(status);
		label.setText(StringController.createTweetString(status));
		timelines.getItems().add(0,label);
	}
	
	public void setMention(Status status){
		Mention_id = status.getId();
		
		Label label = new Label();
		label.setUserData(status);
		label.setText(StringController.createTweetString(status));
		mentions.getItems().add(0,label);
	}
	
	@FXML
	private void onTimelineReply(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status replystatus = (Status)model.getSelectedItem().getUserData();
		textarea.setText("@" + replystatus.getUser().getScreenName() + " ");
		reply_id = replystatus.getId();
	}
	
	@FXML
	private void onTimelineReTweet(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status retweetstatus = (Status)model.getSelectedItem().getUserData();
		try {
			Twmain.retweet(retweetstatus);
			this.label2.setText("RetweetÇµÇ‹ÇµÇΩ");
		} catch (TwitterException e) {
			this.label2.setText("ReTweetÇ…é∏îsÇµÇ‹ÇµÇΩ");
		}
	}
	
	@FXML
	private void onTimelineFavorite(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status favoritestatus = (Status)model.getSelectedItem().getUserData();
		try {
			Twmain.favorite(favoritestatus);
			this.label2.setText("FavoriteÇµÇ‹ÇµÇΩ");
		} catch (TwitterException e) {
			this.label2.setText("FavoriteÇ…é∏îsÇµÇ‹ÇµÇΩ");
		}
	}
	
	@FXML
	private void onMentionsReply(){
		MultipleSelectionModel<Label> model = mentions.getSelectionModel();
		Status replystatus = (Status)model.getSelectedItem().getUserData();
		textarea.setText("@" + replystatus.getUser().getScreenName() + " ");
		reply_id = replystatus.getId();
	}
	
	@FXML
	private void onMentionReTweet(){
		MultipleSelectionModel<Label> model = mentions.getSelectionModel(); 
		Status retweetstatus = (Status)model.getSelectedItem().getUserData();
		try {
			Twmain.retweet(retweetstatus);
			this.label2.setText("RetweetÇµÇ‹ÇµÇΩ");
		} catch (TwitterException e) {
			this.label2.setText("ReTweetÇ…é∏îsÇµÇ‹ÇµÇΩ");
		}
	}
	
	@FXML
	private void onMentionFavorite(){
		MultipleSelectionModel<Label> model = mentions.getSelectionModel(); 
		Status favoritestatus = (Status)model.getSelectedItem().getUserData();
		try {
			Twmain.favorite(favoritestatus);
			this.label2.setText("FavoriteÇµÇ‹ÇµÇΩ");
		} catch (TwitterException e) {
			this.label2.setText("FavoriteÇ…é∏îsÇµÇ‹ÇµÇΩ");
		}
	}
	
	@FXML
	private void onOpenTimelineUser(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status OpenUserStatus = (Status)model.getSelectedItem().getUserData();
		openUser(OpenUserStatus);
	}
	
	@FXML
	private void onOpenMentionUser(){
		MultipleSelectionModel<Label> model = mentions.getSelectionModel(); 
		Status OpenUserStatus = (Status)model.getSelectedItem().getUserData();
		openUser(OpenUserStatus);
	}
	
	private void openUser(Status status){
		Stage AStage = new Stage();
		AStage.setTitle("User - @" + status.getUser().getScreenName());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("UserWindow.fxml"));
			Parent Aroot = (Parent)loader.load();
			UserWindowController controller = loader.getController();
			controller.setUserInfo(Twmain,status.getUser());
			Scene Ascene = new Scene(Aroot);
			AStage.setScene(Ascene);
			AStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onSetTimelineChats(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status OpenUserStatus = (Status)model.getSelectedItem().getUserData();
		try {
			setChats(OpenUserStatus);
		} catch (TwitterException e) {
		}
		TwitterTab.getSelectionModel().select(2);
	}
	
	private void setChats(Status status) throws TwitterException{
		chats.getItems().remove(0,chats.getItems().size());
		long chatsId = status.getId();
		while(chatsId != 0){
			Status replystatus = Twmain.getStatus(chatsId);
			chats.setUserData(replystatus);
			Label label = new Label();
			label.setText(StringController.createTweetString(replystatus));
			chats.getItems().add(0,label);
			chatsId = replystatus.getInReplyToStatusId();
		}
	}
	
	@FXML
	private void onStreamChecked(){
		if(streamcheck.isSelected()){
			try {
				TwSmain.startUserStream();
				this.label2.setText("streamÇ…ê⁄ë±ÇµÇ‹ÇµÇΩ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			TwSmain.stopStream();
			this.label2.setText("streamÇêÿífÇµÇ‹ÇµÇΩ");
		}
	}
	
	@FXML
	private void onClosed(){
		Platform.exit();
	}
}
