/*
 * EntityUtil.java
 *
 * Created on May 17, 2013, 2:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 * This will transfer field names starting with :
 * separated with underscore _
 * into embedded map objects. For example:
 * :permitee_name, :permitee_address
 *
 */
public final class EntityUtil {
    
    public static Map fieldToMap( Map source  ) {
        return fieldToMap(source, false);
    }
    
    public static Map fieldToMap( Map source, boolean prefixed  ) {
        Map target = new LinkedHashMap();
        Map<String, Map> embeddedFields = new LinkedHashMap();
        for(Object o: source.entrySet() ) {
            Map.Entry me = (Map.Entry)o;
            String key = me.getKey().toString();
            boolean _process = false;
            if( prefixed && key.startsWith(":")) {
                _process = true;
            } else if( key.indexOf("_") > 0 && !key.endsWith("_") ) {
                _process = true;
            }
            if(_process) {
                if(key.startsWith(":")) key = key.substring(1);
                
                //int pos = key.indexOf("_");
                String arr[] = key.split("_");
                Map c = embeddedFields;
                for(int i=0; i<(arr.length-1);i++) {
                    String kf = arr[i];
                    Map inner = (Map)c.get(kf);
                    if(inner == null ) {
                        inner = new LinkedHashMap();
                        c.put( kf, inner );
                    }
                    c = inner;
                }
                String lastField = arr[arr.length-1];
                c.put( lastField, me.getValue() );
            } else {
                target.put( me.getKey(), me.getValue() );
            }
        }
        //apply the embedded fields
        for(Object m: embeddedFields.entrySet()) {
            Map.Entry me = (Map.Entry)m;
            target.put( me.getKey(), me.getValue() );
        }
        return target;
    }
    
    /*************************************************************************
     * UPDATE OR INSERT TO DATABASE
     *************************************************************************/
    public static Map mapToField( Map source) {
        return mapToField(source,null,false);
    }
    public static Map mapToField( Map source, boolean includePrefix) {
        return mapToField(source,null, includePrefix);
    }
     public static Map mapToField( Map source, String excludeFields) {
        return mapToField(source,excludeFields, false);
    }
    private static void collectData(String parentField, String fieldName, Object d, Map target, boolean includePrefix ) {
        if(d instanceof Map) {
            if( parentField == null )
                parentField = fieldName;
            else
                parentField = parentField +"_" + fieldName;
            for(Object o: ((Map)d).entrySet() ) {
                Map.Entry me = (Map.Entry)o;
                collectData( parentField , me.getKey().toString(), me.getValue(), target, includePrefix );
            }
        } else {
            if(parentField!=null)
                fieldName = parentField+"_"+fieldName;
            if(includePrefix)
                fieldName = ":"+fieldName;
            target.put(fieldName, d );
        }
    }
    
    public static Map mapToField( Map source, String excludeFields, boolean includePrefix ) {
        Map target = new LinkedHashMap();
        for(Object o: source.entrySet() ) {
            Map.Entry me = (Map.Entry)o;
            if( excludeFields!=null && me.getKey().toString().matches(excludeFields) ) {
                target.put(me.getKey(), me.getValue());
            } else if( me.getValue() instanceof Map) {
                collectData( null, me.getKey()+"", me.getValue(), target, includePrefix );
            } else {
                target.put(me.getKey(), me.getValue());
            }
        }
        return target;
    }
    
}
