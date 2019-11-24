package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.FAASChangeInfoService;
import test.utils.Data;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.HashMap;
import java.util.Map;

public class FaasChangeInfoModel {
    private FAASChangeInfoService svc;
    private FAASChangeInfoService remoteSvc;
    
    public FaasChangeInfoModel() {
        svc = ServiceUtil.create(FAASChangeInfoService.class);
        remoteSvc = ServiceUtil.create(FAASChangeInfoService.class, true);
    }
    
    public Map open(Map entity) {
        return open(entity,false);
    }
    
    public Map open(Map entity, boolean remote) {
        if (remote) {
            waitForData(entity);
            return remoteSvc.open(entity);
        }
        return svc.open(entity);
    }
    
    public Map openByRedFlag(Map entity) {
        return svc.openByRedFlag(entity);
    }
    
    
    public Map updateInfo(Map entity) {
        return updateInfo(entity, false);
    }
    
    public Map updateInfo(Map entity, boolean remote) {
        if (remote) {
            waitForData(entity);
            return svc.updateInfo(entity);
        } else {
            return svc.updateInfo(entity);
        }
    }

    
    public Map initChangeOwner(Map faas) {
        Map info = init(faas);
        Map newinfo = new HashMap();
        newinfo.put("taxpayer", faas.get("taxpayer"));
        newinfo.put("owner", faas.get("owner"));
        newinfo.put("administrator", faas.get("administrator"));
        
        //preserve original info
        Map previnfo = new HashMap(newinfo);

        //modify owner data 
        Map newTaxpayer = Entity.findEntity(Entity.ENTITY_002);
        
        Map newOwner = new HashMap(newTaxpayer);
        newOwner.put("address", "NEW ADDRESS");
        
        Map administrator = (Map)faas.get("administrator");
        Map newAdmin = new HashMap(administrator);
        newAdmin.put("name", "NEW " + administrator.get("name"));
        newAdmin.put("address", "NEW " + administrator.get("address"));
        
        newinfo.put("taxpayer", newTaxpayer);
        newinfo.put("owner", newOwner);
        newinfo.put("administrator", newAdmin);
        
        info.put("newinfo", newinfo);
        info.put("previnfo", previnfo);
        return info;
    }
    
    private Map init(Map faas) {
        Map rp = (Map) faas.get("rp");
        Map rpu = (Map) faas.get("rpu");
        Map redflag = (Map) faas.get("_redflag");
        
        Map info = new HashMap();
        info.put("objid", "TCI" + new java.rmi.server.UID());
        info.put("refid", faas.get("objid"));
        info.put("faasid", faas.get("objid"));
        info.put("rpid", rp.get("objid"));
        info.put("rpuid", rpu.get("objid"));
        info.put("action", "change_owner_info");
        info.put("reason", "TEST");
        info.put("redflagid", (redflag != null ? redflag.get("objid") : null));
        return info;
    }

    private void waitForData(Map entity) {
        Data.findRemoteEntity("rpt_changeinfo", entity, true);
        Util.sleep(1000);
    }
    
    
}
