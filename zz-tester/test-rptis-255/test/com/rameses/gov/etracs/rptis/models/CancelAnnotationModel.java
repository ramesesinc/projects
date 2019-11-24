package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.CancelAnnotationService;
import test.utils.Data;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.Map;

public class CancelAnnotationModel {
    private CancelAnnotationService svc;
    private CancelAnnotationService remoteSvc;
    
    public CancelAnnotationModel() {
        svc = ServiceUtil.create(CancelAnnotationService.class);
        remoteSvc = ServiceUtil.create(CancelAnnotationService.class, true);
    }
    
    public Map create(Map entity) {
        return svc.create(entity);
    }
    
    public Map open(Map entity) {
        return open(entity,false);
    }
    
    public Map open(Map entity, boolean remote) {
        if (remote) {
            return remoteSvc.open(entity);
        }
        return svc.open(entity);
    }
    
    public Map submitForApproval(Map entity) {
        return svc.submitForApproval(entity);
    }
    
    public Map approve(Map entity) {
        return svc.approve(entity);
    }
    
    public Map init(Map annotation) {
        Util.sleep(1000);
        Map cancel = svc.init(annotation);
        cancel.put("fileno", "F001");
        cancel.put("remarks", "CANCEL FAAS");
        cancel.put("orno", "OR-001");
        cancel.put("ordate", Data.getServerDate());
        cancel.put("oramount", 50.0);
        cancel.put("signedby", "SIGNED BY");
        cancel.put("signedbytitle", "SIGNED BY TITLE");
        return cancel;
    }
    
}
