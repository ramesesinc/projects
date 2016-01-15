/*
 * UIAction.java
 *
 * Created on February 1, 2014, 3:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 *
 * @author wflores
 */
public class UIAction 
{
    private EventHandler handler;
    private View view;
    
    public UIAction(Activity activity, int viewid) { 
        view = activity.findViewById(viewid);
        if (view instanceof Button) {
            Button btn = (Button)view;
            initButton(btn);
        } else {
            System.out.println("viewid " + viewid + " is not a button ("+view+")");
        }
    }
    
    public View getView() { return view; } 
    
    private EventHandler getEventHandler() {
        if (handler == null) { 
            handler = new EventHandler(); 
        } 
        return handler; 
    }
    
    private void initButton(Button btn) { 
        EventHandler handler = getEventHandler();
        btn.setOnClickListener(handler); 
    } 
    
    protected void onClick(View view) {
        onClick(); 
    } 
    
    protected void onClick() {
    }
    
    protected boolean onLongClick(View view) {
        return onLongClick(); 
    } 
    
    protected boolean onLongClick() {
        return true; 
    }        
    
    protected boolean onKey(View view, int i, KeyEvent keyEvent) {
        return onKey(view, i);
    }
    
    protected boolean onKey(View view, int i) {
        return onKey(view);
    }
    
    protected boolean onKey(View view) {
        return onKey(); 
    }
    
    protected boolean onKey() { 
        return true; 
    } 
    
    protected boolean onTouch(View view, MotionEvent motionEvent) {
        return onTouch(view);
    }    
    
    protected boolean onTouch(View view) {
        return onTouch(); 
    } 
    
    protected boolean onTouch() {
        return true; 
    } 
    
    
    private class EventHandler implements View.OnClickListener, 
        View.OnKeyListener, View.OnLongClickListener, View.OnTouchListener  
    {
        UIAction root = UIAction.this; 
        
        public void onClick(View view) {
            root.onClick(view); 
        }

        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            return root.onKey(view, i, keyEvent);
        }

        public boolean onLongClick(View view) {
            return root.onLongClick(view); 
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            return root.onTouch(view, motionEvent); 
        }
    }     
}
