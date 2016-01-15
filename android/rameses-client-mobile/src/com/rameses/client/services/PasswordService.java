/*
 * PasswordService.java
 *
 * Created on January 23, 2014, 1:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.services;

import com.rameses.client.android.AppContext;
import com.rameses.client.android.SessionContext;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.util.Encoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflores
 */
public class PasswordService extends AbstractService 
{
    public String getServiceName() {
        return "PasswordService"; 
    } 

    public void changePassword(String oldpwd, String newpwd, String confirmpwd) {
        if (newpwd == null || newpwd.length() == 0) 
            throw new RuntimeException("Please provide a new password");
        if (!newpwd.equals(confirmpwd)) 
            throw new RuntimeException("New password and Confirm password must be the same");
        
        SessionContext sess = AppContext.getSession();
        UserProfile profile = sess.getProfile();
        String username  = profile.getUserName();
        String encCurPwd = sess.getPassword();
        String encOldPwd = (oldpwd == null? encCurPwd: Encoder.MD5.encode(oldpwd, username));

        if (!encOldPwd.equals(encCurPwd)) 
            throw new RuntimeException("Old password is incorrect"); 

        String encNewPwd = Encoder.MD5.encode(newpwd, username);
        if (encNewPwd.equals(encOldPwd)) 
            throw new RuntimeException("New password and Old password must not be the same"); 
        
        Map param = new HashMap();
        param.put("username", username);
        param.put("newpassword", encNewPwd);
        param.put("oldpassword", encOldPwd);
        invoke("changePassword", param);
        sess.set("encpwd", encNewPwd); 
    }    
}
