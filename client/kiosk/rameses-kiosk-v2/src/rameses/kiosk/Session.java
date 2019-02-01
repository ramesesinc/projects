/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.kiosk;

import java.util.Map;

/**
 *
 * @author louie
 */
public class Session {
    
    private static Session instance;
    
    private Map userData;
    
    private Session() {}
    
    public static Session getSession() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    public Map getUser() { return userData; }
    
    public void setUser( Map userData ) {
        this.userData = userData;
    }
    
    
}
