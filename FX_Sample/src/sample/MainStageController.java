package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class MainStageController implements Initializable{
	
	File Authfile = new File("AccessToken.txt");
	ArrayList<AccessToken> Tokenlist = new ArrayList<AccessToken>();
	TwitterMain Twmain = new TwitterMain();
	TwitterStreamMain TwSmain = new TwitterStreamMain();
	long Timeline_id = 1;
	long Mention_id = 1;
	long Message_id = 1;
	long reply_id = 0;
	static String ScreenName;
	File image = null;
	Rectangle2D ScreenBounds = Screen.getPrimary().getBounds();
	
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
	private ListView<Label> dms;
	
	@FXML
	private ListView<Label> chats;
	
	@FXML
	private TabPane TwitterTab;
	
	@FXML
	private Label imagename;
	
	@FXML
	private Button addimage;

	@FXML
	private ProgressIndicator Indicator = new ProgressIndicator();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		if(Authfile.exists()){
			readTokenfile();
			Twmain.TimelineStatuses.addListener(new ChangeListener<Exstatus>(){
				@Override
				public void changed(ObservableValue<? extends Exstatus> arg0,Exstatus arg1, Exstatus arg2) {
					setTimeline(arg2);
				}
			});
			Twmain.MentionStatuses.addListener(new ChangeListener<Exstatus>(){
				@Override
				public void changed(ObservableValue<? extends Exstatus> arg0,Exstatus arg1, Exstatus arg2) {
					setMention(arg2);
				}
			});
			Twmain.DMessages.addListener(new ChangeListener<Exstatus>(){
				@Override
				public void changed(ObservableValue<? extends Exstatus> arg0,Exstatus arg1, Exstatus arg2) {
					setDM(arg2);
				}
			});
			onUpdateTimelines();
			TwSmain.timeline_status.addListener(new ChangeListener<Exstatus>(){
				@Override
				public void changed(ObservableValue<? extends Exstatus> arg0,Exstatus arg1,Exstatus arg2) {
					setTimeline(arg2);
				}
			});
			TwSmain.mention_status.addListener(new ChangeListener<Exstatus>(){
				@Override
				public void changed(ObservableValue<? extends Exstatus> arg0,Exstatus arg1,Exstatus arg2) {
					setMention(arg2);
				}
			});
			TwSmain.dmessage.addListener(new ChangeListener<Exstatus>(){
				@Override
				public void changed(ObservableValue<? extends Exstatus> arg0,Exstatus arg1,Exstatus arg2) {
					setDM(arg2);
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
			TwSmain.favoritedStatusProperty.addListener(new ChangeListener<FavoritedStatus>(){
				@Override
				public void changed(ObservableValue<? extends FavoritedStatus> arg0,FavoritedStatus arg1, FavoritedStatus arg2) {
					favoriteNotification(arg2);
				}
			});
			Twmain.ChatStatuses.addListener(new ChangeListener<Exstatus>(){
				@Override
				public void changed(ObservableValue<? extends Exstatus> arg0,Exstatus arg1, Exstatus arg2) {
					setChats(arg2);
				}
			});
			Twmain.indicator.addListener(new ChangeListener<Number>(){
				@Override
				public void changed(ObservableValue<? extends Number> arg0,Number arg1, Number arg2) {	
					if(arg2.intValue() == -1){
						Indicator.setProgress(-1);
						Indicator.setVisible(true);
					}else{
						Indicator.setProgress(0);
						Indicator.setVisible(false);
					}	
				}
			});
			try {
				TwSmain.startUserStream();
			} catch (Exception e) {
				e.printStackTrace();
			}
			imagename.setVisible(false);
		}else{
			try {
				accountAuthorization();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	protected void accountAuthorization() throws IOException{
		Authfile.createNewFile();
		Authfile.setWritable(true);
		
		Stage AStage = new Stage();
		AStage.setTitle("Authorization");
		AStage.initModality(Modality.APPLICATION_MODAL);
		Parent Aroot = FXMLLoader.load(getClass().getResource("Authorization.fxml"));
		Scene Ascene = new Scene(Aroot);
		AStage.setScene(Ascene);
		AStage.show();
				
		AStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent arg0) {
				initialize(null, null);
			}
		});
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
			this.label2.setText("スクリーンネームの取得に失敗しました");
			e.printStackTrace();
		}		
	}
	
	@FXML
	protected void postTweet(){
		String str = this.textarea.getText();
	
		try {
			if(reply_id == 0){
				if(image == null){
					if(str.length() > 0){
						Twmain.postTweet(str);
					}else{
						throw new TwitterException("");
					}
				}else{
					Twmain.postTweet(str, image);
				}
			}else{
				if(image == null){
					Twmain.postTweet(str, reply_id);
				}else{
					Twmain.postTweet(str, reply_id, image);
				}
			}
			this.label2.setText("投稿しました");
		} catch (TwitterException e) {
			this.label2.setText("投稿に失敗しました");
		}
		
		this.textarea.setText("");
		reply_id = 0;
		onRemoveImage();
	}
	
	public void setToken(){
		Twmain.setToken(Tokenlist.get(0));
		TwSmain.setToken(Tokenlist.get(0));
	}
	
	@FXML
	private void onUpdateTimelines(){
		try {
			Twmain.getTimeline(1, Timeline_id);
			Twmain.getMentions(1, Mention_id);
			Twmain.getDMs(1, Message_id);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
				
	public void setTimeline(Exstatus status){
		Timeline_id = status.getStatus().getId();
		
		Label label = new Label();
		label.setWrapText(true);
		if(status.getStatus().isRetweet()){
			label.setUserData(status.getStatus().getRetweetedStatus());
		}else{
			label.setUserData(status.getStatus());
		}
		label.setText(StringController.createTweetString(status.getStatus()));
		label.setGraphic(new ImageView(status.getImage()));
		label.setContentDisplay(ContentDisplay.LEFT);
		
		timelines.getItems().add(0,label);		
	}
	
	public void setMention(Exstatus status){
		Mention_id = status.getStatus().getId();
				
		Label label = new Label();
		label.setWrapText(true);
		label.setUserData(status.getStatus());
		label.setText(StringController.createTweetString(status.getStatus()));
		label.setGraphic(new ImageView(status.getImage()));
		label.setContentDisplay(ContentDisplay.LEFT);
		mentions.getItems().add(0,label);
		
		
	}
	
	public void setDM(Exstatus dm){
		Message_id = dm.getDMessage().getId();
		
		Label label = new Label();
		label.setUserData(dm);
		label.setText(StringController.createDMString(dm.getDMessage()));
		label.setGraphic(new ImageView(dm.getImage()));
		
		dms.getItems().add(0,label);
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
			this.label2.setText("Retweetしました");
		} catch (TwitterException e) {
			this.label2.setText("ReTweetに失敗しました");
		}
	}
	
	@FXML
	private void onTimelineFavorite(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status favoritestatus = (Status)model.getSelectedItem().getUserData();
		try {
			Twmain.favorite(favoritestatus);
			this.label2.setText("Favoriteしました");
		} catch (TwitterException e) {
			this.label2.setText("Favoriteに失敗しました");
		}
	}
	
	@FXML
	private void onTimelineSendDM() throws IOException{
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status status = (Status)model.getSelectedItem().getUserData();
		sendDM(status.getUser());
	}
	
	@FXML
	private void onMentionSendDM() throws IOException{
		MultipleSelectionModel<Label> model = mentions.getSelectionModel(); 
		Status status = (Status)model.getSelectedItem().getUserData();
		sendDM(status.getUser());
	}
	
	private void sendDM(User user) throws IOException{
		Stage AStage = new Stage();
		AStage.setTitle("Send DirectMessage");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DirectMessageWindow.fxml"));
		Parent Aroot = (Parent) loader.load();
		DirectMessageWindowController controller = loader.getController();
		controller.setData(Twmain,user);	
		Scene Ascene = new Scene(Aroot);
		AStage.setScene(Ascene);
		AStage.show();
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
			this.label2.setText("Retweetしました");
		} catch (TwitterException e) {
			this.label2.setText("ReTweetに失敗しました");
		}
	}
	
	@FXML
	private void onMentionFavorite(){
		MultipleSelectionModel<Label> model = mentions.getSelectionModel(); 
		Status favoritestatus = (Status)model.getSelectedItem().getUserData();
		try {
			Twmain.favorite(favoritestatus);
			this.label2.setText("Favoriteしました");
		} catch (TwitterException e) {
			this.label2.setText("Favoriteに失敗しました");
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
			Scene Ascene = new Scene(Aroot);
			AStage.setScene(Ascene);
			AStage.show();
			UserWindowController controller = loader.getController();
			controller.setUserInfo(Twmain,status.getUser());		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void onSetTimelineChats(){
		MultipleSelectionModel<Label> model = timelines.getSelectionModel(); 
		Status OpenUserStatus = (Status)model.getSelectedItem().getUserData();
		chats.getItems().remove(0,chats.getItems().size());
		Twmain.getChats(OpenUserStatus);
		TwitterTab.getSelectionModel().select(3);
	}
	
    @FXML
    private void onSetMentionChats(){
    	MultipleSelectionModel<Label> model = mentions.getSelectionModel(); 
		Status OpenUserStatus = (Status)model.getSelectedItem().getUserData();
		chats.getItems().remove(0,chats.getItems().size());
		Twmain.getChats(OpenUserStatus);
		TwitterTab.getSelectionModel().select(3);
    }
				
	private void setChats(Exstatus status){
		
		Label label = new Label();
		label.setWrapText(true);
		label.setUserData(status);
		label.setText(StringController.createTweetString(status.getStatus()));
		label.setGraphic(new ImageView(status.getImage()));
		label.setContentDisplay(ContentDisplay.LEFT);
		chats.getItems().add(0,label);

	}
	
	@FXML
	private void onStreamChecked(){
		if(streamcheck.isSelected()){
			try {
				TwSmain.startUserStream();
				this.label2.setText("streamに接続しました");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			TwSmain.stopStream();
			this.label2.setText("streamを切断しました");
		}
	}
	
	@FXML
	private void onAddImage(){
		FileChooser fc = new FileChooser();
		fc.setTitle("添付画像の選択");
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		fc.getExtensionFilters().add(new ExtensionFilter("jpg,png,gif","*.jpg", "*.png","*.gif"));
		image = fc.showOpenDialog(null);
		try {
			ImageView uploadimage = new ImageView(new Image(image.toURI().toURL().toString()));
			uploadimage.setPreserveRatio(true);
			uploadimage.setFitHeight(20);
			imagename.setText("添付画像  : " + image.getName());
			imagename.setGraphic(uploadimage);
			imagename.setVisible(true);
		} catch (Exception e) {
		}
		
	}
	
	@FXML
	private void onRemoveImage(){
		image = null;
		imagename.setVisible(false);
	}
	
	private void favoriteNotification(FavoritedStatus fs){
		final Stage AStage = new Stage();
		AStage.setTitle("Notification");
		AStage.setResizable(false);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NotificationWindow.fxml"));
			Parent Aroot = (Parent)loader.load();
			Scene Ascene = new Scene(Aroot);
			AStage.setScene(Ascene);
			AStage.setX(ScreenBounds.getWidth() - 320);
			AStage.setY(ScreenBounds.getHeight() - 190);
			AStage.show();
			NotificationWindowController controller = loader.getController();
			controller.setNotificationData(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Timeline closetimeline = new Timeline(new KeyFrame(new Duration(5000),new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				AStage.close();
			}
		}));
		closetimeline.play();
	}
	
	@FXML
	private void onClosed(){
		Platform.exit();
	}
}
