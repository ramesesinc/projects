package com.rameses.android.efaas.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataBuilder {
	
    private static Map doBuildMap(Map map, Map datamap){
        Set<Map.Entry<String, Object>> entrySet = datamap.entrySet();
        for(Map.Entry<String, Object> entry : entrySet){
            String k = entry.getKey();
            Object v = entry.getValue();
            
            if(k.indexOf("_") < 0){
                map.put(k, v);
            }
            else{
                String newk = k.substring(0, k.indexOf("_"));
                String nextk = k.substring(k.indexOf("_")+1);
                
                if(map.containsKey(newk)){
                    Map emap = (Map) map.get(newk);
                    Map m = new HashMap();
                    m.put(nextk, v);
                    doBuildMap(emap,m);
                }else{
                    Map m = new HashMap();
                    m.put(nextk, v);
                    map.put(newk, doBuildMap(new HashMap(),m));
                }
            }
        }
        return map;
    }
    
    public static Map buildMap(Map datamap){
        Map map = new HashMap();
        doBuildMap(map,datamap);
        return map;
    }

	public static List<Map> buildList(List<Map> list){
        List<Map> data = new ArrayList<Map>();
        for(Map map : list){
            data.add(buildMap(map));
        }
        return data;
	}

}