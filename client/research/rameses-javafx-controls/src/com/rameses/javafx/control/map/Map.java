/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.javafx.control.map;

import com.rameses.rcp.common.PropertySupport.PropertyInfo;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import resources.Resource;
/**
 *
 * @author Dino Quimson
 */
public class Map extends JFXPanel implements UIControl{

    //rameses classes
    private Binding binding;
    private String[] depends;
    private int index;
    private String visibleWhen;
    private int stretchWidth;
    private int stretchHeight;
    private String mode;
    private String name;
    private int sceneWidth, sceneHeight;
    private boolean loaded = false;
    private boolean pinned = false;
    
    //javafx classes
    private JFXPanel fxpanel;
    private WebView webview;
    private WebEngine webengine;
    private BorderPane root;
    
    private static String latlng;
    
    public Map() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void createScene(){
        fxpanel = this;
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                webview = new WebView();
                webview.setId("webview");
                webview.setContextMenuEnabled(false);
                if(sceneWidth != 0) webview.setPrefWidth(sceneWidth);
                if(sceneHeight != 0) webview.setPrefHeight(sceneHeight);
                //get the data from html
                webview.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent t) {
                        if(loaded){
                            pinned = true;
                        }
                    }
                });
                webview.setOnMouseMoved(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent t) {
                        if(loaded && pinned){
                            String latitude = String.valueOf((double)webengine.executeScript("latitude"));
                            String longitude = String.valueOf((double)webengine.executeScript("longitude"));
                            String location = (String) webengine.executeScript("addresstext");
                            //setting the bean value
                            java.util.Map value = new HashMap();
                            value.put("latitude", latitude);
                            value.put("longitude", longitude);
                            value.put("address", location);
                            UIControlUtil.setBeanValue(binding, name, value);
                        }
                    }
                });
                
                String url = Resource.class.getResource("html/map.html").toExternalForm();
                webengine = webview.getEngine();
                webengine.load(url);
                webengine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>(){
                    @Override
                    public void changed(ObservableValue<? extends State> ov, State t, State t1) {
                        if(t1 == State.SUCCEEDED){
                            loaded = true;
                            //setting the height of the map
                            String height = "$('#map').css('height',\'"+sceneHeight+"px\');";
                            if(sceneHeight != 0) webengine.executeScript(height);
                            if(mode != null){
                                if(mode.equals("read")) webview.setDisable(true);
                            }
                            
                            //initializing the location
                            Object val = UIControlUtil.getBeanValue(binding, name);
                            if(val != null){
                                HashMap value = (HashMap) val;
                                String latitude = value.get("latitude").toString();
                                String longitude = value.get("longitude").toString();
                                String address = value.get("address").toString();
                                //set the marker location;
                                webengine.executeScript("map.panTo(new google.maps.LatLng("+latitude+","+longitude+"));");
                                webengine.executeScript("marker = new google.maps.Marker({position:new google.maps.LatLng("+latitude+","+longitude+")});");
                                webengine.executeScript("marker.setMap(map);");
                                webengine.executeScript("info.setContent('<b>"+address+"</b>');");
                                webengine.executeScript("info.open(map,marker);");
                            }
                        }
                    }
                });

                root = new BorderPane();
                root.setPadding(new Insets(0, 2, 0, 1));
                root.setCenter(webview);
                
                Scene scene = new Scene(root);
                scene.getStylesheets().add(Resource.class.getResource("css/map.css").toExternalForm());
                fxpanel.setScene(scene);
            }
        });
    }
    
    @Override
    public Binding getBinding() {
        return binding;
    }

    @Override
    public void setBinding(Binding bndng) {
        this.binding = bndng;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public void setName(String s){
        this.name = s;
    }

    @Override
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] s){
        this.depends = s;
    }

    @Override
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int i){
        this.index = i;
    }

    @Override
    public void load() {
        createScene();
    }

    @Override
    public void refresh() {
        
    }

    @Override
    public void setPropertyInfo(PropertyInfo pi) {
        
    }

    @Override
    public int getStretchWidth() {
        return stretchWidth;
    }

    @Override
    public void setStretchWidth(int i) {
        this.stretchWidth = i;
    }

    @Override
    public int getStretchHeight() {
        return stretchHeight;
    }

    @Override
    public void setStretchHeight(int i) {
        this.stretchHeight = i;
    }

    @Override
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public String getVisibleWhen(){
        return visibleWhen;
    }
    
    public void setVisibleWhen(String s){
        this.visibleWhen = s;
    }
    
    public String getMode(){
        return mode;
    }
    
    public void setMode(String m){
        this.mode = m;
    }
    
    public int getSceneWidth(){
        return sceneWidth;
    }
    
    public void setSceneWidth(int i){
        sceneWidth = i;
    }
    
    public int getSceneHeight(){
        return sceneHeight;
    }
    
    public void setSceneHeight(int i){
        sceneHeight = i;
    }
}
