/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.LandRYSettingLookupService;
import test.utils.ServiceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FaasLand {
    private static LandRYSettingLookupService settingSvc;
    
    
    static {
        settingSvc = ServiceUtil.create(LandRYSettingLookupService.class);
    }
    
    public static void assess(Map faas){
        addLandDetail(faas);
        addAssessment(faas);
        
    }

    private static void addLandDetail(Map faas) {
        Map rpu = (Map) faas.get("rpu");
        double areasqm = 100.0;
        double areaha = areasqm / 10000.0;
        rpu.put("totalareasqm", areasqm);
        rpu.put("totalareaha", areaha);
        
        Map subclass = getLandSubclass(faas);
        Map actualuse = getLandActualUse(faas);
        
        Map ld = new HashMap();
        ld.put("objid", "LD" + new java.rmi.server.UID());
        ld.put("landrpuid", rpu.get("objid"));
        ld.put("subclass", subclass);
        ld.put("landspecificclass", subclass.get("landspecificclass"));
        ld.put("specificclass", subclass.get("specificclass"));
        ld.put("actualuse", actualuse);
        ld.put("stripping_objid", null);
        ld.put("addlinfo", null);
        ld.put("striprate", 0);
        ld.put("areatype", "SQM");
        ld.put("area", areasqm);
        ld.put("areasqm", areasqm);
        ld.put("areaha", areaha);
        ld.put("basevalue", 1000.0);
        ld.put("unitvalue", 1000.0);
        ld.put("taxable", true);
        ld.put("basemarketvalue",  100000.0);
        ld.put("adjustment", 0.0);
        ld.put("landvalueadjustment", 0.0);
        ld.put("actualuseadjustment", 0.0);
        ld.put("marketvalue", 100000.0);
        ld.put("assesslevel", 10.0);
        ld.put("assessedvalue", 10000.0);
        
        List<Map> list = new ArrayList<Map>();
        list.add(ld);
        rpu.put("landdetails", list);
    }
    
    private static void addAssessment(Map faas) {
        Map rpu = (Map) faas.get("rpu");
        
        Map assessment = new HashMap();
        assessment.put("objid", "RA" + new java.rmi.server.UID());
        assessment.put("rpuid", rpu.get("objid"));
        assessment.put("rputype", "land");
        assessment.put("classification", rpu.get("classification"));
        assessment.put("actualuse", rpu.get("classification"));
        assessment.put("areasqm", 100.0);
        assessment.put("areaha", 0.0100); 
        assessment.put("marketvalue", 100000.0);
        assessment.put("assesslevel", 10.0);
        assessment.put("assessedvalue", 10000.0);
        assessment.put("taxable", true);
        
        List<Map> list = new ArrayList<Map>();
        list.add(assessment);
        rpu.put("assessments", list);
    }
    
    
    public static Map getLandSubclass(Map faas) {
        Map rpu = (Map) faas.get("rpu");
        Map param = new HashMap();
        param.put("lguid", faas.get("lguid"));
        param.put("ry", rpu.get("ry"));
        param.put("searchtext", "RESIDENTIAL");
        List<Map> list = settingSvc.lookupSubclasses(param);
        if (list.isEmpty()) {
            throw new RuntimeException("Unable to lookup subclass.");
        }
        return list.get(0);
    }

    private static Map getLandActualUse(Map faas) {
        Map rpu = (Map) faas.get("rpu");
        Map param = new HashMap();
        param.put("lguid", faas.get("lguid"));
        param.put("ry", rpu.get("ry"));
        param.put("searchtext", "RESIDENTIAL");
        List<Map> list = settingSvc.lookupAssessLevels(param);
        if (list.isEmpty()) {
            throw new RuntimeException("Unable to lookup subclass.");
        }
        return list.get(0);
    }

}
