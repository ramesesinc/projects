package com.rameses.gov.etracs.rptis.models;

import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.Map;


public class Entity {
    public static String ENTITY_001 = "D001";
    public static String ENTITY_002 = "D002";
    
    
    public static Map createEntity(String info) {
        Map addr = new HashMap();
        addr.put("type", "LOCAL");
        addr.put("text", "LOCAL");
        
        Map entity = new HashMap();
        entity.put("_schemaname", "entityindividual");
        entity.put("entityno", info);
        entity.put("lastname", info);
        entity.put("firstname", info);
        entity.put("middlename", info);
        entity.put("gender", "M");
        entity.put("address", addr);
        entity = ServiceUtil.persistence().create(entity);
        return entity;
    }

    public static boolean existEntity(String entityno) {
        Map item = findEntity(entityno);
        return item != null;
    }
    
    public static Map findEntity(String entityno) {
        Map param = new HashMap();
        param.put("_schemaname", "entity");
        param.put("entityno", entityno);
        param.put("where", new Object[]{"entityno = :entityno", param});
        return ServiceUtil.query().findFirst(param);
    }
}
