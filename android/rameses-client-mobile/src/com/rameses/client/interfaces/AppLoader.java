/*
 * AppLoader.java
 *
 * Created on January 22, 2014, 1:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.interfaces;

/**
 *
 * @author wflores
 */
public interface AppLoader 
{
    void setCaller(AppLoaderCaller caller); 
    
    int getIndex();
    void load(); 
}
