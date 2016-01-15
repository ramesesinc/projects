/*
 * Service.java
 *
 * Created on February 1, 2014, 7:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.android;

import android.content.res.XmlResourceParser;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.xmlpull.v1.XmlPullParser;

/**
 *
 * @author wflores
 */
public final class Service 
{
    public static synchronized Iterator providers(Class clazz) {
        try {
            return new Service(clazz).getProviders(); 
        } catch(RuntimeException re) {
            throw re; 
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
        
    private Class clazz;
    
    private Service(Class clazz) {
        this.clazz = clazz;
    }
    
    private Iterator getProviders() throws Exception {
        List list = new ArrayList();
        UIApplication uiapp = Platform.getApplication();
        if (uiapp == null) return list.iterator(); 
        
        int resid = uiapp.getResources().getIdentifier("services", "xml", uiapp.getPackageName()); 
        if (resid == 0) return list.iterator();
        
        XmlResourceParser xml = uiapp.getResources().getXml(resid);
        xml.next();
        
        List<String> impls = null;
        List<String> paths = new ArrayList();
        int eventType = xml.getEventType();        
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                paths.add(xml.getName()); 
                Properties attrs = getAttributes(xml);                
                String s = join(paths, "/");                
                if (s.equals("services/service")) {
                    if (clazz.getName().equals(attrs.getProperty("provider"))) {
                        if (impls == null) impls = new ArrayList(); 
                    }
                } else if (s.equals("services/service/impl")) {
                    String sname = attrs.getProperty("name");
                    if (sname != null && sname.length() > 0 && impls != null) {
                        impls.add(sname); 
                    } 
                }
                attrs.clear();
            } 
            else if (eventType == XmlPullParser.END_TAG) {
                if (!paths.isEmpty()) {
                    paths.remove(paths.size()-1);
                }
            }
            eventType = xml.next();
        }
        
        if (impls != null) {
            while (!impls.isEmpty()) {
                String classname = impls.remove(0);
                try {
                    Class clazz = Class.forName(classname, true, uiapp.getClassLoader()); 
                    list.add(clazz.newInstance()); 
                } catch(Throwable t) {
                    System.out.println("[Service.providers] failed to load " + classname + " caused by " + t.getMessage());
                }
            }
        }
        return list.iterator(); 
    }
    
    private String join(List<String> list, String delim) {
        StringBuffer sb = new StringBuffer();
        for (String str : list) {
            if (sb.length() > 0) sb.append("/"); 
            
            sb.append(str);
        }
        return sb.toString(); 
    }
    
    private Properties getAttributes(XmlResourceParser xml) {
        Properties attrs = new Properties();
        int len = xml.getAttributeCount();
        for (int i=0; i<len; i++) {
            String name = xml.getAttributeName(i);
            String val  = xml.getAttributeValue(i);
            attrs.put(name, val);
        } 
        return attrs; 
    }
}
