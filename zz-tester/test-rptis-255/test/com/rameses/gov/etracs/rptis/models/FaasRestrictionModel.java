package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.FAASRestrictionService;
import test.utils.Data;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.HashMap;
import java.util.Map;

public class FaasRestrictionModel {
    private FAASRestrictionService svc;
    private FAASRestrictionService remoteSvc;
    
    public FaasRestrictionModel() {
        svc = ServiceUtil.create(FAASRestrictionService.class);
        remoteSvc = ServiceUtil.create(FAASRestrictionService.class, true);
    }
    
    
    public Map init(Map faas) {
        Map restriction = new HashMap();
        restriction.put("objid", "TR" + new java.rmi.server.UID());
        restriction.put("state", "DRAFT");
        restriction.put("parent", faas);
        restriction.put("restrictiontype", Data.getRestrictionType("CARP"));
        restriction.put("ledger", null);
        restriction.put("remarks", "TEST");
        return restriction;
    }
    
    
    public Map create(Map entity) {
        entity.put("_schemaname", "faas_restriction");
        return ServiceUtil.persistence().create(entity);
    }
    
    public Map update(Map entity) {
        entity.put("_schemaname", "faas_restriction");
        return ServiceUtil.persistence().update(entity);
    }
    
    public Map open(Map entity) {
        return open(entity,false);
    }
    
    public Map open(Map entity, boolean remote) {
        entity.put("_schemaname", "faas_restriction");
        if (remote) {
            waitForRemoteData(entity);
            return ServiceUtil.persistence(true).read(entity);
        }
        return ServiceUtil.persistence().read(entity);
    }
    
    
    public Map openCancelled(Map restriction, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", "faas_restriction");
        param.put("objid", restriction.get("objid"));
        param.put("state", "UNRESTRICTED");
        param.put("where", new Object[]{"objid = :objid and state = :state", param});
        
        if (remote) {
            Map data = ServiceUtil.query(true).findFirst(param);
            while (data == null) {
                Util.sleep(1000);
                data = ServiceUtil.query(true).findFirst(param);
            }
            return data;
        } else {
            return ServiceUtil.query().findFirst(param);
        }
    }
    
    
    public Map openReactivated(Map restriction, boolean remote) {
        Map param = new HashMap();
        param.put("_schemaname", "faas_restriction");
        param.put("objid", restriction.get("objid"));
        param.put("state", "ACTIVE");
        param.put("where", new Object[]{"objid = :objid and state = :state", param});
        
        if (remote) {
            Map data = ServiceUtil.query(true).findFirst(param);
            while (data == null) {
                Util.sleep(1000);
                data = ServiceUtil.query(true).findFirst(param);
            }
            return data;
        } else {
            return ServiceUtil.query().findFirst(param);
        }
    }



    
    public Map removeRestriction(Map entity) {
        return svc.removeRestriction(entity);
    }
    
    public Map approve(Map entity) {
        return svc.approve(entity);
    }
    
    public Map cancel(Map restriction) {
        //simulate payment (receipt)
        return svc.cancel(restriction, getReceipt());
    }
    
    
    public Map voidPayment(Map restriction) {
        return svc.reactivate(restriction);
    }
    
    public Map getReceipt() {
        Map receipt = new HashMap();
        receipt.put("objid", "R0001");
        receipt.put("receiptno", "R0001");
        receipt.put("receiptdate", Data.format(Data.getServerDate()));
        receipt.put("amount", 50.00);
        receipt.put("lastyearpaid", Data.getCurrentYear());
        receipt.put("lastqtrpaid", 4);
        return receipt;
    }

    
    private void waitForRemoteData(Map entity) {
        Data.findRemoteEntity("faas_restriction", entity, true);
        Util.sleep(1000);
    }

    
}
