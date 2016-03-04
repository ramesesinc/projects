package com.rameses.waterworks.page;

import com.rameses.Main;
import com.rameses.waterworks.database.Database;
import com.rameses.waterworks.database.DatabasePlatformFactory;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.layout.Header;
import com.rameses.waterworks.service.LoginService;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Login {
    
    private GridPane grid;
    private TextField username;
    private PasswordField password;
    private Button login;
    private CheckBox offline;
    private Map account = null;
    
    public Login(){
        Header.TITLE.setText("User Login");
        
        ImageView image = new ImageView(new Image("icon/userlogin.png"));
        
        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getChildren().add(image);
        
        username = new TextField();
        username.getStyleClass().add("login-field");
        username.setPrefWidth(300);
        username.setFocusTraversable(false);
        
        password = new PasswordField();
        password.getStyleClass().add("login-field");
        password.setPrefWidth(300);
        password.setFocusTraversable(false);
        
        Label username_lb = new Label("Username");
        username_lb.getStyleClass().add("login-label");
        
        Label password_lb = new Label("Password");
        password_lb.getStyleClass().add("login-label");
        
        offline = new CheckBox("OFFLINE");
        offline.getStyleClass().add("login-label");
        
        login = new Button("Login");
        login.setId("login-button");
        login.setFocusTraversable(true);
        login.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        String usrname = username.getText();
                        String pwd = password.getText();
                        if(usrname.isEmpty() || pwd.isEmpty()) return;

                        Map parameter = new HashMap();
                        parameter.put("username", usrname);
                        parameter.put("password", pwd);

                        account = null;

                        if(!offline.isSelected()){
                            onlineLogin(parameter);
                        }else{
                            offlineLogin();
                        }
                    }
                };
                t.start();
            }
        });
        
        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.getChildren().addAll(offline, login);
        
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        if(Main.HEIGHT > 800){
            grid.setPadding(new Insets(50, 50, 50, 50));
        }else{
            grid.setPadding(new Insets(15, 15, 15, 15));
        }
        grid.setVgap(10);
        grid.setHgap(10);
        
        grid.add(imageContainer, 0, 0, 2, 1);
        grid.add(username_lb, 0, 1);
        grid.add(username, 1, 1);
        grid.add(password_lb, 0, 2);
        grid.add(password, 1, 2);
        grid.add(buttonContainer, 1, 3);
    }
    
    private void onlineLogin(Map parameter){
        LoginService service = new LoginService();
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                Dialog.wait("Please wait ...");
            }
        });
        account = service.login(parameter);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                Dialog.hide();
            }
        });
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if(!service.ERROR.isEmpty()){
                    Dialog.showError(service.ERROR);
                }
                if(account != null){
                    Main.MYACCOUNT = account;
                    Header.useraccount.setVisible(true);
                    Main.ROOT.setCenter(new Home().getLayout());
                }else{
                    return;
                }
                com.rameses.waterworks.bean.Setting userid_setting = new com.rameses.waterworks.bean.Setting("userid",SystemPlatformFactory.getPlatform().getSystem().getUserID());
                com.rameses.waterworks.bean.Setting username_setting = new com.rameses.waterworks.bean.Setting("username",parameter.get("username").toString());
                com.rameses.waterworks.bean.Setting password_setting = new com.rameses.waterworks.bean.Setting("password",parameter.get("password").toString()); 
                com.rameses.waterworks.bean.Setting name_setting = new com.rameses.waterworks.bean.Setting("name",SystemPlatformFactory.getPlatform().getSystem().getFullName()); 

                //STORE THE USERNAME AND PASSWORD IN THE DATABASE  -- USE FOR OFFLINE LOGIN
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                if(!db.settingExist(username_setting)){
                    db.createSetting(username_setting);
                    if(!db.settingExist(userid_setting)){
                        db.createSetting(userid_setting);
                    }else{
                        db.updateSetting(userid_setting);
                    }
                    if(!db.settingExist(password_setting)){
                        db.createSetting(password_setting);
                    }else{
                        db.updateSetting(password_setting);
                    }
                    if(!db.settingExist(name_setting)){
                        db.createSetting(name_setting);
                    }else{
                        db.updateSetting(name_setting);
                    }
                }else{
                    db.updateSetting(username_setting);
                }
            }
        });
    }
    
    private void offlineLogin(){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                String userid = null, uname = null, pword = null, name = null;
                Database db = DatabasePlatformFactory.getPlatform().getDatabase();
                List<com.rameses.waterworks.bean.Setting> settings = db.getAllSettings();
                Iterator<com.rameses.waterworks.bean.Setting> i = settings.iterator();
                while(i.hasNext()){
                    com.rameses.waterworks.bean.Setting setting = i.next();
                    if(setting.getName().equals("userid")) userid = setting.getValue();
                    if(setting.getName().equals("username")) uname = setting.getValue();
                    if(setting.getName().equals("password")) pword = setting.getValue();
                    if(setting.getName().equals("name")) name = setting.getValue();
                }
                if(userid == null){
                    Dialog.showAlert("User account does not exist!");
                    return;
                }
                if(userid!=null && uname!=null && pword!=null && name!=null){
                    Map env = new HashMap();
                    env.put("FULLNAME", name);

                    account = new HashMap();
                    account.put("USERID", userid);
                    account.put("username", uname);
                    account.put("password", pword);
                    account.put("env", env);
                }
                if(!uname.equals(username.getText())){
                    Dialog.showAlert("Invalid username or password.");
                    return;
                }
                if(!pword.equals(password.getText())){
                    Dialog.showAlert("Invalid username or password.");
                    return;
                }
                if(account == null) Dialog.showAlert("Offline account cannot be found!");
                if(account != null){
                    Main.MYACCOUNT = account;
                    Header.useraccount.setVisible(true);
                    Main.ROOT.setCenter(new Home().getLayout());
                }else{
                    Dialog.showError("Invalid username or password.");
                }
            }

        });
    }
    
    public Node getLayout(){
        return grid;
    }
    
}
