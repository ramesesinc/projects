/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

import com.rameses.gov.etracs.services.FAASLookupService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Lookup {
    
    public static Map faas(Object tdno) {
        FAASLookupService svc = ServiceUtil.create(FAASLookupService.class);
        Map params = new HashMap();
        params.put("tdno", tdno);
        List<Map> items = svc.lookupFaas(params);
        if (items.isEmpty()){
            throw new RuntimeException("faas " + tdno + " should be existing.");
        }
        return (Map)items.get(0);
    }
    
    public static Map faasById(Object objid) {
        FAASLookupService svc = ServiceUtil.create(FAASLookupService.class);
        Map params = new HashMap();
        params.put("objid", objid);
        List<Map> items = svc.lookupFaas(params);
        if (items.isEmpty()){
            throw new RuntimeException("faas " + objid + " should be existing.");
        }
        return (Map)items.get(0);
    }

    public static Map newFaas(Map faas) {
        Map params = new HashMap();
        params.put("_schemaname", "faas");
        params.put("tdno", faas.get("tdno"));
        params.put("where", new Object[]{"prevtdno = :tdno", params});
        return ServiceUtil.query().findFirst(params);
    }

}
