package com.rameses.client.ui.fx;

import java.util.Map;

public interface GoogleMapModel {
    
    Map getProperty();
    
    void onLocationSelected( Map map );
    
}
