/*
 * Task.java
 *
 * Created on January 27, 2014, 5:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import java.util.TimerTask;

/**
 *
 * @author wflores 
 */
public abstract class Task extends TimerTask 
{    
    public abstract void run();    
}
