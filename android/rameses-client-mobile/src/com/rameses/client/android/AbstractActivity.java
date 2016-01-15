/*
 * AbstractActivity.java
 *
 * Created on January 31, 2014, 2:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author wflores 
 */
public abstract class AbstractActivity extends Activity 
{
    private final static int TAG_PROPERTY = -972292339;
    
    private Map<Object,View> views;
    private Handler handler;
    
    public AbstractActivity() {
        super();
        
        views = new HashMap();
        init();
    }
    
    protected void init() {}
    
    public boolean isCloseable() { 
        return true; 
    } 
    
    public Handler getHandler() { 
        return handler; 
    }

    protected void onCreateProcess(Bundle savedInstanceState) {}    
    protected void beforeCreate(Bundle savedInstanceState) {}    
    protected final void onCreate(Bundle savedInstanceState) {
        try { 
            dump("onCreate (Bundle)");        
            handler = new Handler();            
            beforeCreate(savedInstanceState);
            super.onCreate(savedInstanceState);
            onCreateProcess(savedInstanceState); 
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        } 
    }

    protected void onResumeProcess() {}    
    protected final void onResume() {
        try { 
            dump("onResume");
            super.onResume(); 

            Platform platform = Platform.getInstance();
            if (platform != null) {
                AbstractActivity aa = platform.find(getClass());
                if (aa == null) {
                    platform.register(this);
                    afterRegister();
                }
            } 

            UIApplication uiapp = Platform.getApplication();
            if (uiapp != null) {
                beforeActivityChanged();
                uiapp.setCurrentActivity(this);
                afterActivityChanged();
            }

            onResumeProcess();
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        } 
    } 
    
    protected void beforeActivityChanged() {}    
    protected void afterActivityChanged() {}    

    protected void afterRegister() {}
    
    protected void onPostCreateProcess(Bundle savedInstanceState) {}     
    protected final void onPostCreate(Bundle savedInstanceState) {
        try { 
            dump("onPostCreate (Bundle)");        
            super.onPostCreate(savedInstanceState);
            onPostCreateProcess(savedInstanceState);
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        } 
    }

    protected void onStopProcess() {}
    protected final void onStop() {
        dump("onstop"); 
        super.onStop();
        onStopProcess();
    }

    protected void onStartProcess() {}
    protected final void onStart() { 
        try { 
            dump("onstart");  
            super.onStart();
            onStartProcess(); 
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        }             
    }

    protected void onRestartProcess() {}    
    protected final void onRestart() { 
        try { 
            dump("onrestart");  
            super.onRestart();
            onRestartProcess(); 
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        }  
    }

    protected void afterAttachedToWindow() {}        
    public final void onAttachedToWindow() {
        dump("onAttachedToWindow");  
        super.onAttachedToWindow();        
        afterAttachedToWindow(); 
    }

    protected boolean beforeFinish() { return true; } 
    protected void afterFinish() {}        
    public final void finish() {
        try { 
            dump("finish");  
            if (!isCloseable()) {
                //this is not closeable
                return;
            } 
            if (beforeFinish()) {
                super.finish(); 
                afterFinish(); 
            } 
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        }              
    }
    
    final void dispose() {
        dump("dispose"); 
        super.finish();
        afterFinish();
    }
    
    protected final void disposeMe() {
        this.dispose(); 
    }
    
    protected boolean beforeBackPressed() { return true; } 
    protected void afterBackPressed() {}            
    public final void onBackPressed() {
        try { 
            dump("onBackPressed");  
            if (beforeBackPressed()) { 
                super.onBackPressed(); 
                afterBackPressed(); 
            } 
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        }
    }

    public void onContentChanged() {
        dump("onContentChanged");  
        super.onContentChanged();
    }

    protected void beforeDestroy() {}  
    protected void afterDestroy() {}  
    protected final void onDestroy() {
        dump("onDestroy");
        beforeDestroy();
        super.onDestroy(); 
        views.clear();
        
        Platform platform = Platform.getInstance();
        if (platform != null) platform.unregister(this); 
        
        UIApplication uiapp = Platform.getApplication(); 
        if (uiapp != null) {
            AbstractActivity current = uiapp.getCurrentActivity();
            if (this.equals(current)) uiapp.setCurrentActivity(null);
        } 
        
        afterDestroy();
    }

    protected void afterDetachedFromWindow() {}  
    public final void onDetachedFromWindow() {
        dump("onDetachedFromWindow");  
        super.onDetachedFromWindow();
        afterDetachedFromWindow();
    }

    protected void onPauseProcess() {}  
    protected final void onPause() {
        try { 
            dump("onPause");  
            super.onPause();
            onPauseProcess();  
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        }
    }

    protected void onPostResumeProcess() {}  
    protected final void onPostResume() {
        try { 
            dump("onPostResume");  
            super.onPostResume(); 
            onPostResumeProcess(); 
        } catch(Throwable t) { 
            Logger logger = Platform.getLogger(); 
            if (logger != null) logger.log(t); 
            
            UIDialog.showError(t, this);
        } 
    }
    
    public void processActivityChanged() {
        //invoked everytime the current activity changed
    }
    
    private void dump(String message) {
        Logger logger = Platform.getLogger();
        if (logger == null) {
            System.out.println("[" + getClass().getName() + "] " + message);    
        } else { 
            logger.log("[" + getClass().getName() + "] " + message); 
        }
    }
    
    
    public View getView(int viewid) {
        View vw = views.get(viewid);
        if (vw == null) {
            vw = findViewById(viewid);
            views.put(viewid, vw);
        }
        return vw;
    }
    
    public EditText getTextField(int viewid) {
        View vw = getView(viewid);
        if (vw instanceof EditText) {
            return (EditText) vw;
        } else {
            return null; 
        }
    }
    
    public Button getButton(int viewid) {
        View vw = getView(viewid);
        if (vw instanceof Button) {
            return (Button) vw;
        } else {
            return null; 
        }
    }
    
    public RadioButton getRadioButton(int viewid) {
        View vw = getView(viewid);
        if (vw instanceof RadioButton) {
            return (RadioButton) vw;
        } else {
            return null; 
        }
    }    
    
    public TextView getLabel(int viewid) {
        View vw = getView(viewid);
        if (vw instanceof TextView) {
            return (TextView) vw;
        } else {
            return null; 
        }
    }    
    
    public void setValue(int viewid, Object value) {
        View view = getView(viewid);
        if (view == null) return;
        
        String text = (value == null? "": value.toString());
        if (view instanceof EditText) {
            ((EditText)view).setText(text); 
        } else if (view instanceof TextView) {
            ((TextView)view).setText(text); 
        } else if (view instanceof Button) {
            ((Button)view).setText(text); 
        } else if (view instanceof RadioButton) {
            ((RadioButton)view).setText(text); 
        }
    }
    
    public Object getValue(int viewid) {
        View view = getView(viewid);
        if (view == null) return null;
                
        Object value = null;
        if (view instanceof EditText) {
            Map props = (Map) view.getTag(TAG_PROPERTY);
            String sval = ((EditText)view).getText().toString();
            value = resolveValue(sval, props);
        } else if (view instanceof TextView) {
            value = ((TextView)view).getText().toString();
        } else if (view instanceof Button) {
            value = ((Button)view).getTag();
        } else if (view instanceof RadioButton) {
            value = ((RadioButton)view).getTag(); 
        } else if (view instanceof RadioGroup) {
            RadioGroup rg = (RadioGroup)view;
            int selId = rg.getCheckedRadioButtonId(); 
            View selView = getView(selId);
            value = (selView == null? null: selView.getTag()); 
        } 
        return value; 
    }
    
    public String getValueAsString(int viewid) {
        Object value = getValue(viewid);
        return (value == null? null: value.toString()); 
    }
    
    public void requestFocus(int viewid) {
        View view = getView(viewid);
        if (view != null) view.requestFocus();
    }
    
    public boolean isEmpty(Object value) {
        if (value == null) { 
            return true; 
        } else if (value instanceof String) { 
            String str = value.toString();
            return (str.length() == 0);
        } else if (value instanceof Object[]) {
            return (((Object[]) value).length == 0); 
        } else if (value instanceof Collection) { 
            return ((Collection)value).isEmpty(); 
        } else {
            return false; 
        } 
    }
    
    public void setProperty(int viewid, String name, Object value) {
        View view = getView(viewid);
        if (view == null) return;
        if (name == null) return;
        
        Map map = (Map) view.getTag(TAG_PROPERTY);
        if (map == null) { 
            map = new Hashtable(); 
            view.setTag(TAG_PROPERTY, map); 
        }
        map.put(name, value); 
    }
    
    private Object resolveValue(String value, Map props) {
        if (value == null || props == null) return value;
        
        String datatype = (props.get("datatype")+"").toLowerCase();
        if ("integer".equals(datatype)) {
            try { 
                return Integer.parseInt(value); 
            } catch(Throwable t) {;} 
            
        } else if ("double".equals(datatype)) {
            try { 
                return Double.parseDouble(value); 
            } catch(Throwable t) {;} 
            
        } else if ("float".equals(datatype)) {
            try { 
                return Float.parseFloat(value); 
            } catch(Throwable t) {;} 
            
        } else if ("decimal".equals(datatype)) {
            try { 
                return new BigDecimal(value.toString()); 
            } catch(Throwable t) {;} 
            
        } else if ("boolean".equals(datatype)) {
            if ("true".equals(value)) {
                return true;
            } else if ("false".equals(value)) {
                return false;
            }
        } 
        return value; 
    } 
    
    public Object getValue(Map data, Object key) {
        return (data == null? null: data.get(key));
    }
    
    public String getValueAsString(Map data, Object key) {
        Object value = (data == null? null: data.get(key));
        return (value == null? null: value.toString()); 
    }
    
    public Integer getValueAsInteger(Map data, Object key) {
        Object value = (data == null? null: data.get(key));
        try { 
            return new Integer(Integer.parseInt(value.toString())); 
        } catch(Throwable t) { 
            return null; 
        } 
    }    
    
    public Long getValueAsLong(Map data, Object key) {
        Object value = (data == null? null: data.get(key));
        try { 
            return new Long(Long.parseLong(value.toString())); 
        } catch(Throwable t) { 
            return null; 
        } 
    }    
    
    public Double getValueAsDouble(Map data, Object key) {
        Object value = (data == null? null: data.get(key));
        try { 
            return new Double(Double.parseDouble(value.toString())); 
        } catch(Throwable t) { 
            return null; 
        } 
    } 
    
}
