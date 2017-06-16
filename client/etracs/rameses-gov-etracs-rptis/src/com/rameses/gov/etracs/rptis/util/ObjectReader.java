/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rptis.util;

import com.rameses.util.Base64Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;


public class ObjectReader 
{
    private File file;
    private Reader reader;
    private BufferedReader br;
    
    public ObjectReader(File file) throws Exception{
        this.file = file;
        reader = new FileReader(file);
        br = new BufferedReader(reader);
    }
    
    public Object readObject() throws Exception{
        String s = br.readLine();
        if (s == null) 
            return null;
        return toObject(s);
    }
    
    public void close() throws Exception{
        if (br != null)
            br.close();
    }
    
    private Object toObject(String base64Str){
        return new Base64Cipher().decode(base64Str);
    }
    
}
