/*
 * StringHttpClientOutputHandler.java
 *
 * Created on February 27, 2013, 10:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.http;

import java.io.InputStream;

/**
 *
 * @author Elmo
 */
public class StringHttpClientOutputHandler implements HttpClientOutputHandler {
    
    public Object getResult(InputStream is) {
        try {
            StringBuilder b = new StringBuilder();
            int i = 0;
            while((i=is.read())!=-1) {
                b.append((char)i);
            }
            return b.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
