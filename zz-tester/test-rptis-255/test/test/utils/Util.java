/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;


public class Util {
    
    private Util(){}
    
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch(Exception ex) {
            //ignore 
        }
    }

       
}
