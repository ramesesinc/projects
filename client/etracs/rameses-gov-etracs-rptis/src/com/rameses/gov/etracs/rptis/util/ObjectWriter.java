/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rptis.util;

import com.rameses.util.Base64Cipher;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;


public class ObjectWriter 
{
    private File file;
    private Writer writer;
    
    public ObjectWriter(File file) throws Exception{
        this.file = file;
        if (file.exists()){
            file.delete();
            file.createNewFile();
        }
        writer = new FileWriter(file, true);
    }
    
    public void writeObject(Object obj) throws Exception{
        String base64Str = toBase64(obj);
        writer.write(base64Str);
    }
    
    public void close() throws Exception{
        if (writer != null){
            writer.close();
            writer = null;
        }
    }
    
    public void cancel() throws Exception{
        close();
        if (file.exists()){
            System.out.println("deleting...");
            System.out.println(file.delete());
        }
    }
    
    private String toBase64(Object obj){
        String s = new Base64Cipher().encode(obj);
        s += "\n";
        return s;
    }
    
    
}
