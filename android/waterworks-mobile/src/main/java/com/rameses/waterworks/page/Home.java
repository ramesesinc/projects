package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.layout.Header;
import java.util.Iterator;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Home {
    
    private VBox root;
    private ImageView logo;
    private FlowPane pane;
    
    public Home(){
        Header.TITLE.setText("Waterworks System");
        
        MenuItem accountItem = new MenuItem("icon/database.png","Accounts","View the list of account information including its meter and application information.");
        Node account = accountItem.getLayout();
        accountItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new AccountList().getLayout());
            }
        });
        accountItem.getLayout().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                Main.ROOT.setCenter(new AccountList().getLayout());
            }
        });
        
        MenuItem sheetItem = new MenuItem("icon/meter.png","Reading Sheet","Update the the account's meter reading history by capturing its new meter reading.");
        Node sheet = sheetItem.getLayout();
        sheetItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new ReadingSheet().getLayout());
            }
        });
        sheetItem.getLayout().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                Main.ROOT.setCenter(new ReadingSheet().getLayout());
            }
        });
        
        MenuItem downloadItem = new MenuItem("icon/download.png","Download","Download account and meter information from the server database.");
        Node download = downloadItem.getLayout();
        downloadItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                 Main.ROOT.setCenter(new Download().getLayout());
            }
        });
        downloadItem.getLayout().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                 Main.ROOT.setCenter(new Download().getLayout());
            }
        });
        
        MenuItem uploadItem = new MenuItem("icon/upload.png","Upload","Upload meter-reading information to the server database.");
        Node upload = uploadItem.getLayout();
        uploadItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new Upload().getLayout());
            }
        });
        uploadItem.getLayout().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                Main.ROOT.setCenter(new Upload().getLayout());
            }
        });
        
        MenuItem settingItem = new MenuItem("icon/mysetting1.png","Setting","Manage your user account and system setting.");
        Node setting = settingItem.getLayout();
        settingItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new Setting().getLayout());
            }
        });
        settingItem.getLayout().setOnTouchPressed(new EventHandler<TouchEvent>(){
            @Override
            public void handle(TouchEvent event) {
                Main.ROOT.setCenter(new Setting().getLayout());
            }
        });
        
        StackPane stack = new StackPane();
        stack.setAlignment(Pos.CENTER);
        stack.setPadding(new Insets(0,15,0,15));
        stack.getChildren().add(createReadingBulletin());
        
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        if(Main.HEIGHT > 800){
            root.setSpacing(30);
            root.setPadding(new Insets(50, 50, 50, 50));
        }else{
            root.setSpacing(15);
            root.setPadding(new Insets(15, 15, 15, 15));
        }
        root.getChildren().addAll(account,sheet,download,upload,setting,stack);
    }
    
    private StackPane createReadingBulletin(){
        int totalrecords = DatabasePlatformFactory.getPlatform().getDatabase().getNoOfTotalRecords();
        int readrecords = DatabasePlatformFactory.getPlatform().getDatabase().getNoOfTotalReadRecords();
        int unreadrecords = totalrecords - readrecords;
        
        ImageView check = new ImageView(new Image("icon/check.png"));
        
        ImageView cancel = new ImageView(new Image("icon/cancel.png"));
        
        Label read = new Label(readrecords + " Records");
        read.getStyleClass().add("readingbulletin-label");
        
        Label unread = new Label(unreadrecords + " Records");
        unread.getStyleClass().add("readingbulletin-label");
        
        HBox space = new HBox();
        space.setPrefWidth(25);
        
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(check,read,space,cancel,unread);
        
        StackPane root = new StackPane();
        root.setId("readingbulletin-container");
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(15));
        root.getChildren().add(box);
        return root;
    }
    
    private class MenuItem {
        
        private HBox container;
        private ImageView image;
        private Text title;
        private Label desc;
        
        public MenuItem(String image_url, String title, String desc){
            this.image = new ImageView(new Image(image_url));
            
            VBox imageContainer = new VBox();
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.getChildren().add(this.image);
            
            this.title = new Text(title);
            this.title.getStyleClass().add("home-title");
            
            this.desc = new Label(desc);
            this.desc.getStyleClass().add("home-desc");
            this.desc.setWrapText(true);
            
            VBox textContainer = new VBox();
            if(Main.HEIGHT > 800){
                textContainer.setSpacing(10);
            }else{
                textContainer.setSpacing(5);
            }
            textContainer.setAlignment(Pos.CENTER_LEFT);
            textContainer.getChildren().addAll(this.title,this.desc);
            
            container = new HBox();
            if(Main.HEIGHT > 800){
                container.setSpacing(20);
            }else{
                container.setSpacing(10);
                image.setFitWidth(image.getFitWidth()*0.5);
                image.setFitHeight(image.getFitHeight()*0.5);
            }
            container.getChildren().addAll(imageContainer,textContainer);
        }
  
        public Node getLayout(){
            return container;
        }
        
        public ImageView getImage(){
            return image;
        }
    }
    
    public Node getLayout(){
        return root;
    }
    
}
