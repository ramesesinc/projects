/*
 * UIMain.java
 *
 * Created on January 31, 2014, 12:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.content.Intent;
import android.os.Bundle;

/**
 *
 * @author wflores
 */
public class UIMain extends UIActivity 
{
    private UIApplication uiapp;
    
    public final boolean isCloseable() {
        return false; 
    }
    
    protected void init() { 
        uiapp = Platform.getApplication();
        uiapp.setMainActivity(this);
    } 

    protected void onCreateProcess(Bundle savedInstanceState) {
    }
    
    protected void afterRegister() {
    }

    protected void onStopProcess() {
        Platform platform = Platform.getInstance();
        if (platform != null) platform.unregister(this); 
    } 
    
    protected void afterBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }    
    
    void unregister() {
        Platform platform = Platform.getInstance();
        if (platform != null) platform.unregister(this);
    } 
}
