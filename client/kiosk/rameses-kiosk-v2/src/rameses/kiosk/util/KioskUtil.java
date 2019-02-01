/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.kiosk.util;

import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author louie
 */
public class KioskUtil {
    
    public static String convertToJSONObject( Map data ) {
        String str = "{";
        
        if (data != null && !data.isEmpty()) {
            Iterator itr = data.keySet().iterator();
            Object obj = null, prevobj = null;
            while (itr.hasNext()) {
                obj = itr.next();
                if (prevobj != null) {
                    str += ",";
                }
                if (obj != null) {
                    str += obj.toString() + ":";
                    if (data.containsKey( obj.toString() )) {
                        Object val = data.get( obj.toString() );
                        if (val instanceof Number) {
                            str += val.toString();
                        } else {
                            str += "'" + val.toString() + "'";
                        }
                    } else {
                        str += "null";
                    }
                }
                    
                prevobj = obj;
            }
        }
        
        str += "}";
        return str;
    }
    
}
