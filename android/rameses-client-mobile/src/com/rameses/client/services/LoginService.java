/*
 * LoginService.java
 *
 * Created on January 22, 2014, 11:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.services;

import android.content.SharedPreferences;
import android.util.Log;
import com.rameses.client.android.AppContext;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIApplication;
import com.rameses.util.Base64Cipher;
import com.rameses.util.Encoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author wflores
 */
public class LoginService extends AbstractService 
{
    private final static String SHARED_NAME = "com.rameses.client.userprofile";
    
    private synchronized SharedPreferences getPrefs() {
        UIApplication uiapp = Platform.getApplication();
        return uiapp.getSharedPreferences(SHARED_NAME, 0);
    }    
    
    public String getServiceName() { return "LoginService"; }
    
    private Map result;
    public Map getResult() { return result; }

    private String encpwd;
    public String getEncpwd() { return encpwd; }
    
    public void login(String username, String password) throws Exception {
        encpwd = Encoder.MD5.encode(password, username); 
        Map param = new HashMap();
        param.put("username", username);
        param.put("password", encpwd);
        
        Exception error = null;
        Map xresult = null; 
        try { 
            xresult = (Map) invoke("login", param);
        } catch(Exception e) {
            error = e; 
            //throw e;
        }
        
        if (error == null) {
            Map profile = new HashMap();
            profile.putAll( param ); 
            profile.put("data", xresult); 
            saveProfile( profile );
        } else {
            boolean isConnectionError = isConnectionError(error);
            println("is connection error " + isConnectionError);
            if (isConnectionError) {
                Map profile = readProfile(); 
                validateProfile(username, encpwd, profile, error); 
                xresult = profile; 
                xresult.remove("AUTH_OPTIONS"); 
            } else {
                println("profile " + readProfile());
                throw error;
            }
        }
        
        println("error " + error);
        
        result = new HashMap();
        result.putAll(xresult);
        
        println("result " + xresult);
        SessionProviderImpl sessImpl = new SessionProviderImpl(xresult);
        SessionContext sess = AppContext.getSession();
        sess.setProvider(sessImpl); 
        sess.set("encpwd", encpwd); 
        
        Map authOpts = (Map) xresult.remove("AUTH_OPTIONS");
        //println("authopts " + authOpts);
        if (authOpts != null) {
            Iterator keys = authOpts.keySet().iterator(); 
            while (keys.hasNext()) { 
                String key = keys.next().toString(); 
                //println("key: " + key);
                sess.set(key, authOpts.get(key)); 
            } 
        } 
    } 
    
    private void println(String str) {
        Log.i("LoginService", str);
    }
    
    private boolean isConnectionError(Exception e) {
        if (e instanceof java.net.ConnectException) return true; 
        else if (e instanceof java.net.NoRouteToHostException) return true; 
        else if (e instanceof java.net.SocketException) return true; 
        else if (e instanceof java.net.SocketTimeoutException) return true; 
        else if (e instanceof java.net.UnknownHostException) return true; 

        return false; 
    }
    
    private void saveProfile( Map profile ) {
        String encstr = new Base64Cipher().encode( profile ); 
        
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(".profile", encstr);
        editor.commit(); 
    }
    
    private Map readProfile() {
        String encstr = getPrefs().getString(".profile", ""); 
        if ( encstr == null || encstr.length() == 0) return null; 
        
        Object o = new Base64Cipher().decode(encstr); 
        if (o instanceof Map) return (Map)o; 
        
        return null; 
    }
    
    private void validateProfile(String username, String pwd, Map profile, Exception error) {
        if (profile == null) {
            if (error instanceof RuntimeException) {
                throw (RuntimeException)error; 
            } else if (error instanceof Exception) {
                throw new RuntimeException(error.getMessage(), error); 
            }
        }
        
        String vname = (profile==null? null: (String)profile.get("username"));
        String vpwd = (profile==null? null: (String)profile.get("password"));
        boolean pass = false; 
        if (vname != null && vname.equalsIgnoreCase(username)) {
            if (vpwd != null && vpwd.equalsIgnoreCase(pwd)) {
                pass = true;
            }
        } 
        
        if ( !pass ) {
            throw new RuntimeException("Account is invalid. Please check your username and password");
        }
    }
}
