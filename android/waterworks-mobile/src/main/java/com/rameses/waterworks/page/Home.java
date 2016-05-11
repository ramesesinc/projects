package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Home {
    
    private ScrollPane root;
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
        
        MenuItem sheetItem = new MenuItem("icon/meter.png","Reading Sheet","Update the the account's meter reading history by capturing its new meter reading.");
        Node sheet = sheetItem.getLayout();
        sheetItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new ReadingSheet(null,null,-1).getLayout());
            }
        });
        
        MenuItem downloadItem = new MenuItem("icon/download.png","Download","Download account and meter information from the server database.");
        Node download = downloadItem.getLayout();
        downloadItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Thread t1 = new Thread(){
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                Dialog.wait("Please wait ...");
                            }
                        });
                    }
                };
                t1.start();
        
                Thread t2 = new Thread(){
                    @Override
                    public void run(){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run() {
                                Main.ROOT.setCenter(new Download().getLayout());
                            }
                        });
                    }
                };
                t2.start();
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
        
        MenuItem ratesItem = new MenuItem("icon/rates.png","Water Rates","View the list of water-rates.");
        Node rates = ratesItem.getLayout();
        ratesItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new Rates().getLayout());
            }
        });
        
        MenuItem settingItem = new MenuItem("icon/mysetting1.png","Setting","Manage the system settings.");
        Node setting = settingItem.getLayout();
        settingItem.getLayout().setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Main.ROOT.setCenter(new Setting().getLayout());
            }
        });
        
        StackPane stack = new StackPane();
        stack.setAlignment(Pos.CENTER);
        stack.setPadding(new Insets(0,5,0,5));
        stack.getChildren().add(createReadingBulletin());
        
        VBox container = new VBox();
        container.setMaxWidth(Main.WIDTH);
        container.setAlignment(Pos.TOP_CENTER);
        container.getChildren().addAll(account,sheet,download,upload,rates,setting,stack);
        if(Main.HEIGHT < 700){
            container.setSpacing(8);
            container.setPadding(new Insets(10, 15, 10, 15));
        }else if(Main.HEIGHT < 1200){
            container.setSpacing(12);
            container.setPadding(new Insets(15, 30, 15, 30));
        }else{
            container.setSpacing(15);
            container.setPadding(new Insets(20, 50, 20, 50));
        }
        
        root = new ScrollPane();
        root.setMaxWidth(Main.WIDTH);
        root.setContent(container);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    if(Dialog.isOpen){ Dialog.hide(); return; }
                }
            }
        });
    }
    
    private StackPane createReadingBulletin(){
        int totalrecords = DatabasePlatformFactory.getPlatform().getDatabase().getNoOfTotalRecords();
        int readrecords = DatabasePlatformFactory.getPlatform().getDatabase().getNoOfTotalReadRecords();
        int unreadrecords = totalrecords - readrecords;
        
        ImageView check = new ImageView(new Image("icon/check.png"));
        ImageView cancel = new ImageView(new Image("icon/cancel.png"));
        if(Main.HEIGHT < 700){
            check.setFitWidth(check.getImage().getWidth() * 0.5);
            check.setFitHeight(check.getImage().getHeight() * 0.5);
            cancel.setFitWidth(cancel.getImage().getWidth() * 0.5);
            cancel.setFitHeight(cancel.getImage().getHeight() * 0.5);
        }else if(Main.HEIGHT < 1200){
            check.setFitWidth(check.getImage().getWidth() * 0.7);
            check.setFitHeight(check.getImage().getHeight() * 0.7);
            cancel.setFitWidth(cancel.getImage().getWidth() * 0.7);
            cancel.setFitHeight(cancel.getImage().getHeight() * 0.7);
        }
        
        Label read = new Label("READ : " + readrecords);
        read.getStyleClass().add("readingbulletin-label");
        
        Label unread = new Label("UNREAD : " + unreadrecords);
        unread.getStyleClass().add("readingbulletin-label");
        
        HBox space = new HBox();
        space.setPrefWidth(25);
        
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(check,read,space,cancel,unread);
        box.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                System.out.println("Sector");
                System.out.println(db.showTableData("download_stat"));
                System.out.println("=================================================================");
            }
        });
        
        StackPane root = new StackPane();
        root.setPadding(new Insets(8,0,8,0));
        root.setId("readingbulletin-container");
        root.setAlignment(Pos.CENTER);
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
            if(Main.HEIGHT < 700){
                this.image.setFitWidth(50);
                this.image.setFitHeight(50);
            }
            if(Main.HEIGHT < 1200){
                this.image.setFitWidth(100);
                this.image.setFitHeight(100);
            }
            
            VBox imageContainer = new VBox();
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.getChildren().add(this.image);
            
            this.title = new Text(title);
            this.title.getStyleClass().add("home-title");
            
            this.desc = new Label(desc);
            this.desc.getStyleClass().add("home-desc");
            this.desc.setWrapText(true);
            
            VBox textContainer = new VBox();
            textContainer.setSpacing(Main.HEIGHT > 700 ? 10 : 5);
            textContainer.setAlignment(Pos.CENTER_LEFT);
            textContainer.getChildren().addAll(this.title,this.desc);
            
            container = new HBox();
            container.getChildren().addAll(imageContainer,textContainer);
            
            if(Main.HEIGHT < 700){
                container.setSpacing(10);
            }else if(Main.HEIGHT < 1200){
                container.setSpacing(15);
            }else{
                container.setSpacing(20);
            }
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
