/*
 * ConfigProperties.java
 *
 * Created on March 1, 2013, 2:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class ConfigProperties {
    
    private Map map = new LinkedHashMap();
    //private List<String,  gMap> groups = new ArrayList();
    private Map<String,Map> groups = new LinkedHashMap();
    private File file;
    private boolean updatable = true;
    
    /** Creates a new instance of ConfigProperties */
    public ConfigProperties(File sfile ) {
        BufferedReader reader = null;
        try {
            this.file = sfile;
            if( file.getParentFile()!=null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            reader = new BufferedReader(new FileReader(file));
            load( reader );
        } 
        catch(RuntimeException re) {
            throw re;
        } 
        catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } 
        finally {
            try { reader.close(); } catch(Exception e){;}
        }
    }
    
    public ConfigProperties(String filename) {
        this( new File(filename));
    }
    
    public ConfigProperties(InputStream is) {
        try {
            updatable = false;
            load( new BufferedReader(new InputStreamReader(is)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    public ConfigProperties(URL u) {
        InputStream is = null;
        try {
            updatable = false;
            is = u.openStream();
            load( new BufferedReader(new InputStreamReader(is)));
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    private void load( BufferedReader reader ) throws Exception {
        map = new LinkedHashMap();
        String s = null;
        Map groupMap = null;
        while( (s=reader.readLine())!=null ) {
            s = s.trim();
            if(s.length()==0) continue;
            if( s.startsWith("#")) continue;
            if( s.startsWith("[") ) {
                String groupName = s.substring(1, s.lastIndexOf("]"));
                groupMap = new LinkedHashMap();
                groups.put( groupName.trim(), groupMap );
                continue;
            }
            String name = s.substring(0, s.indexOf("=")).trim();
            String value = s.substring( s.indexOf("=")+1 ).trim();
            if( groupMap!=null ) {
                groupMap.put(name, value );
            } else {
                map.put( name, value );
            }
        }
    }
    
    public Object getProperty(String name) {
        if( name.indexOf(":")>0) {
            String groupName = name.substring( 0, name.indexOf(":") );
            String propName = name.substring( name.indexOf(":")+1 );
            return groups.get( groupName ).get(propName);
        } else {
            return map.get(name);
        }
    }
    
    /**
     *specify a group name and return properties for that group
     */
    public Map getProperties(String groupName) {
        return groups.get(groupName);
    }
    
    public Map getProperties()  {
        return map;
    }
    
    public void put(String name, String data) {
        setProperty(name.trim(), data.trim());
    }
    
    public String get(String name) {
        return (String) getProperty(name);
    }
    
    public void setProperty(String name, String data) {
        if( name.indexOf(":")>0) {
            String groupName = name.substring( 0, name.indexOf(":") );
            String propName = name.substring( name.indexOf(":")+1 );
            groups.get( groupName ).put(propName, data);
        } else {
            map.put(name.trim(), data.trim());
        }
    }
    
    public void update(){
        StringBuilder sb = new StringBuilder();
        for(Object o: map.keySet() ) {
            String skey = (String)o;
            sb.append( skey +"="+map.get(skey) +"\n" );
        }
        sb.append("\n\n");
        for( Object g: groups.keySet() ) {
            sb.append( "[" + g + "]\n");
            Map gm = groups.get(g);
            for( Object k: gm.keySet()) {
                String gkey = (String)k;
                sb.append( gkey + "=" + gm.get(gkey) + "\n" );
            }
            sb.append("\n\n");
        }
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream( file );
            fos.write( sb.toString().getBytes() );
        } 
        catch(RuntimeException re) {
            throw re;
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } 
        finally {
            try { fos.close(); } catch(Exception e) {;}
        }
    }
    
    public Map<String, Map> getGroups() {
        return groups;
    }
    
    public void putAll( Map map ) {
        for(Object o: map.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            put( me.getKey()+"", me.getValue()+"");
        }
    }
    
    public boolean isUpdatable() {
        return updatable;
    }
    
}
