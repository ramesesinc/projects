package com.rameses.client.ui.fx;

import com.rameses.common.PropertyResolver;
import com.rameses.rcp.common.PropertySupport.PropertyInfo;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class XGoogleMap extends JFXPanel implements UIControl {

    private JFXPanel fxpanel;
    private WebView webView;
    private WebEngine webEngine;
    private ProgressIndicator indicator;
    private StackPane root;
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String visibleWhen;
    private int stretchWidth;
    private int stretchHeight;
    private String name;
    private int width, height;
    
    private String handler;
    private GoogleMapModel googleMapModel;
    
    String latitude = "", longitude = "", addresstext = "";
    
    public XGoogleMap() {
        initComponents();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    void createScene(){
        fxpanel = this;
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                webView = new WebView();
                webView.widthProperty().addListener(new ChangeListener<Number>(){
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                        width = t1.intValue();
                        if(webEngine != null && webEngine.getLoadWorker().getState().equals(Worker.State.SUCCEEDED)){
                            webEngine.executeScript("setWidth("+width+")");
                            webEngine.executeScript("moveToCenter()");
                        }
                    }
                });
                webView.heightProperty().addListener(new ChangeListener<Number>(){
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                        height = t1.intValue();
                        if(webEngine != null && webEngine.getLoadWorker().getState().equals(Worker.State.SUCCEEDED)){
                            webEngine.executeScript("setHeight("+height+")");
                            webEngine.executeScript("moveToCenter()");
                        }
                    }
                });
                webView.setOnMouseReleased(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent t) {
                        latitude = String.valueOf(webEngine.executeScript("latitude"));
                        longitude = String.valueOf(webEngine.executeScript("longitude"));
                        addresstext = (String) webEngine.executeScript("address");
                        
                        Map m = new HashMap();
                        m.put("latitude", latitude);
                        m.put("longitude", longitude);
                        googleMapModel.onLocationSelected(m);
                    }
                });

                webEngine = webView.getEngine();
                webEngine.setJavaScriptEnabled(true);
                webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>(){
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                        if(t1.equals(Worker.State.SUCCEEDED)){
                            root.getChildren().remove(indicator);
                            webEngine.executeScript("setWidth("+width+")");
                            webEngine.executeScript("setHeight("+height+")");
                            webEngine.executeScript("moveToCenter()");  
                            Map property = googleMapModel.getProperty();
                            loadMapProperty(property);
                        }
                    }
                });
                
                String url = getClass().getResource("GoogleMap.html").toExternalForm();
                webEngine.load(url);

                indicator = new ProgressIndicator();
                indicator.setMaxSize(40, 40);

                root = new StackPane();
                root.setAlignment(Pos.CENTER);
                root.getChildren().add(webView);
                root.getChildren().add(indicator);

                Scene scene = new Scene(root, 550, 400);
                fxpanel.setScene(scene);
            }
            
        });
    }
    
    void loadMapProperty(Map property){
        final double latitude = property.get("latitude") != null ? Double.parseDouble(property.get("latitude").toString()) : 0.00;
        final double longitude = property.get("longitude") != null ? Double.parseDouble(property.get("longitude").toString()) : 0.00;
        final int zoom = property.get("zoom") != null ? Integer.parseInt(property.get("zoom").toString()) : 0;
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                if(webEngine != null && webEngine.getLoadWorker().getState().equals(Worker.State.SUCCEEDED)){
                    if(zoom != 0) webEngine.executeScript("map.setZoom("+zoom+")");
                    if(latitude != 0.00 || longitude != 0.00) webEngine.executeScript("center = new google.maps.LatLng("+latitude+","+longitude+")");
                    webEngine.executeScript("moveToCenter()");
                }
            }
        });
    }
    
    @Override
    public Binding getBinding() {
        return binding;
    }

    @Override
    public void setBinding(Binding bndng) {
        binding = bndng;
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

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void load() {
        Object o = PropertyResolver.getInstance().getProperty( binding.getBean(), handler);
        if(!(o instanceof GoogleMapModel) )  {
            throw new RuntimeException("Handler " + handler + " must be a google map model!");
        }
        googleMapModel = (GoogleMapModel)o;
        createScene();
    }

    @Override
    public int getStretchWidth() {
        return stretchWidth;
    }

    @Override
    public void setStretchWidth(int i) {
        stretchWidth = i;
    }

    @Override
    public int getStretchHeight() {
        return stretchHeight;
    }

    @Override
    public void setStretchHeight(int i) {
        stretchHeight = i;
    }

    @Override
    public void refresh() {
        Map property = googleMapModel.getProperty();
        loadMapProperty(property);
    }
    
    public String getVisibleWhen(){
        return visibleWhen;
    }
    
    public void setVisibleWhen(String s){
        this.visibleWhen = s;
    }

    @Override
    public void setPropertyInfo(PropertyInfo pi) {
        
    }

    @Override
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

}
